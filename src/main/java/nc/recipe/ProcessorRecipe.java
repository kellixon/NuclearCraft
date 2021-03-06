package nc.recipe;

import java.util.List;

import nc.config.NCConfig;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.internal.fluid.Tank;
import nc.util.RecipeHelper;
import net.minecraft.item.ItemStack;

public class ProcessorRecipe implements IRecipe {
	
	protected List<IItemIngredient> itemIngredients, itemProducts;
	protected List<IFluidIngredient> fluidIngredients, fluidProducts;
	
	private int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
	
	protected List extras;
	public boolean isShapeless;
	
	public ProcessorRecipe(List<IItemIngredient> itemIngredientsList, List<IFluidIngredient> fluidIngredientsList, List<IItemIngredient> itemProductsList, List<IFluidIngredient> fluidProductsList, List extrasList, boolean shapeless) {
		itemIngredients = itemIngredientsList;
		fluidIngredients = fluidIngredientsList;
		itemProducts = itemProductsList;
		fluidProducts = fluidProductsList;
		
		itemInputSize = itemIngredientsList.size();
		fluidInputSize = fluidIngredientsList.size();
		itemOutputSize = itemProductsList.size();
		fluidOutputSize = fluidProductsList.size();
		
		extras = extrasList;
		isShapeless = shapeless;
	}
	
	@Override
	public List<IItemIngredient> itemIngredients() {
		return itemIngredients;
	}
	
	@Override
	public List<IFluidIngredient> fluidIngredients() {
		return fluidIngredients;
	}
	
	@Override
	public List<IItemIngredient> itemProducts() {
		return itemProducts;
	}
	
	@Override
	public List<IFluidIngredient> fluidProducts() {
		return fluidProducts;
	}
	
	@Override
	public List extras() {
		return extras;
	}

	@Override
	public boolean matchingInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		return RecipeHelper.matchingIngredients(SorptionType.INPUT, itemIngredients, fluidIngredients, itemInputs, fluidInputs, isShapeless);
	}

	@Override
	public boolean matchingOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
		return RecipeHelper.matchingIngredients(SorptionType.OUTPUT, itemProducts, fluidProducts, itemOutputs, fluidOutputs, isShapeless);
	}
	
	@Override
	public boolean matchingIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients) {
		return RecipeHelper.matchingIngredients(SorptionType.INPUT, this.itemIngredients, this.fluidIngredients, itemIngredients, fluidIngredients, isShapeless);
	}

	@Override
	public boolean matchingProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		return RecipeHelper.matchingIngredients(SorptionType.OUTPUT, this.itemProducts, this.fluidProducts, itemProducts, fluidProducts, isShapeless);
	}
	
	// Recipe Info
	
	// Processors
	
	public double getProcessTime(double defaultProcessTime) {
		if (extras.isEmpty()) return defaultProcessTime;
		else if (extras.get(0) instanceof Double) return ((double) extras.get(0))*defaultProcessTime;
		else return defaultProcessTime;
	}
	
	public double getProcessPower(double defaultProcessPower) {
		if (extras.size() < 2) return defaultProcessPower;
		else if (extras.get(1) instanceof Double) return ((double) extras.get(1))*defaultProcessPower;
		else return defaultProcessPower;
	}
	
	// Decay Generator
	
	public double getDecayLifetime() {
		if (extras.isEmpty()) return TileDecayGenerator.DEFAULT_LIFETIME;
		else if (extras.get(0) instanceof Double) return ((double) extras.get(0))/(double)NCConfig.machine_update_rate;
		return TileDecayGenerator.DEFAULT_LIFETIME;
	}
	
	public int getDecayPower() {
		if (extras.size() < 2) return TileDecayGenerator.DEFAULT_POWER;
		if (extras.get(1) instanceof Double) return (int) (((double) extras.get(1))*NCConfig.machine_update_rate/20D);
		return TileDecayGenerator.DEFAULT_POWER;
	}
	
	// Fission
	
	public double getFissionFuelTime() {
		if (extras.isEmpty()) return 1D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 1D;
	}
	
	public double getFissionFuelPower() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	public double getFissionFuelHeat() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	// Fusion
	
	public double getFusionComboTime() {
		if (extras.isEmpty()) return 1D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 1D;
	}
	
	public double getFusionComboPower() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	public double getFusionComboHeatVariable() {
		if (extras.size() < 3) return 1000D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(2);
		else return 1000D;
	}
	
	// Salt Fission Vessel
	
	public double getSaltFissionFuelTime() {
		if (extras.isEmpty()) return 1D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 1D;
	}
	
	public double getSaltFissionFuelHeat() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	// Coolant Heater
	
	public double getCoolantHeaterCoolingRate() {
		if (extras.isEmpty()) return 10D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 0D;
	}
}
