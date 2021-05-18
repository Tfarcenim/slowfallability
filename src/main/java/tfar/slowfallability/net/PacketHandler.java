package tfar.slowfallability.net;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import tfar.slowfallability.SlowfallAbility;

public class PacketHandler {
  public static SimpleChannel INSTANCE;

  public static void registerMessages() {
    int id = 0;

    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SlowfallAbility.MODID, SlowfallAbility.MODID), () -> "1.0", s -> true, s -> true);

    INSTANCE.registerMessage(id++, C2SToggleSlowFallPacket.class,
            (message, buffer) -> {},
            buffer -> new C2SToggleSlowFallPacket(),
            C2SToggleSlowFallPacket::handle);

  }
}
