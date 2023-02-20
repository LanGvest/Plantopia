package plantopia.world.biome;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import plantopia.Plantopia;
import plantopia.world.biome.overworld.PlantopiaOverworldBiomeMaker;
import plantopia.world.biome.overworld.PlantopiaOverworldBiomeRegion;
import plantopia.world.biome.overworld.PlantopiaOverworldSurfaceRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Supplier;

public class PlantopiaBiomes {
	public static final DeferredRegister<Biome> BIOME_REGISTER = DeferredRegister.create(ForgeRegistries.BIOMES, Plantopia.MOD_ID);

//	public static final ResourceKey<Biome> WILLOWHERB_VALLEY = registerBiome("willowherb_valley", PlantopiaOverworldBiomeMaker::marsh);
	public static final ResourceKey<Biome> MARSH = registerBiome("marsh", VillagerType.SWAMP, PlantopiaOverworldBiomeMaker::marsh);
	public static <T extends Biome> @NotNull ResourceKey<Biome> registerBiome(String name, @SuppressWarnings("unused") VillagerType villagerType, Supplier<T> biome) {
		ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Plantopia.MOD_ID, name));
		BIOME_REGISTER.register(name, biome);
		return key;
	}
	public static void setupTerraBlender() {
		Regions.register(new PlantopiaOverworldBiomeRegion.Regular());
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, Plantopia.MOD_ID, PlantopiaOverworldSurfaceRules.makeRules());
	}

	public static void register(IEventBus bus) {
		BIOME_REGISTER.register(bus);
	}
}