package com.teyvats.outrealm;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class TeyvatTags {
    private TeyvatTags() {
    }

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create((ResourceKey)Registries.BLOCK, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"teyvats_delight_meeting_in_outrealm", (String)name));
    }

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"teyvats_delight_meeting_in_outrealm", (String)name));
    }

    public static final class Items {
        public static final TagKey<Item> TEYVAT_CROP_ITEMS = TeyvatTags.itemTag("teyvat_crop_items");
        public static final TagKey<Item> TEYVAT_MINERAL_ITEMS = TeyvatTags.itemTag("teyvat_mineral_items");
        public static final TagKey<Item> TEYVAT_DISHES = TeyvatTags.itemTag("teyvat_dishes");
    }

    public static final class Blocks {
        public static final TagKey<Block> CROPS = TeyvatTags.blockTag("crops");
        public static final TagKey<Block> TEYVAT_FIELDS = TeyvatTags.blockTag("teyvat_fields");
        public static final TagKey<Block> XUAN_CI_JADE_FIELDS = TeyvatTags.blockTag("xuan_ci_jade_fields");
        public static final TagKey<Block> NI_CI_ZHI_FIELDS = TeyvatTags.blockTag("ni_ci_zhi_fields");
        public static final TagKey<Block> CHU_CI_ZHU_FIELDS = TeyvatTags.blockTag("chu_ci_zhu_fields");
        public static final TagKey<Block> XUAN_CI_PU_FIELDS = TeyvatTags.blockTag("xuan_ci_pu_fields");
        public static final TagKey<Block> TEYVAT_MINERAL_BLOCKS = TeyvatTags.blockTag("teyvat_mineral_blocks");
        public static final TagKey<Block> NATURAL_SHIPO_SURFACES = TeyvatTags.blockTag("natural_shipo_surfaces");
        public static final TagKey<Block> NATURAL_NOCTILUCOUS_JADE_SURFACES = TeyvatTags.blockTag("natural_noctilucous_jade_surfaces");
        public static final TagKey<Block> WILD_GRASS_OR_DIRT_CROP_SURFACES = TeyvatTags.blockTag("wild_grass_or_dirt_crop_surfaces");
        public static final TagKey<Block> WILD_ROCKY_CROP_SURFACES = TeyvatTags.blockTag("wild_rocky_crop_surfaces");
        public static final TagKey<Block> WILD_GRASS_DIRT_OR_MUD_CROP_SURFACES = TeyvatTags.blockTag("wild_grass_dirt_or_mud_crop_surfaces");
        public static final TagKey<Block> WILD_FLUORESCENT_FUNGUS_SURFACES = TeyvatTags.blockTag("wild_fluorescent_fungus_surfaces");
        public static final TagKey<Block> WILD_DENDROBIUM_SURFACES = TeyvatTags.blockTag("wild_dendrobium_surfaces");
        public static final TagKey<Block> WILD_FROSTLAMP_SURFACES = TeyvatTags.blockTag("wild_frostlamp_surfaces");
        public static final TagKey<Block> WILD_CORAL_PEARL_SURFACES = TeyvatTags.blockTag("wild_coral_pearl_surfaces");
        public static final TagKey<Block> WILD_YUNYAN_CRACKLEAF_SURFACES = TeyvatTags.blockTag("wild_yunyan_crackleaf_surfaces");
    }
}

