package com.teyvats.outrealm;

import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.GrassColor;

/**
 * Fabric 移植版客户端入口：
 * - RegisterColorHandlersEvent.Block/Item -> ColorProviderRegistry（fabric-rendering-v1）
 * - RegisterRenderers / RegisterLayerDefinitions -> EntityRendererRegistry / EntityModelLayerRegistry
 * - ItemTooltipEvent -> ItemTooltipCallback（fabric-item-api-v1，仅客户端）
 */
public class TeyvatDelightClient implements ClientModInitializer {
    private static final int DEFAULT_WATER_COLOR = 4159204;

    @Override
    public void onInitializeClient() {
        // 方块染色：玄磁/拟磁田 -> 草地色，诸磁竹田 -> 水色
        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) {
                return -1;
            }
            return level != null && pos != null ? BiomeColors.getAverageGrassColor((BlockAndTintGetter) level, (BlockPos) pos) : GrassColor.getDefaultColor();
        }, TeyvatDelight.XUAN_CI_JADE_FIELD.get(), TeyvatDelight.NI_CI_ZHI_FIELD.get());
        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 1) {
                return -1;
            }
            return level != null && pos != null ? BiomeColors.getAverageWaterColor((BlockAndTintGetter) level, (BlockPos) pos) : DEFAULT_WATER_COLOR;
        }, TeyvatDelight.CHU_CI_ZHU_FIELD.get());

        // 物品染色：诸磁竹田物品 -> 水色
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 1 ? DEFAULT_WATER_COLOR : -1,
                TeyvatDelight.CHU_CI_ZHU_FIELD_ITEM.get());

        // 星螺实体渲染器与模型层
        EntityRendererRegistry.register(TeyvatDelight.STARCONCH.get(), StarconchRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(StarconchModel.LAYER_LOCATION, StarconchModel::createBodyLayer);

        // 透明方块渲染层（1.21.1 的 render_type JSON 已废弃，必须代码注册；否则默认 SOLID，透明像素渲染成黑块=贴图崩坏）
        cutout(
            TeyvatDelight.SMALL_LAMP_GRASS_CROP.get(), TeyvatDelight.WINDWHEEL_ASTER_CROP.get(),
            TeyvatDelight.CALLA_LILY_CROP.get(), TeyvatDelight.MINT_CROP.get(),
            TeyvatDelight.JUEYUN_CHILI_CROP.get(), TeyvatDelight.GLAZE_LILY_CROP.get(),
            TeyvatDelight.MUFENG_MUSHROOM_CROP.get(), TeyvatDelight.SUMERU_ROSE_CROP.get(),
            TeyvatDelight.GRAINFRUIT_CROP.get(), TeyvatDelight.FLUORESCENT_FUNGUS_CROP.get(),
            TeyvatDelight.SEA_GANODERMA_CROP.get(), TeyvatDelight.DENDROBIUM_CROP.get(),
            TeyvatDelight.FROSTLAMP_FLOWER_CROP.get(), TeyvatDelight.GROWING_CORAL_SHELL.get(),
            TeyvatDelight.PEARL_BEARING_CORAL_SHELL.get(), TeyvatDelight.NATURAL_CORAL_PEARL.get(),
            TeyvatDelight.HORSETAIL_BOTTOM.get(), TeyvatDelight.HORSETAIL_TOP.get(),
            TeyvatDelight.THIRSTING_JINXIN_FLOWER.get(), TeyvatDelight.BLAZING_JINXIN_FLOWER.get(),
            TeyvatDelight.BURNT_OUT_JINXIN_FLOWER.get(), TeyvatDelight.YUNYAN_LIEYE_CROP.get(),
            TeyvatDelight.BURIED_SHIPO_FRAGMENT.get(), TeyvatDelight.REPAIRING_SHIPO.get(), TeyvatDelight.INTACT_SHIPO.get(),
            TeyvatDelight.BURIED_JINGHUAGUSUI_FRAGMENT.get(), TeyvatDelight.REPAIRING_JINGHUAGUSUI.get(), TeyvatDelight.INTACT_JINGHUAGUSUI.get(),
            TeyvatDelight.BURIED_YEBOSHI_FRAGMENT.get(), TeyvatDelight.REPAIRING_YEBOSHI.get(), TeyvatDelight.INTACT_YEBOSHI.get(),
            TeyvatDelight.NATURAL_SHIPO.get(), TeyvatDelight.NATURAL_YEBOSHI.get(),
            TeyvatDelight.NATURAL_DEEPSLATE_YEBOSHI.get(), TeyvatDelight.NATURAL_JINGHUAGUSUI.get(),
            TeyvatDelight.WILD_SMALL_LAMP_GRASS.get(), TeyvatDelight.WILD_WINDWHEEL_ASTER.get(),
            TeyvatDelight.WILD_CALLA_LILY.get(), TeyvatDelight.WILD_MINT.get(),
            TeyvatDelight.WILD_JUEYUN_CHILI.get(), TeyvatDelight.WILD_GLAZE_LILY.get(),
            TeyvatDelight.WILD_MUFENG_MUSHROOM.get(), TeyvatDelight.WILD_SUMERU_ROSE.get(),
            TeyvatDelight.WILD_GRAINFRUIT.get(), TeyvatDelight.WILD_HORSETAIL.get(),
            TeyvatDelight.WILD_JINXIN_FLOWER.get(), TeyvatDelight.WILD_FLUORESCENT_FUNGUS.get(),
            TeyvatDelight.WILD_SEA_GANODERMA.get(), TeyvatDelight.WILD_DENDROBIUM.get(),
            TeyvatDelight.WILD_FROSTLAMP_FLOWER.get(), TeyvatDelight.WILD_YUNYAN_LIEYE.get(),
            TeyvatDelight.XUAN_CI_JADE_FIELD.get(), TeyvatDelight.NI_CI_ZHI_FIELD.get(),
            TeyvatDelight.CHU_CI_ZHU_FIELD.get());

        // 物品描述提示（Shift）
        ItemTooltipCallback.EVENT.register(TeyvatDelightClient::addItemDescription);
    }

    private static void cutout(net.minecraft.world.level.block.Block... blocks) {
        for (net.minecraft.world.level.block.Block b : blocks) {
            BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.cutout());
        }
    }

    private static void addItemDescription(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> tooltip) {
        String key = stack.getDescriptionId() + ".desc";
        if (!Language.getInstance().has(key)) {
            return;
        }
        if (!Screen.hasShiftDown()) {
            tooltip.add(1, Component.translatable("tooltip.teyvats_delight_meeting_in_outrealm.hold_shift_for_description").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }
        String desc = Language.getInstance().getOrDefault(key);
        int insertIndex = 1;
        for (String line : desc.split("\\n")) {
            tooltip.add(insertIndex++, Component.literal(line).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
