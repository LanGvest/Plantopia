package plantopia.world.biome.overworld;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import plantopia.world.biome.PlantopiaBiomes;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PlantopiaOverworldBiomeBuilder {
	private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);

	private final Climate.Parameter[] temperatures = new Climate.Parameter[] {
		Climate.Parameter.span(-1.0F, -0.45F),
		Climate.Parameter.span(-0.45F, -0.15F),
		Climate.Parameter.span(-0.15F, 0.2F),
		Climate.Parameter.span(0.2F, 0.55F),
		Climate.Parameter.span(0.55F, 1.0F)
	};

	private final Climate.Parameter[] humidities = new Climate.Parameter[] {
		Climate.Parameter.span(-1.0F, -0.35F),
		Climate.Parameter.span(-0.35F, -0.1F),
		Climate.Parameter.span(-0.1F, 0.1F),
		Climate.Parameter.span(0.1F, 0.3F),
		Climate.Parameter.span(0.3F, 1.0F)
	};

	private final Climate.Parameter[] erosions = new Climate.Parameter[] {
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
	private final Climate.Parameter mushroomFieldsContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
	private final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
	private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
	private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
	private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
	private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
	private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
	private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][] {
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][] {
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][] {
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][] {
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] SHATTERED_BIOMES = new ResourceKey[][] {
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null},
		{null, null, null, null, null}
	};

	public void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addOffCoastBiomes(mapper);
		addInlandBiomes(mapper);
		addUndergroundBiomes(mapper);
	}

	private void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {}

	@SuppressWarnings("GrazieInspection")
	private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        /*
	        Weirdness ranges map to specific slices in a repeating triangle wave fashion:

	               PEAKS                           PEAKS
	           HIGH     HIGH                   HIGH     HIGH
	        MID             MID             MID             MID
	                           LOW       LOW
	                              VALLEYS
		*/

		/* FIRST CYCLE */
		addMidSlice(mapper, Climate.Parameter.span(-1.0F, -0.93333334F));
		addHighSlice(mapper, Climate.Parameter.span(-0.93333334F, -0.7666667F));
		addPeaks(mapper, Climate.Parameter.span(-0.7666667F, -0.56666666F));
		addHighSlice(mapper, Climate.Parameter.span(-0.56666666F, -0.4F));
		addMidSlice(mapper, Climate.Parameter.span(-0.4F, -0.26666668F));
		addLowSlice(mapper, Climate.Parameter.span(-0.26666668F, -0.05F));
		addValleys(mapper, Climate.Parameter.span(-0.05F, 0.05F));
		addLowSlice(mapper, Climate.Parameter.span(0.05F, 0.26666668F));
		addMidSlice(mapper, Climate.Parameter.span(0.26666668F, 0.4F));

		/* TRUNCATED SECOND CYCLE */
		addHighSlice(mapper, Climate.Parameter.span(0.4F, 0.56666666F));
		addPeaks(mapper, Climate.Parameter.span(0.56666666F, 0.7666667F));
		addHighSlice(mapper, Climate.Parameter.span(0.7666667F, 0.93333334F));
		addMidSlice(mapper, Climate.Parameter.span(0.93333334F, 1.0F));
	}

	@SuppressWarnings("DuplicatedCode")
	private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		for(int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for(int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> plateauBiome = pickPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = pickShatteredBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickWindsweptSavannaBiome(i, j, weirdness, shatteredBiome);
				ResourceKey<Biome> peakBiome = pickPeakBiome(i, j, weirdness);
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
		for(int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for(int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> biomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> plateauBiome = pickPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = pickShatteredBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickWindsweptSavannaBiome(i, j, weirdness, middleBiome);
				ResourceKey<Biome> slopeBiome = pickSlopeBiome(i, j, weirdness);
				ResourceKey<Biome> peakBiome = pickPeakBiome(i, j, weirdness);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[0], weirdness, 0.0F, slopeBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0F, peakBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[1], weirdness, 0.0F, slopeBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[2], weirdness, 0.0F, plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, midInlandContinentalness, erosions[3], weirdness, 0.0F, biomeOrBadlandsIfHot);
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
		addSurfaceBiome(mapper, UNFROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, PlantopiaBiomes.MARSH);
		for(int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for(int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> biomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> shatteredBiome = pickShatteredBiome(i, j, weirdness);
				ResourceKey<Biome> plateauBiome = pickPlateauBiome(i, j, weirdness);
				ResourceKey<Biome> beachBiome = pickBeachBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickWindsweptSavannaBiome(i, j, weirdness, middleBiome);
				ResourceKey<Biome> shatteredCoastBiome = pickShatteredCoastBiome(i, j, weirdness);
				ResourceKey<Biome> slopeBiome = pickSlopeBiome(i, j, weirdness);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[0], weirdness, 0.0F, slopeBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, midInlandContinentalness), erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
				addSurfaceBiome(mapper, temperature, humidity, farInlandContinentalness, erosions[1], weirdness, 0.0F, i == 0 ? slopeBiome : plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[2], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, midInlandContinentalness, erosions[2], weirdness, 0.0F, biomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, farInlandContinentalness, erosions[2], weirdness, 0.0F, plateauBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, nearInlandContinentalness), erosions[3], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[3], weirdness, 0.0F, biomeOrBadlandsIfHot);
				if(weirdness.max() < 0F) {
					addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[4], weirdness, 0.0F, beachBiome);
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				} else {
					addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				}
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[5], weirdness, 0.0F, shatteredCoastBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[5], weirdness, 0.0F, windsweptSavannaBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0F, shatteredBiome);
				if(weirdness.max() < 0F) {
					addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0F, beachBiome);
				} else {
					addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0F, middleBiome);
				}
				if(i == 0) addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, middleBiome);
			}
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		addSurfaceBiome(mapper, UNFROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, PlantopiaBiomes.MARSH);
		for(int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for(int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> middleBiome = pickMiddleBiome(i, j, weirdness);
				ResourceKey<Biome> biomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdness);
				ResourceKey<Biome> beachBiome = pickBeachBiome(i, j, weirdness);
				ResourceKey<Biome> windsweptSavannaBiome = maybePickWindsweptSavannaBiome(i, j, weirdness, middleBiome);
				ResourceKey<Biome> shatteredCoastBiome = pickShatteredCoastBiome(i, j, weirdness);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, biomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[3]), weirdness, 0.0F, biomeOrBadlandsIfHot);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, Climate.Parameter.span(erosions[3], erosions[4]), weirdness, 0.0F, beachBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[4], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[5], weirdness, 0.0F, shatteredCoastBiome);
				addSurfaceBiome(mapper, temperature, humidity, nearInlandContinentalness, erosions[5], weirdness, 0.0F, windsweptSavannaBiome);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), erosions[5], weirdness, 0.0F, middleBiome);
				addSurfaceBiome(mapper, temperature, humidity, coastContinentalness, erosions[6], weirdness, 0.0F, beachBiome);
				if(i == 0) addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(nearInlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, middleBiome);
			}
		}
	}

	private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
		addSurfaceBiome(mapper, UNFROZEN_RANGE, FULL_RANGE, Climate.Parameter.span(inlandContinentalness, farInlandContinentalness), erosions[6], weirdness, 0.0F, PlantopiaBiomes.MARSH);
		for(int i = 0; i < temperatures.length; i++) {
			Climate.Parameter temperature = temperatures[i];
			for(int j = 0; j < humidities.length; j++) {
				Climate.Parameter humidity = humidities[j];
				ResourceKey<Biome> biomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(i, j, weirdness);
				addSurfaceBiome(mapper, temperature, humidity, Climate.Parameter.span(midInlandContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0.0F, biomeOrBadlandsIfHot);
			}
		}
	}

	private ResourceKey<Biome> pickMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return pickVariativeBiome(temperatureIndex, humidityIndex, weirdness, MIDDLE_BIOMES, MIDDLE_BIOMES_VARIANT);
	}

	private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHot(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		if(temperatureIndex == 4) return pickBadlandsBiome(temperatureIndex, humidityIndex, weirdness);
		return pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
	}

	private ResourceKey<Biome> pickBadlandsBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return null;
	}

	private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		if(temperatureIndex == 0) return pickSlopeBiome(temperatureIndex, humidityIndex, weirdness);
		return pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
	}

	private ResourceKey<Biome> pickSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		if(temperatureIndex >= 3) return pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
		return null;
	}

	private ResourceKey<Biome> pickPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return pickVariativeBiome(temperatureIndex, humidityIndex, weirdness, PLATEAU_BIOMES, PLATEAU_BIOMES_VARIANT);
	}

	private ResourceKey<Biome> pickVariativeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome>[][] normal, ResourceKey<Biome>[][] variant) {
		ResourceKey<Biome> biome = normal[temperatureIndex][humidityIndex];
		if(weirdness.max() < 0F) return biome;
		ResourceKey<Biome> biomeVariant = variant[temperatureIndex][humidityIndex];
		if(biomeVariant == null) return biome;
		return biomeVariant;
	}

	private ResourceKey<Biome> pickShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> shatteredBiome = SHATTERED_BIOMES[temperatureIndex][humidityIndex];
		if(shatteredBiome == null) return pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
		return shatteredBiome;
	}

	private ResourceKey<Biome> maybePickWindsweptSavannaBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> biome) {
		if(temperatureIndex > 1 && humidityIndex < 4 && weirdness.max() >= 0F) return null;
		return biome;
	}

	private ResourceKey<Biome> pickPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		if(temperatureIndex <= 2) return null;
		return temperatureIndex == 3 ? null : pickBadlandsBiome(temperatureIndex, humidityIndex, weirdness);
	}

	private ResourceKey<Biome> pickBeachBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		return null;
	}

	private ResourceKey<Biome> pickShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
		ResourceKey<Biome> biome = weirdness.max() >= 0F ? pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : pickBeachBiome(temperatureIndex, humidityIndex, weirdness);
		return maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, biome);
	}

	private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {}

	protected static void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, @SuppressWarnings("SameParameterValue") float offset, @Nullable ResourceKey<Biome> biome) {
		if(biome == null) return;
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), biome));
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), biome));
	}

	protected void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, Climate.Parameter depth, float offset, @Nullable ResourceKey<Biome> biome) {
		if(biome == null) return;
		mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, depth, weirdness, offset), biome));
	}
}