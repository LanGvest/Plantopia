package plantopia;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import plantopia.block.PlantopiaBlocks;
import plantopia.item.PlantopiaItems;
import plantopia.world.biome.PlantopiaBiomes;
import plantopia.world.noise.PlantopiaNoises;

@Mod(Plantopia.MOD_ID)
public class Plantopia {
	public static final String MOD_ID = "plantopia";

	@SuppressWarnings("unused")
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public Plantopia() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);

		PlantopiaBlocks.register(bus);
		PlantopiaItems.register(bus);
		PlantopiaNoises.register(bus);
		PlantopiaBiomes.register(bus);
	}

	private void commonSetup(final @NotNull FMLCommonSetupEvent event) {
		event.enqueueWork(PlantopiaBiomes::setupTerraBlender);
	}

	private void clientSetup(final @NotNull FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(PlantopiaBlocks.WILLOWHERB.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(PlantopiaBlocks.WATERGRASS.get(), RenderType.cutout());
		});
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		BlockColors blockColors = Minecraft.getInstance().getBlockColors();

		blockColors.register(
			(state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.get(0.5D, 1.0D),
			PlantopiaBlocks.WATERGRASS.get()
		);
	}
}