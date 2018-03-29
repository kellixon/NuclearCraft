package nc.integration.crafttweaker;

import java.util.ArrayList;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.recipe.RecipeOreStack;
import nc.recipe.SorptionType;
import nc.recipe.StackType;
import nc.util.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RemoveRecipe implements IAction {
	
	public ArrayList<IIngredient> ingredients;
	public SorptionType type;
	public IRecipe recipe;
	public boolean wasNull, wrongSize;
	public final NCRecipes.Type recipeType;

	public RemoveRecipe(NCRecipes.Type recipeType, SorptionType type, ArrayList<Object> ingredients) {
		this.recipeType = recipeType;
		this.type = type;
		if (type == SorptionType.OUTPUT ? ingredients.size() != recipeType.getRecipeHandler().outputSizeItem + recipeType.getRecipeHandler().outputSizeFluid : ingredients.size() != recipeType.getRecipeHandler().inputSizeItem + recipeType.getRecipeHandler().inputSizeFluid) {
			CraftTweakerAPI.logError("A " + recipeType.getRecipeHandler().getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}

		ArrayList adaptedIngredients = new ArrayList();
		for (Object output : ingredients) {
			if (output == null) {
				CraftTweakerAPI.logError(String.format("An ingredient of a %s was null", recipeType.getRecipeHandler().getRecipeName()));
				wasNull = true;
				return;
			}
			if (output instanceof IItemStack) {
				adaptedIngredients.add(((IItemStack) output).getInternal());
				continue;
			} else if (output instanceof IOreDictEntry) {
				adaptedIngredients.add(new RecipeOreStack(((IOreDictEntry) output).getName(), StackType.ITEM, ((IOreDictEntry) output).getAmount()));
				continue;
			} else if (output instanceof IngredientStack) {
				ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
				((IngredientStack) output).getItems().forEach(ingredient -> stackList.add(StackHelper.changeStackSize((ItemStack) ((IItemStack) ingredient).getInternal(), ((IngredientStack) output).getAmount())));
				adaptedIngredients.add(recipeType.getRecipeHandler().buildRecipeObject(stackList));
				continue;
			} else if (output instanceof ILiquidStack) {
				//adaptedIngredients.add(recipeType.getRecipeHandler().buildRecipeObject(CraftTweakerMC.getLiquidStack((ILiquidStack) output)));
				FluidStack fluidStack = (FluidStack) ((ILiquidStack) output).getInternal();
				adaptedIngredients.add(new RecipeOreStack(fluidStack.getFluid().getName(), StackType.FLUID, fluidStack.amount));
				continue;
			} else if (!(output instanceof ItemStack)) {
				CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeHandler().getRecipeName(), output.getClass().getName(), output));
			} else {
				adaptedIngredients.add(output);
				continue;
			}
		}

		this.ingredients = adaptedIngredients;
		this.recipe = type == SorptionType.OUTPUT ? recipeType.getRecipeHandler().getRecipeFromOutputs(adaptedIngredients.toArray()) : recipeType.getRecipeHandler().getRecipeFromInputs(adaptedIngredients.toArray());
	}
	
	@Override
	public void apply() {
		if (recipe == null) {
			CraftTweakerAPI.logError(String.format("%s: Removing Recipe - Couldn't find matching recipe %s", recipeType.getRecipeHandler().getRecipeName(), RecipeMethods.getIngredientNames(ingredients)));
			return;
		}
		if (!wasNull && !wrongSize) {
			boolean removed = recipeType.getRecipeHandler().removeRecipe(recipe);
			if (!removed){
				CraftTweakerAPI.logError(String.format("%s: Removing Recipe - Failed to remove recipe %s", recipeType.getRecipeHandler().getRecipeName(), RecipeMethods.getIngredientNames(ingredients)));
			}else{
				//CraftTweakerAPI.getIjeiRecipeRegistry().removeRecipe(JEIMethods.createJEIRecipe(recipe, helper));
			}
		}
	}
	
	public boolean canUndo() {
		return true;
	}
	
	public void undo() {
		if (recipe != null && !wasNull && !wrongSize) {
			recipeType.getRecipeHandler().addRecipe(recipe);
			//CraftTweakerAPI.getIjeiRecipeRegistry().addRecipe(JEIMethods.createJEIRecipe(recipe, helper));
		}
	}
	
	@Override
	public String describe() {
		if (recipe == null) {
			return "ERROR: RECIPE IS NULL";
		}
		return String.format("Removing %s recipe (%s -> %s)", recipeType.getRecipeHandler().getRecipeName(), RecipeMethods.getIngredientNames(recipe.inputs()), RecipeMethods.getIngredientNames(recipe.outputs()));
	}
	
	public String describeUndo() {
		return String.format("Reverting /%s/", describe());
	}
	
	public Object getOverrideKey() {
		return null;
	}
}
