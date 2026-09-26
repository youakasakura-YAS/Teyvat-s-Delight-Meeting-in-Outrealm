package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatItemData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.util.Mth;

public class TeyvatDishItem
extends Item {
    private final ChatFormatting nameColor;
    private final int defaultStars;

    public TeyvatDishItem(Item.Properties properties) {
        this(properties, null, 1);
    }

    public TeyvatDishItem(Item.Properties properties, ChatFormatting nameColor) {
        this(properties, nameColor, 1);
    }

    public TeyvatDishItem(Item.Properties properties, ChatFormatting nameColor, int defaultStars) {
        super(properties);
        this.nameColor = nameColor;
        this.defaultStars = TeyvatItemData.clampStars(defaultStars);
    }

    public Component getName(ItemStack stack) {
        int stars = TeyvatItemData.getStars(stack, this.defaultStars);
        if (stars > 0) {
            return Component.translatable((String)this.getDescriptionId(stack)).withStyle(TeyvatItemData.getStarColor(stars));
        }
        if (this.nameColor != null) {
            return Component.translatable((String)this.getDescriptionId(stack)).withStyle(this.nameColor);
        }
        return super.getName(stack);
    }

    public int getDefaultStars() {
        return this.defaultStars;
    }

    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        TeyvatItemData.appendRarityTooltip(stack, tooltip, this.defaultStars);
        TeyvatItemData.appendFoodQualityTooltip(stack, tooltip);
        TeyvatDishItem.addFoodEffectTooltip(stack, tooltip);
    }

    /**
     * 自实现"食用效果"提示（等价 NeoForge 农夫乐事 TextUtils.addFoodEffectTooltip，
     * Fabric 移植版无此工具类）。
     */
    private static void addFoodEffectTooltip(ItemStack stack, List<Component> tooltip) {
        FoodProperties food = stack.getItem().getFoodProperties();
        if (food == null || food.getEffects().isEmpty()) {
            return;
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("teyvats_delight_meeting_in_outrealm.tooltip.when_consumed").withStyle(ChatFormatting.GOLD));
        for (com.mojang.datafixers.util.Pair<MobEffectInstance, Float> possible : food.getEffects()) {
            MobEffectInstance effect = possible.getFirst();
            tooltip.add(Component.translatable(effect.getDescriptionId()).withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(" (" + TeyvatDishItem.formatDuration(effect.getDuration()) + ")").withStyle(ChatFormatting.GRAY)));
        }
    }

    private static String formatDuration(int duration) {
        int seconds = Math.max(1, Mth.ceil(duration / 20.0f));
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + ":" + String.format("%02d", remainingSeconds) : remainingSeconds + "s";
    }
}

