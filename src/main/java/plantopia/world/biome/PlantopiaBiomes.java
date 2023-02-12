package plantopia.world.biome;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import plantopia.Plantopia;
import plantopia.world.biome.overworld.PlantopiaOverworldBiomeMaker;
import plantopia.world.biome.overworld.PlantopiaOverworldRegionPrimary;
import plantopia.world.biome.overworld.PlantopiaOverworldSurfaceRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Supplier;
public class PlantopiaBiomes {
	public static final DeferredRegister<Biome> BIOME_REGISTER = DeferredRegister.create(ForgeRegistries.BIOMES, Plantopia.MOD_ID);

//	public static final ResourceKey<Biome> WILLOWHERB_VALLEY = registerBiome("willowherb_valley", PlantopiaOverworldBiomeMaker::marsh);
	public static final ResourceKey<Biome> MARSH = registerBiome("marsh", PlantopiaOverworldBiomeMaker::marsh);
	public static <T extends Biome> ResourceKey<Biome> registerBiome(String name, Supplier<T> biome) {
		ResourceKey<Biome> biomeResourceKey = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Plantopia.MOD_ID, name));
		BIOME_REGISTER.register(name, biome);
		return biomeResourceKey;
	}
	public static void setupTerraBlender() {
		Regions.register(new PlantopiaOverworldRegionPrimary(1));
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, Plantopia.MOD_ID, PlantopiaOverworldSurfaceRules.makeRules());
	}

	public static void register(IEventBus bus) {
		BIOME_REGISTER.register(bus);
	}
}