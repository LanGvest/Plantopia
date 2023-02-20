package plantopia.world.biome.overworld;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlantopiaOverworldBiomeMaker {
	@Nullable
	private static final Music NORMAL_MUSIC = null;

	public static @NotNull Biome marsh() {
		// Mob spawns
		MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
		marshSpawns(spawnBuilder);

		// Biome features
		BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP);
//		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, PlantopiaVegetationPlacements.PATCH_WATERGRASS);

		return biome(Biome.Precipitation.RAIN, Biome.BiomeCategory.SWAMP, 0.65f, 0.7f, spawnBuilder, biomeBuilder, NORMAL_MUSIC);
	}

	@SuppressWarnings("SameParameterValue")
	private static @NotNull Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music) {
		return biome(precipitation, category, temperature, downfall, 4159204, 329011, spawnBuilder, biomeBuilder, music);
	}

	@SuppressWarnings("SameParameterValue")
	private static @NotNull Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, int waterColor, int waterFogColor, MobSpawnSettings.@NotNull Builder spawnBuilder, BiomeGenerationSettings.@NotNull Builder biomeBuilder, @Nullable Music music) {
		return new Biome.BiomeBuilder()
			.precipitation(precipitation)
			.biomeCategory(category)
			.temperature(temperature)
			.downfall(downfall)
			.specialEffects(
				new BiomeSpecialEffects.Builder()
					.waterColor(waterColor)
					.waterFogColor(waterFogColor)
					.fogColor(12638463)
					.skyColor(getSkyColorByTemperature(temperature))
					.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
					.backgroundMusic(music)
					.build()
			)
			.mobSpawnSettings(spawnBuilder.build())
			.generationSettings(biomeBuilder.build())
			.build();
	}

	@SuppressWarnings("SameParameterValue")
	private static void addFeature(BiomeGenerationSettings.@NotNull Builder builder, GenerationStep.Decoration step, Holder<PlacedFeature> feature) {
		builder.addFeature(step, feature);
	}

	private static void globalOverworldGeneration(BiomeGenerationSettings.Builder biomeBuilder) {
		BiomeDefaultFeatures.addDefaultCarversAndLakes(biomeBuilder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomeBuilder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(biomeBuilder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeBuilder);
		BiomeDefaultFeatures.addDefaultSprings(biomeBuilder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomeBuilder);
	}

	private static void marshSpawns(MobSpawnSettings.Builder spawnBuilder) {
		BiomeDefaultFeatures.commonSpawns(spawnBuilder);
		spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
	}

	protected static int getSkyColorByTemperature(float temperature) {
		float temp = temperature / 3f;
		temp = Mth.clamp(temp, -1f, 1f);
		return Mth.hsvToRgb(0.62222224f - temp * 0.05f, 0.5f + temp * 0.1f, 1f);
	}
}