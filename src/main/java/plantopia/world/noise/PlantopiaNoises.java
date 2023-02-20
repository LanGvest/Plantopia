package plantopia.world.noise;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import plantopia.Plantopia;

import java.util.function.Supplier;

public class PlantopiaNoises {
	public static final DeferredRegister<NormalNoise.NoiseParameters> NOISE_REGISTER = DeferredRegister.create(Registry.NOISE_REGISTRY, Plantopia.MOD_ID);

	public static final ResourceKey<NormalNoise.NoiseParameters> WEIGHTED = registerNoise("weighted", () -> new NormalNoise.NoiseParameters(0, 1.0));

	public static <T extends NormalNoise.NoiseParameters> @NotNull ResourceKey<NormalNoise.NoiseParameters> registerNoise(String name, Supplier<T> noise) {
		ResourceKey<NormalNoise.NoiseParameters> key = ResourceKey.create(Registry.NOISE_REGISTRY, new ResourceLocation(Plantopia.MOD_ID, name));
		NOISE_REGISTER.register(name, noise);
		return key;
	}

	public static void register(IEventBus bus) {
		NOISE_REGISTER.register(bus);
	}
}