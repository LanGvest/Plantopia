package plantopia.world.biome.overworld;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.core.Holder;

import javax.annotation.Nullable;

public class PlantopiaOverworldBiomeMaker {
	@Nullable
	private static final Music MUSIC = null;

	public static Biome marsh() {
		// Mob spawns
		MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.commonSpawns(spawnBuilder);

		// Biome features
		BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP);
//		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS);
//		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, BOPVegetationPlacements.PATCH_TALL_GRASS_250);
//		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, BOPVegetationPlacements.PATCH_REED_10);
//		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, BOPVegetationPlacements.PATCH_GRASS_24);
//		addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, BOPVegetationPlacements.PATCH_WATERGRASS_50);

		return biome(Biome.Precipitation.RAIN, Biome.BiomeCategory.SWAMP, 0.65f, 0.7f, spawnBuilder, biomeBuilder, MUSIC);
	}

	private static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music) {
		return biome(precipitation, category, temperature, downfall, 4159204, 329011, spawnBuilder, biomeBuilder, music);
	}

	private static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, int waterColor, int waterFogColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music) {
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

	private static void addFeature(BiomeGenerationSettings.Builder builder, GenerationStep.Decoration step, Holder<PlacedFeature> feature) {
		builder.addFeature(step, feature);
	}

	private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
		BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
		BiomeDefaultFeatures.addDefaultSprings(builder);
		BiomeDefaultFeatures.addSurfaceFreezing(builder);
	}

	protected static int getSkyColorByTemperature(float temperature) {
		float temp = temperature / 3f;
		temp = Mth.clamp(temp, -1f, 1f);
		return Mth.hsvToRgb(0.62222224f - temp * 0.05f, 0.5f + temp * 0.1f, 1f);
	}
}