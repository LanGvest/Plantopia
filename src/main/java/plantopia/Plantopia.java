package plantopia;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import plantopia.block.PlantopiaBlocks;
import plantopia.item.PlantopiaItems;

@Mod(Plantopia.MOD_ID)
public class Plantopia {
	public static final String MOD_ID = "plantopia";

	public Plantopia() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);

		PlantopiaItems.register(bus);
		PlantopiaBlocks.register(bus);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {

	}

	private void clientSetup(final FMLClientSetupEvent event) {

	}

	private void loadComplete(final FMLLoadCompleteEvent event) {

	}
}