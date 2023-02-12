package plantopia.world.biome.overworld;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class PlantopiaOverworldSurfaceRules {
	private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
	private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);

	private static SurfaceRules.RuleSource makeStateRule(Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	public static SurfaceRules.RuleSource makeRules() {
		return SurfaceRules.sequence();
	}
}