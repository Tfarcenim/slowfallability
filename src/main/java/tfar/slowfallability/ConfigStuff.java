package tfar.slowfallability;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.fml.config.ModConfig;
import tfar.slowfallability.mixin.AttributeModifierAccess;
import tfar.slowfallability.mixin.LivingEntityAccess;

public class ConfigStuff {
    public static void config(final ModConfig.ModConfigEvent event) {
        if (event.getConfig().getModId().equals(SlowfallAbility.MODID)) {
            AttributeModifier fallModifier = LivingEntityAccess.getSLOW_FALLING();
            ((AttributeModifierAccess)fallModifier).setAmount(SlowfallAbility.fall_modifier.get());
        }
    }
}
