package tfar.slowfallability.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.extensions.IForgeEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

@Mixin(DisplayEffectsScreen.class)
public abstract class DisplayEffectsScreenMixin extends ContainerScreen {

    @Shadow protected abstract void renderEffectBackground(MatrixStack matrixStack, int renderX, int yOffset, Iterable<EffectInstance> effects);

    @Shadow protected abstract void renderEffectSprites(MatrixStack matrixStack, int renderX, int yOffset, Iterable<EffectInstance> effects);

    @Shadow protected abstract void renderEffectText(MatrixStack matrixStack, int renderX, int yOffset, Iterable<EffectInstance> effects);

    public DisplayEffectsScreenMixin(Container screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    /**
     * @author tfar
     */
    @Overwrite
    private void renderEffects(MatrixStack matrixStack) {
        int i = this.guiLeft - 124;
        Collection<EffectInstance> collection = this.minecraft.player.getActivePotionEffects();
        if (!collection.isEmpty()) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = 33;
            if (collection.size() > 5) {
                j = 132 / (collection.size() - 1);
            }
            //not very nice but it gets the job done
            Iterable<EffectInstance> iterable = collection.stream().filter(IForgeEffectInstance::shouldRender).filter(EffectInstance::isShowIcon).sorted().collect(java.util.stream.Collectors.toList());
            this.renderEffectBackground(matrixStack, i, j, iterable);
            this.renderEffectSprites(matrixStack, i, j, iterable);
            this.renderEffectText(matrixStack, i, j, iterable);
        }
    }
}
