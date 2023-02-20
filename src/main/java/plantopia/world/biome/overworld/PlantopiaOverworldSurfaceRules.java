package plantopia.world.biome.overworld;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import plantopia.world.biome.PlantopiaBiomes;
import plantopia.world.noise.PlantopiaNoises;

public class PlantopiaOverworldSurfaceRules {
	private static final SurfaceRules.ConditionSource IS_ABOVE_62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
	private static final SurfaceRules.ConditionSource IS_ABOVE_63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
	private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);

	@Contract("_ -> new")
	@SuppressWarnings("SameParameterValue")
	private static SurfaceRules.@NotNull RuleSource makeStateRule(@NotNull Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	@Contract(" -> new")
	public static SurfaceRules.@NotNull RuleSource makeRules() {
		return SurfaceRules.sequence(
			SurfaceRules.ifTrue(
				SurfaceRules.ON_FLOOR,
				SurfaceRules.sequence(
					SurfaceRules.ifTrue(
						// Swamp water noise
						SurfaceRules.isBiome(PlantopiaBiomes.MARSH),
						SurfaceRules.ifTrue(
							IS_ABOVE_62,
							SurfaceRules.ifTrue(
								SurfaceRules.not(IS_ABOVE_63),
								SurfaceRules.ifTrue(
									SurfaceRules.noiseCondition(PlantopiaNoises.WEIGHTED, 0.0D),
									WATER
								)
							)
						)
					)
				)
			)
		);
	}
}