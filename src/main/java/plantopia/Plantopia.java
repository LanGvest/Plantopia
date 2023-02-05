package plantopia;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Plantopia.MOD_ID)
public class Plantopia {
	public static final String MOD_ID = "plantopia";

	public Plantopia() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}