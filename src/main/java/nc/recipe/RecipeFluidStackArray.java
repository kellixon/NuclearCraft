package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.fluids.FluidStack;

public class RecipeFluidStackArray implements IFluidIngredient {
	
	public List<IFluidIngredient> ingredientList;
	public List<FluidStack> cachedStackList = new ArrayList<FluidStack>();
	
	public RecipeFluidStackArray(IFluidIngredient... ingredients) {
		this(Lists.newArrayList(ingredients));
	}

	public RecipeFluidStackArray(List<IFluidIngredient> ingredientList) {
		this.ingredientList = ingredientList;
		ingredientList.forEach(input -> cachedStackList.add(input.getStack()));
	}

	@Override
	public FluidStack getStack() {
		if (cachedStackList == null || cachedStackList.isEmpty()) return null;
		return cachedStackList.get(0);
	}
	
	@Override
	public String getIngredientName() {
		return ingredientList.get(0).getIngredientName();
	}
	
	@Override
	public String getIngredientNamesConcat() {
		String names = "";
		for (IFluidIngredient ingredient : ingredientList) names += (", " + ingredient.getIngredientName());
		return names.substring(2);
	}

	@Override
	public int getMaxStackSize() {
		return ingredientList.get(0).getMaxStackSize();
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		for (IFluidIngredient ingredient : ingredientList) ingredient.setMaxStackSize(stackSize);
		for (FluidStack stack : cachedStackList) stack.amount = stackSize;
	}

	@Override
	public List<FluidStack> getInputStackList() {
		List<FluidStack> stacks = new ArrayList<FluidStack>();
		ingredientList.forEach(ingredient -> ingredient.getInputStackList().forEach(obj -> stacks.add(obj)));
		return stacks;
	}
	
	@Override
	public List<FluidStack> getOutputStackList() {
		if (cachedStackList == null || cachedStackList.isEmpty()) return new ArrayList<FluidStack>();
		return Lists.newArrayList(getStack());
	}

	@Override
	public boolean matches(Object object, SorptionType sorption) {
		for (IFluidIngredient ingredient : ingredientList) {
			if (ingredient.matches(object, sorption)) {
				return true;
			}
		}
		return false;
	}
}
