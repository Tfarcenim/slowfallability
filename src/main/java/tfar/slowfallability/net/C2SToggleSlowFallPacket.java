package tfar.slowfallability.net;

import net.minecraftforge.fml.network.NetworkEvent;
import tfar.slowfallability.SlowfallAbility;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SToggleSlowFallPacket {



    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            UUID uuid = contextSupplier.get().getSender().getGameProfile().getId();
            if (SlowfallAbility.active.contains(uuid)) {
                SlowfallAbility.active.remove(uuid);
            } else {
                SlowfallAbility.active.add(uuid);
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
