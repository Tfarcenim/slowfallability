package tfar.slowfallability.mixin;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeModifier.class)
public interface AttributeModifierAccess {
    @Accessor void setAmount(double amount);
}
