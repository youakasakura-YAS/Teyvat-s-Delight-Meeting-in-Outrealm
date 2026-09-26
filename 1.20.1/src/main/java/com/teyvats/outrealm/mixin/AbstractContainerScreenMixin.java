package com.teyvats.outrealm.mixin;

import com.teyvats.outrealm.TeyvatDelightClientGameEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 在容器界面背景渲染完成后绘制菜品星级背景。
 * 替代 NeoForge 的 ContainerScreenEvent.Render.Background。
 *
 * 注意：不能注入 renderBg —— 它是 AbstractContainerScreen 的抽象方法（无方法体），
 * mixin 的 @At("RETURN") 无法命中，会报 "Scanned 0 target(s)" 并崩溃。
 * renderLabels 是具体方法，且在 render() 中紧跟在 renderBg 之后调用，
 * 其 RETURN 时机 = 背景已画完、物品未画，与原事件语义一致。
 */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;

    @Inject(method = "renderLabels", at = @At("RETURN"))
    private void teyvats_delight_meeting_in_outrealm$renderStarBackgrounds(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
        TeyvatDelightClientGameEvents.renderStarBackgrounds(guiGraphics, this.leftPos, this.topPos, screen);
    }
}
