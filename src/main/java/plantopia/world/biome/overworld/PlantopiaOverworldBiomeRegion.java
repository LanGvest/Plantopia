package plantopia.world.biome.overworld;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import plantopia.Plantopia;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class PlantopiaOverworldBiomeRegion {
	public static final int REGULAR_WEIGHT = 10;

	public static class Regular extends Region {
		public static final ResourceLocation LOCATION = new ResourceLocation(Plantopia.MOD_ID, "overworld_regular");

		public Regular() {
			super(LOCATION, RegionType.OVERWORLD, REGULAR_WEIGHT);
		}

		@Override
		public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
			new PlantopiaOverworldBiomeBuilder().addBiomes(mapper);
		}
	}
}