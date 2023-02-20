package plantopia.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import plantopia.Plantopia;
import plantopia.block.special.PlantopiaWaterloggedDoublePlantBlock;
import plantopia.item.PlantopiaItems;

import java.util.function.Supplier;

public class PlantopiaBlocks {
	public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Plantopia.MOD_ID);

	public static final RegistryObject<TallFlowerBlock> WILLOWHERB = registerBlock("willowherb", () -> new TallFlowerBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
	public static final RegistryObject<PlantopiaWaterloggedDoublePlantBlock> WATERGRASS = registerBlock("watergrass", () -> new PlantopiaWaterloggedDoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS)));

	public static <T extends Block> @NotNull RegistryObject<T> registerBlock(String name, Supplier<T> supplier) {
		RegistryObject<T> blockRegistryObject = BLOCK_REGISTER.register(name, supplier);
		PlantopiaItems.registerBlockItem(name, blockRegistryObject);
		return blockRegistryObject;
	}

	public static void register(IEventBus bus) {
		BLOCK_REGISTER.register(bus);
	}
}