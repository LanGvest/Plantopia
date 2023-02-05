package plantopia.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plantopia.Plantopia;
import plantopia.item.PlantopiaItems;

import java.util.function.Supplier;

public class PlantopiaBlocks {
	public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Plantopia.MOD_ID);

	public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> supplier) {
		RegistryObject<T> blockRegistryObject = BLOCK_REGISTER.register(name, supplier);
		PlantopiaItems.registerBlockItem(name, blockRegistryObject);
		return blockRegistryObject;
	}

	public static void register(IEventBus bus) {
		BLOCK_REGISTER.register(bus);
	}
}