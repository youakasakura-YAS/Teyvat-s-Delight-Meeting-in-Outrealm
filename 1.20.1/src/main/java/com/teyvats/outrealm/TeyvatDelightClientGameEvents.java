package com.teyvats.outrealm;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric 移植版客户端星级背景渲染：
 * 原 NeoForge 使用 ContainerScreenEvent.Render.Background，
 * Fabric 无对应事件，改为由 AbstractContainerScreenMixin 在 renderBg 末尾调用本方法。
 */
public final class TeyvatDelightClientGameEvents {
    private static final ResourceLocation[] STAR_BACKGROUNDS = new ResourceLocation[]{
            null,
            new ResourceLocation("teyvats_delight_meeting_in_outrealm", "textures/slot/star_background_1.png"),
            new ResourceLocation("teyvats_delight_meeting_in_outrealm", "textures/slot/star_background_2.png"),
            new ResourceLocation("teyvats_delight_meeting_in_outrealm", "textures/slot/star_background_3.png"),
            new ResourceLocation("teyvats_delight_meeting_in_outrealm", "textures/slot/star_background_4.png"),
            new ResourceLocation("teyvats_delight_meeting_in_outrealm", "textures/slot/star_background_5.png")
    };

    private TeyvatDelightClientGameEvents() {
    }

    public static void renderStarBackgrounds(GuiGraphics guiGraphics, int left, int top, AbstractContainerScreen<?> screen) {
        for (Slot slot : screen.getMenu().slots) {
            ItemStack stack;
            ResourceLocation background;
            if (!slot.isActive() || !slot.hasItem() || (background = TeyvatDelightClientGameEvents.getStarBackground(TeyvatItemData.getDisplayStars(stack = slot.getItem()))) == null) {
                continue;
            }
            guiGraphics.blit(background, left + slot.x, top + slot.y, 0.0f, 0.0f, 16, 16, 16, 16);
        }
    }

    private static ResourceLocation getStarBackground(int stars) {
        return stars >= 1 && stars <= 5 ? STAR_BACKGROUNDS[stars] : null;
    }
}
