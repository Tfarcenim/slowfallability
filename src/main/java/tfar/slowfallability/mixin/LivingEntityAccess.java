package tfar.slowfallability.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccess {
	@Accessor
    static AttributeModifier getSLOW_FALLING() {
	    throw new RuntimeException("LivingEntityAccess");
  }
}
