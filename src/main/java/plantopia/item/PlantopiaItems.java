package plantopia.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plantopia.Plantopia;

public class PlantopiaItems {
	private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Plantopia.MOD_ID);

	public static <T extends Block> void registerBlockItem(String name, RegistryObject<T> blockRegistryobject) {
		ITEM_REGISTER.register(name, () -> new BlockItem(blockRegistryobject.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	}

	public static void register(IEventBus bus) {
		ITEM_REGISTER.register(bus);
	}
}