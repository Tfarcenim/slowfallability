package tfar.slowfallability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import tfar.slowfallability.net.C2SToggleSlowFallPacket;
import tfar.slowfallability.net.PacketHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SlowfallAbility.MODID)
public class SlowfallAbility {
    // Directly reference a log4j logger.

    public static final String MODID = "slowfallability";

    public static Set<UUID> active = new HashSet<>();

    static ForgeConfigSpec.DoubleValue fall_modifier;

    public SlowfallAbility() {

        final Pair<SlowfallAbility, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(this::spec);

        ForgeConfigSpec serverSpec = specPair.getRight();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverSpec);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigStuff::config);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::common);
        MinecraftForge.EVENT_BUS.addListener(this::playerTick);
        if (FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::client);
            MinecraftForge.EVENT_BUS.addListener(Client::key);
            MinecraftForge.EVENT_BUS.addListener(Client::potionRender);
            MinecraftForge.EVENT_BUS.addListener(Client::potionShift);
        }
    }

    private void common(FMLCommonSetupEvent e) {
        PacketHandler.registerMessages();
    }

    private void playerTick(TickEvent.PlayerTickEvent e) {
        UUID uuid = e.player.getGameProfile().getId();
        if (active.contains(uuid) && !e.player.isOnGround()) {
            e.player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING,20,0,false,false,false,null));
        }
    }

    private SlowfallAbility spec(ForgeConfigSpec.Builder builder) {
        builder.push("general");
        fall_modifier = builder.defineInRange("trophy_count", -.07, -100000, 100000);
        return null;
    }

    public static class Client {
        public static KeyBinding BUTTON = new KeyBinding("toggle", GLFW.GLFW_KEY_Y,MODID);

        public static void client(FMLClientSetupEvent e) {
            ClientRegistry.registerKeyBinding(BUTTON);
        }

        public static void potionRender(RenderGameOverlayEvent e) {
            if (e.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
                e.setCanceled(true);
            }
        }

        public static void potionShift(GuiScreenEvent.PotionShiftEvent e) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                Collection<EffectInstance> effects = player.getActivePotionEffects();
                //only has one effect active and it's named slowfall
                if (effects.size() == 1 && player.isPotionActive(Effects.SLOW_FALLING)) {
                    e.setCanceled(true);
                }
            }
        }

        public static void key(InputEvent.KeyInputEvent e) {
            while (BUTTON.isPressed()) {
                PacketHandler.INSTANCE.sendToServer(new C2SToggleSlowFallPacket());
            }
        }
    }
}
