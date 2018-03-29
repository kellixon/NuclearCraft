package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidCryotheum extends FluidBase {
	
	public FluidCryotheum(String fluidName) {
		super(fluidName, true);
		setViscosity(8000);
		setTemperature(100);
		setLuminosity(10);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidCryotheum(String fluidName, Integer colour) {
		super(fluidName, true, "molten", colour);
		setViscosity(8000);
		setTemperature(100);
		setLuminosity(10);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
