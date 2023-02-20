package plantopia.world.biome.overworld;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import plantopia.world.biome.PlantopiaBiomes;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * <code>continentalness</code> - low to generate near coasts, far to generate away from coasts.<br/>
 * <code>erosion</code> - low is hilly terrain, high is flat terrain.
 */
public class PlantopiaOverworldBiomeBuilder {
	private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
	private final Climate.Parameter DEPTH_RANGE = Climate.Parameter.span(0.2F, 0.9F);

	private final Climate.Parameter[] temperatures = new Climate.Parameter[]{
		Climate.Parameter.span(-1.0F, -0.45F),
		Climate.Parameter.span(-0.45F, -0.15F),
		Climate.Parameter.span(-0.15F, 0.2F),
		Climate.Parameter.span(0.2F, 0.55F),
		Climate.Parameter.span(0.55F, 1.0F)
	};

	private final Climate.Parameter[] humidities = new Climate.Parameter[]{
		Climate.Parameter.span(-1.0F, -0.35F),
		Climate.Parameter.span(-0.35F, -0.1F),
		Climate.Parameter.span(-0.1F, 0.1F),
		Climate.Parameter.span(0.1F, 0.3F),
		Climate.Parameter.span(0.3F, 1.0F)
	};

	private final Climate.Parameter[] erosions = new Climate.Parameter[]{
		Climate.Parameter.span(-1.0F, -0.78F),
		Climate.Parameter.span(-0.78F, -0.375F),
		Climate.Parameter.span(-0.375F, -0.2225F),
		Climate.Parameter.span(-0.2225F, 0.05F),
		Climate.Parameter.span(0.05F, 0.45F),
		Climate.Parameter.span(0.45F, 0.55F),
		Climate.Parameter.span(0.55F, 1.0F)
	};

	private final Climate.Parameter FROZEN_RANGE = temperatures[0];
	private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(temperatures[1], temperatures[4]);
	private final Climate.Parameter islandContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
	private final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
	private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
	private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
	private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
	private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
	private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
	private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);

	/* MINECRAFT BIOMES ******************************************/

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MINECRAFT_OCEANS = new ResourceKey[][]{
		{Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN},
		{Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MINECRAFT_MIDDLE_BIOMES = new ResourceKey[][]{
		{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA},
		{Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
		{Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST},
		{Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE},
		{Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MINECRAFT_MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
		{Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null},
		{null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA},
		{Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null},
		{null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MINECRAFT_PLATEAU_BIOMES = new ResourceKey[][]{
		{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
		{Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
		{Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.DARK_FOREST},
		{Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE},
		{Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MINECRAFT_PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
		{Biomes.ICE_SPIKES, null, null, null, null},
		{null, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA},
		{null, null, Biomes.FOREST, Biomes.BIRCH_FOREST, null},
		{null, null, null, null, null},
		{Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MINECRAFT_SHATTERED_BIOMES = new ResourceKey[][]{
		{Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
		{Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
		{Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	/* PLANTOPIA BIOMES ******************************************/

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLANTOPIA_MIDDLE_BIOMES = new ResourceKey[][]{
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLANTOPIA_MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLANTOPIA_PLATEAU_BIOMES = new ResourceKey[][]{
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLANTOPIA_PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLANTOPIA_SHATTERED_BIOMES = new ResourceKey[][]{
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLANTOPIA_SWAMP_BIOMES = new ResourceKey[][]{
		{null, null, null, null, null},
		{null, null, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH},
		{PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH},
		{PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH, PlantopiaBiomes.MARSH},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLANTOPIA_ISLAND_BIOMES = new ResourceKey[][]{
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	/* ADD BIOMES ******************************************/

	public void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addOffCoastBiomes(mapper);
		addInlandBiomes(mapper);
		addUndergroundBiomes(mapper);
	}

	/* OFF COAST BIOMES ******************************************/

	private void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		for (int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			addSurfaceBiome(mapper, temperature, FULL_RANGE, deepOceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0F, MINECRAFT_OCEANS[0][i]);
			addSurfaceBiome(mapper, temperature, FULL_RANGE, oceanContinentalness, FULL_RANGE, FULL_RANGE, 0.0F, MINECRAFT_OCEANS[1][i]);
			for (int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> islandBiome = pickPlantopiaIslandBiome(i, j);
				addSurfaceBiome(mapper, temperature, humidity, islandContinentalness, FULL_RANGE, FULL_RANGE, 0.0F, islandBiome);
			}
		}
	}

	/* INLAND BIOMES ******************************************/

	@SuppressWarnings("GrazieInspection")
	private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		/*
		A weirdness range (second parameter of functions below) is needed to specify a certain inland slice in
		a repeating triangle wave fashion:
		1                                     2
		|        PEAKS                        |     PEAKS
		|    HIGH     HIGH                    | HIGH     HIGH
		| MID             MID             MID |              MID
		|                    LOW       LOW    |
		|                       VALLEYS       |
		 */

		/* FIRST CYCLE ******************************************/

		addMidSlice(mapper, Climate.Parameter.span(-1.0F, -0.93333334F));
		addHighSlice(mapper, Climate.Parameter.span(-0.93333334F, -0.7666667F));
		addPeaks(mapper, Climate.Parameter.span(-0.7666667F, -0.56666666F));
		addHighSlice(mapper, Climate.Parameter.span(-0.56666666F, -0.4F));
		addMidSlice(mapper, Climate.Parameter.span(-0.4F, -0.26666668F));
		addLowSlice(mapper, Climate.Parameter.span(-0.26666668F, -0.05F));
		addValleys(mapper, Climate.Parameter.span(-0.05F, 0.05F));
		addLowSlice(mapper, Climate.Parameter.span(0.05F, 0.26666668F));
		addMidSlice(mapper, Climate.Parameter.span(0.26666668F, 0.4F));

		/* TRUNCATED SECOND CYCLE ******************************************/

		addHighSlice(mapper, Climate.Parameter.span(0.4F, 0.56666666F));
		addPeaks(mapper, Climate.Parameter.span(0.56666666F, 0.7666667F));
		addHighSlice(mapper, Climate.Parameter.span(0.7666667F, 0.93333334F));
		addMidSlice(mapper, Climate.Parameter.span(0.93333334F, 1.0F));
	}

	/* UNDERGROUND BIOMES ******************************************/

	private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addUndergroundBiome(mapper, FULL_RANGE, FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), FULL_RANGE, FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
		addUndergroundBiome(mapper, FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), FULL_RANGE, FULL_RANGE, FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
	}

	/* INLAND BIOMES SLICES ******************************************/

	@SuppressWarnings("DuplicatedCode")
	private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		for (int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for (int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickPlantopiaMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickPlantopiaMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickPlantopiaMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> plateauBiome = pickPlantopiaPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = pickPlantopiaShatteredBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickPlantopiaWindsweptSavannaBiome(i, j, weirdness, shatteredBiome);
				ResourceKey<Biome> peakBiome = pickPlantopiaPeakBiome(i, j, weirdness);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0F, peakBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0F, peakBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[2], weirdness, 0.0F, plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, midInlandContinentalness, erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, farInlandContinentalness, erosions[3], weirdness, 0.0F, plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[5], weirdness, 0.0F, windsweptSavannaBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0F, shatteredBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, middleBiome);
			}
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		for (int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for (int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickPlantopiaMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickPlantopiaMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickPlantopiaMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> plateauBiome = pickPlantopiaPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = pickPlantopiaShatteredBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickPlantopiaWindsweptSavannaBiome(i, j, weirdness, middleBiome);
				ResourceKey<Biome> slopeBiome = pickPlantopiaSlopeBiome(i, j, weirdness);
				ResourceKey<Biome> peakBiome = pickPlantopiaPeakBiome(i, j, weirdness);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[0], weirdness, 0.0F, slopeBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0F, peakBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0F, slopeBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[2], weirdness, 0.0F, plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, midInlandContinentalness, erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, farInlandContinentalness, erosions[3], weirdness, 0.0F, plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[5], weirdness, 0.0F, windsweptSavannaBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0F, shatteredBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, middleBiome);
			}
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		addSurfaceBiome(mapper, FULL_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
		for (int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for (int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickPlantopiaMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> biomeOrBadlandsIfHot = pickPlantopiaMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickPlantopiaMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = pickPlantopiaShatteredBiome(i, j, weirdness);
				ResourceKey<Biome> plateauBiome = pickPlantopiaPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> beachBiome = pickPlantopiaBeachBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickPlantopiaWindsweptSavannaBiome(i, j, weirdness, middleBiome);
				ResourceKey<Biome> shatteredCoastBiome = pickPlantopiaShatteredCoastBiome(i, j, weirdness);
				ResourceKey<Biome> slopeBiome = pickPlantopiaSlopeBiome(i, j, weirdness);
				ResourceKey<Biome> swampBiome = maybePickPlantopiaSwampBiome(i, j, weirdness, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0F, slopeBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, midInlandContinentalness), erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
				addSurfaceBiome(mapper, temperature, humidity, farInlandContinentalness, erosions[1], weirdness, 0.0F, i == 0 ? slopeBiome : plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[2], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, midInlandContinentalness, erosions[2], weirdness, 0.0F, biomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, farInlandContinentalness, erosions[2], weirdness, 0.0F, plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[3], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[3], weirdness, 0.0F, biomeOrBadlandsIfHot);
				if (weirdness.max() < 0L) {
					addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[4], weirdness, 0.0F, beachBiome);
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				} else {
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				}
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[5], weirdness, 0.0F, shatteredCoastBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[5], weirdness, 0.0F, windsweptSavannaBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0F, shatteredBiome);
				if (weirdness.max() < 0L) {
					addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0F, beachBiome);
				} else {
					addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0F, middleBiome);
				}
				if (i == 0) {
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, middleBiome);
				} else {
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, swampBiome);
				}
			}
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		addSurfaceBiome(mapper, FULL_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
		for (int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for (int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickPlantopiaMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickPlantopiaMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickPlantopiaMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> beachBiome = pickPlantopiaBeachBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickPlantopiaWindsweptSavannaBiome(i, j, weirdness, middleBiome);
				ResourceKey<Biome> shatteredCoastBiome = pickPlantopiaShatteredCoastBiome(i, j, weirdness);
				ResourceKey<Biome> swampBiome = maybePickPlantopiaSwampBiome(i, j, weirdness, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, Climate.Parameter.span(erosions[3], erosions[4]), weirdness, 0.0F, beachBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[5], weirdness, 0.0F, shatteredCoastBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[5], weirdness, 0.0F, windsweptSavannaBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0F, beachBiome);
				if (i == 0) {
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, middleBiome);
				} else {
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, swampBiome);
				}
			}
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		addSurfaceBiome(mapper, FROZEN_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, weirdness.max() < 0L ? Biomes.STONY_SHORE : Biomes.FROZEN_RIVER);
		addSurfaceBiome(mapper, UNFROZEN_RANGE, FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, weirdness.max() < 0L ? Biomes.STONY_SHORE : Biomes.RIVER);
		addSurfaceBiome(mapper, FROZEN_RANGE, FULL_RANGE, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, Biomes.FROZEN_RIVER);
		addSurfaceBiome(mapper, UNFROZEN_RANGE, FULL_RANGE, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, Biomes.RIVER);
		addSurfaceBiome(mapper, FROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0.0F, Biomes.FROZEN_RIVER);
		addSurfaceBiome(mapper, UNFROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0.0F, Biomes.RIVER);
		addSurfaceBiome(mapper, FROZEN_RANGE, FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
		addSurfaceBiome(mapper, UNFROZEN_RANGE, FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0.0F, Biomes.RIVER);
		addSurfaceBiome(mapper, FROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
		for (int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for (int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickPlantopiaMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> swampBiome = maybePickPlantopiaSwampBiome(i, j, weirdness, middleBiomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
				if (i != 0) {
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, swampBiome);
				}
			}
		}
	}

	/* PICK VARIATIVE BIOME ******************************************/

	@Contract(pure = true)
	private ResourceKey<Biome> pickVariativeBiome(int temperatureIndex, int humidityIndex, Climate.@NotNull Parameter weirdness, @NotNull ResourceKey<Biome>[][] normal, @NotNull ResourceKey<Biome>[][] variant) {
		ResourceKey<Biome> biome = normal[temperatureIndex][humidityIndex];
		if (weirdness.max() < 0L) return biome;
		ResourceKey<Biome> biomeVariant = variant[temperatureIndex][humidityIndex];
		if (biomeVariant == null) return biome;
		return biomeVariant;
	}

	/* PICK MIDDLE BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return pickVariativeBiome(temperatureIndex, humidityIndex, weirdness, MINECRAFT_MIDDLE_BIOMES, MINECRAFT_MIDDLE_BIOMES_VARIANT);
	}

	private ResourceKey<Biome> pickPlantopiaMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome = pickVariativeBiome(temperatureIndex, humidityIndex, weirdness, PLANTOPIA_MIDDLE_BIOMES, PLANTOPIA_MIDDLE_BIOMES_VARIANT);
		if (biome == null) biome = pickMinecraftMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* PICK MIDDLE BIOME OR BADLANDS IF HOT ******************************************/

	private ResourceKey<Biome> pickMinecraftMiddleBiomeOrBadlandsIfHot(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex == 4) biome = pickMinecraftBadlandsBiome(temperatureIndex, humidityIndex, weirdness);
		else biome = pickMinecraftMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	private ResourceKey<Biome> pickPlantopiaMiddleBiomeOrBadlandsIfHot(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex == 4) biome = pickPlantopiaBadlandsBiome(temperatureIndex, humidityIndex, weirdness);
		else biome = pickPlantopiaMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		if (biome == null) biome = pickMinecraftMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* PICK MIDDLE BIOME OR BADLANDS IF HOT OR SLOPE IF COLD ******************************************/

	private ResourceKey<Biome> pickMinecraftMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex == 0) biome = pickMinecraftSlopeBiome(temperatureIndex, humidityIndex, weirdness);
		else biome = pickMinecraftMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	private ResourceKey<Biome> pickPlantopiaMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex == 0) biome = pickPlantopiaSlopeBiome(temperatureIndex, humidityIndex, weirdness);
		else biome = pickPlantopiaMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
		if (biome == null)
			biome = pickMinecraftMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* PICK BADLANDS BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftBadlandsBiome(@SuppressWarnings("unused") int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (humidityIndex < 2) biome = weirdness.max() < 0L ? Biomes.ERODED_BADLANDS : Biomes.BADLANDS;
		else biome = humidityIndex < 3 ? Biomes.BADLANDS : Biomes.WOODED_BADLANDS;
		return biome;
	}

	private ResourceKey<Biome> pickPlantopiaBadlandsBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return pickMinecraftBadlandsBiome(temperatureIndex, humidityIndex, weirdness);
	}

	/* PICK SLOPE BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex >= 3) biome = pickMinecraftPlateauBiome(temperatureIndex, humidityIndex, weirdness);
		else biome = humidityIndex <= 1 ? Biomes.SNOWY_SLOPES : Biomes.GROVE;
		return biome;
	}

	private ResourceKey<Biome> pickPlantopiaSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex >= 3) biome = pickPlantopiaPlateauBiome(temperatureIndex, humidityIndex, weirdness);
		else biome = null;
		if (biome == null) biome = pickMinecraftSlopeBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* PICK PLATEAU BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return pickVariativeBiome(temperatureIndex, humidityIndex, weirdness, MINECRAFT_PLATEAU_BIOMES, MINECRAFT_PLATEAU_BIOMES_VARIANT);
	}

	private ResourceKey<Biome> pickPlantopiaPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome = pickVariativeBiome(temperatureIndex, humidityIndex, weirdness, PLANTOPIA_PLATEAU_BIOMES, PLANTOPIA_PLATEAU_BIOMES_VARIANT);
		if (biome == null) biome = pickMinecraftPlateauBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* PICK SHATTERED BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome = MINECRAFT_SHATTERED_BIOMES[temperatureIndex][humidityIndex];
		if (biome == null) biome = pickMinecraftMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	private ResourceKey<Biome> pickPlantopiaShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome = PLANTOPIA_SHATTERED_BIOMES[temperatureIndex][humidityIndex];
		if (biome == null) biome = pickPlantopiaMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		if (biome == null) biome = pickMinecraftShatteredBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* MAYBE PICK WINDSWEPT SAVANNA BIOME ******************************************/

	private ResourceKey<Biome> maybePickMinecraftWindsweptSavannaBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> fallbackBiome) {
		return temperatureIndex > 1 && humidityIndex < 4 && weirdness.max() >= 0L ? Biomes.WINDSWEPT_SAVANNA : fallbackBiome;
	}

	private ResourceKey<Biome> maybePickPlantopiaWindsweptSavannaBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> fallbackBiome) {
		return maybePickMinecraftWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, fallbackBiome);
	}

	/* PICK PEAK BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex <= 2) biome = weirdness.max() < 0L ? Biomes.JAGGED_PEAKS : Biomes.FROZEN_PEAKS;
		else
			biome = temperatureIndex == 3 ? Biomes.STONY_PEAKS : pickMinecraftBadlandsBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	private ResourceKey<Biome> pickPlantopiaPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex <= 2) biome = null;
		else
			biome = temperatureIndex == 3 ? null : pickPlantopiaBadlandsBiome(temperatureIndex, humidityIndex, weirdness);
		if (biome == null) biome = pickMinecraftPeakBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* PICK ISLAND BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftIslandBiome(@SuppressWarnings("unused") int temperatureIndex, @SuppressWarnings("unused") int humidityIndex) {
		return Biomes.MUSHROOM_FIELDS;
	}

	private ResourceKey<Biome> pickPlantopiaIslandBiome(int temperatureIndex, int humidityIndex) {
		ResourceKey<Biome> biome = PLANTOPIA_ISLAND_BIOMES[temperatureIndex][humidityIndex];
		if (biome == null) biome = pickMinecraftIslandBiome(temperatureIndex, humidityIndex);
		return biome;
	}

	/* PICK BEACH BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftBeachBiome(int temperatureIndex, @SuppressWarnings("unused") int humidityIndex, @SuppressWarnings("unused") Climate.Parameter weirdness) {
		ResourceKey<Biome> biome;
		if (temperatureIndex == 0) biome = Biomes.SNOWY_BEACH;
		else biome = temperatureIndex == 4 ? Biomes.DESERT : Biomes.BEACH;
		return biome;
	}

	private ResourceKey<Biome> pickPlantopiaBeachBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return pickMinecraftBeachBiome(temperatureIndex, humidityIndex, weirdness);
	}

	/* PICK SHATTERED COAST BIOME ******************************************/

	private ResourceKey<Biome> pickMinecraftShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.@NotNull Parameter weirdness) {
		ResourceKey<Biome> fallbackBiome;
		if (weirdness.max() >= 0L) fallbackBiome = pickMinecraftMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		else fallbackBiome = pickMinecraftBeachBiome(temperatureIndex, humidityIndex, weirdness);
		return maybePickMinecraftWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, fallbackBiome);
	}

	private ResourceKey<Biome> pickPlantopiaShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.@NotNull Parameter weirdness) {
		ResourceKey<Biome> biome, fallbackBiome;
		if (weirdness.max() >= 0L) fallbackBiome = pickPlantopiaMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		else fallbackBiome = pickPlantopiaBeachBiome(temperatureIndex, humidityIndex, weirdness);
		biome = maybePickPlantopiaWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, fallbackBiome);
		if (biome == null) biome = pickMinecraftShatteredCoastBiome(temperatureIndex, humidityIndex, weirdness);
		return biome;
	}

	/* MAYBE PICK SWAMP BIOME ******************************************/

	private ResourceKey<Biome> maybePickMinecraftSwampBiome(@SuppressWarnings("unused") int temperatureIndex, int humidityIndex, @SuppressWarnings("unused") Climate.Parameter weirdness, ResourceKey<Biome> fallbackBiome) {
		return humidityIndex >= 3 ? Biomes.SWAMP : fallbackBiome;
	}

	private ResourceKey<Biome> maybePickPlantopiaSwampBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> fallbackBiome) {
		ResourceKey<Biome> biome = PLANTOPIA_SWAMP_BIOMES[temperatureIndex][humidityIndex];
		if (biome == null)
			biome = maybePickMinecraftSwampBiome(temperatureIndex, humidityIndex, weirdness, fallbackBiome);
		return biome;
	}

	/* ADD BIOME ******************************************/

	protected void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, @SuppressWarnings("SameParameterValue") float offset, @Nullable ResourceKey<Biome> biome) {
		if (biome == null) return;
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), biome));
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), biome));
	}

	protected void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, @SuppressWarnings("SameParameterValue") float offset, @Nullable ResourceKey<Biome> biome) {
		if (biome == null) return;
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, DEPTH_RANGE, weirdness, offset), biome));
	}
}