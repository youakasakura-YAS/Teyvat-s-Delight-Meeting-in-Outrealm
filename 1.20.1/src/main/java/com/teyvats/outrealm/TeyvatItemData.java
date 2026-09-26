package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatDishItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class TeyvatItemData {
    public static final int MIN_STARS = 1;
    public static final int MAX_STARS = 5;
    public static final String QUALITY_STRANGE = "strange";
    public static final String QUALITY_NORMAL = "normal";
    public static final String QUALITY_DELICIOUS = "delicious";

    private TeyvatItemData() {
    }

    public static int clampStars(int stars) {
        return Math.max(1, Math.min(5, stars));
    }

    public static int getStars(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().contains("TeyvatStars", net.minecraft.nbt.Tag.TAG_INT)
                ? TeyvatItemData.clampStars(stack.getTag().getInt("TeyvatStars")) : 0;
    }

    public static int getStars(ItemStack stack, int fallbackStars) {
        int stars = TeyvatItemData.getStars(stack);
        return stars == 0 ? TeyvatItemData.clampStars(fallbackStars) : TeyvatItemData.clampStars(stars);
    }

    public static int getDisplayStars(ItemStack stack) {
        int stars = TeyvatItemData.getStars(stack);
        if (stars > 0) {
            return stars;
        }
        Item item = stack.getItem();
        if (item instanceof TeyvatDishItem) {
            TeyvatDishItem dishItem = (TeyvatDishItem)item;
            return dishItem.getDefaultStars();
        }
        return 0;
    }

    public static void setStars(ItemStack stack, int stars) {
        stack.getOrCreateTag().putInt("TeyvatStars", TeyvatItemData.clampStars(stars));
    }

    public static String getFoodQuality(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().contains("TeyvatQuality", net.minecraft.nbt.Tag.TAG_STRING)
                ? TeyvatItemData.normalizeQuality(stack.getTag().getString("TeyvatQuality")) : QUALITY_NORMAL;
    }

    public static void setFoodQuality(ItemStack stack, String quality) {
        stack.getOrCreateTag().putString("TeyvatQuality", TeyvatItemData.normalizeQuality(quality));
    }

    public static String normalizeQuality(String quality) {
        if (QUALITY_STRANGE.equals(quality) || QUALITY_DELICIOUS.equals(quality) || QUALITY_NORMAL.equals(quality)) {
            return quality;
        }
        return QUALITY_NORMAL;
    }

    public static void appendRarityTooltip(ItemStack stack, List<Component> tooltip) {
        TeyvatItemData.appendRarityTooltip(TeyvatItemData.getStars(stack), tooltip);
    }

    public static void appendRarityTooltip(ItemStack stack, List<Component> tooltip, int fallbackStars) {
        TeyvatItemData.appendRarityTooltip(TeyvatItemData.getStars(stack, fallbackStars), tooltip);
    }

    private static void appendRarityTooltip(int stars, List<Component> tooltip) {
        if (stars > 0) {
            tooltip.add((Component)Component.translatable((String)"tooltip.teyvats_delight_meeting_in_outrealm.stars", (Object[])new Object[]{TeyvatItemData.buildStars(stars)}).withStyle(TeyvatItemData.getStarColor(stars)));
        }
    }

    public static void appendFoodQualityTooltip(ItemStack stack, List<Component> tooltip) {
        String quality = TeyvatItemData.getFoodQuality(stack);
        if (!QUALITY_NORMAL.equals(quality)) {
            tooltip.add((Component)Component.translatable((String)("tooltip.teyvats_delight_meeting_in_outrealm.food_quality." + TeyvatItemData.normalizeQuality(quality))));
        }
    }

    public static String buildStars(int stars) {
        return "\u2605".repeat(TeyvatItemData.clampStars(stars));
    }

    public static ChatFormatting getStarColor(int stars) {
        return switch (TeyvatItemData.clampStars(stars)) {
            case 5 -> ChatFormatting.GOLD;
            case 4 -> ChatFormatting.LIGHT_PURPLE;
            case 3 -> ChatFormatting.BLUE;
            case 2 -> ChatFormatting.GREEN;
            default -> ChatFormatting.WHITE;
        };
    }
}

