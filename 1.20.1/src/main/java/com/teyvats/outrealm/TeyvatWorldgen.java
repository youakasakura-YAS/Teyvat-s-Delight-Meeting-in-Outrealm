package com.teyvats.outrealm;

import java.util.Set;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.GenerationStep;

/**
 * 世界生成挂载（Fabric 版）。
 * 原 NeoForge 的 data/teyvats_delight_meeting_in_outrealm/neoforge/biome_modifier/*.json 在 Fabric 不生效，
 * 改为使用 Fabric API 的 BiomeModifications 在运行期把 placed_feature 挂到对应生物群系。
 */
public final class TeyvatWorldgen {

    private TeyvatWorldgen() {
    }

    public static void registerAll() {
        // ============ 野生植物（vegetal_decoration） ============
        addFeature(Set.of("minecraft:beach", "minecraft:stony_shore"), "patch_natural_coral_pearl");
        addFeature(Set.of("minecraft:warm_ocean"), "patch_natural_coral_pearl_common");
        addFeature(Set.of("minecraft:stony_shore", "minecraft:stony_peaks"), "patch_natural_shipo");
        addFeature(Set.of("minecraft:windswept_hills", "minecraft:windswept_gravelly_hills", "minecraft:windswept_forest"), "patch_natural_shipo_common");
        // ============ 矿物（与 Forge 版 biome_modifier 一致：vegetal_decoration 阶段） ============
        addFeature(Set.of("minecraft:deep_dark"), "patch_natural_deepslate_yeboshi_underground");
        addFeature(Set.of("minecraft:deep_dark"), "patch_natural_yeboshi_underground");
        addFeature(Set.of("minecraft:dripstone_caves"), "patch_natural_deepslate_yeboshi_underground_common");
        addFeature(Set.of("minecraft:dripstone_caves"), "patch_natural_yeboshi_underground_common");
        // 晶化骨髓：与 Forge 版一致，通过覆盖原版 amethyst_geode 晶洞生成（见 data/minecraft/worldgen/configured_feature/amethyst_geode.json）
        addFeature(Set.of("minecraft:stony_peaks"), "patch_natural_yeboshi_surface");
        addFeature(Set.of("minecraft:forest", "minecraft:beach"), "patch_wild_calla_lily");
        addFeature(Set.of("minecraft:river"), "patch_wild_calla_lily_common");
        addFeature(Set.of("minecraft:soul_sand_valley", "minecraft:crimson_forest"), "patch_wild_dendrobium");
        addFeature(Set.of("minecraft:nether_wastes"), "patch_wild_dendrobium_common");
        addFeature(Set.of("minecraft:dark_forest"), "patch_wild_fluorescent_fungus");
        addFeature(Set.of("minecraft:deep_dark"), "patch_wild_fluorescent_fungus_underground");
        addFeature(Set.of("minecraft:soul_sand_valley"), "patch_wild_fluorescent_fungus_common");
        addFeature(Set.of("minecraft:river", "minecraft:beach"), "patch_wild_frostlamp_flower");
        addFeature(Set.of("minecraft:frozen_river", "minecraft:snowy_beach"), "patch_wild_frostlamp_flower_common");
        addFeature(Set.of("minecraft:flower_forest", "minecraft:jungle", "minecraft:sparse_jungle", "minecraft:bamboo_jungle"), "patch_wild_glaze_lily");
        addFeature(Set.of("minecraft:swamp", "minecraft:mangrove_swamp"), "patch_wild_glaze_lily_common");
        addFeature(Set.of("minecraft:savanna", "minecraft:savanna_plateau", "minecraft:desert"), "patch_wild_grainfruit");
        addFeature(Set.of("minecraft:warped_forest"), "patch_wild_grainfruit_common");
        addFeature(Set.of("minecraft:river", "minecraft:swamp", "minecraft:mangrove_swamp"), "patch_wild_horsetail");
        addFeature(Set.of("minecraft:badlands", "minecraft:eroded_badlands", "minecraft:wooded_badlands", "minecraft:nether_wastes"), "patch_wild_jinxin_flower");
        addFeature(Set.of("minecraft:crimson_forest"), "patch_wild_jinxin_flower_common");
        addFeature(Set.of("minecraft:savanna", "minecraft:savanna_plateau", "minecraft:windswept_savanna", "minecraft:windswept_hills", "minecraft:windswept_gravelly_hills", "minecraft:windswept_forest"), "patch_wild_jueyun_chili");
        addFeature(Set.of("minecraft:stony_peaks"), "patch_wild_jueyun_chili_common");
        addFeature(Set.of("minecraft:plains", "minecraft:sunflower_plains"), "patch_wild_mint_common");
        addFeature(Set.of("minecraft:savanna", "minecraft:savanna_plateau", "minecraft:taiga"), "patch_wild_mufeng_mushroom");
        addFeature(Set.of("minecraft:plains"), "patch_wild_mufeng_mushroom_common");
        addFeature(Set.of("minecraft:plains"), "patch_wild_mufeng_mushroom_plains_lantern");
        addFeature(Set.of("minecraft:ocean", "minecraft:lukewarm_ocean"), "patch_wild_sea_ganoderma");
        addFeature(Set.of("minecraft:warm_ocean"), "patch_wild_sea_ganoderma_common");
        addFeature(Set.of("minecraft:forest", "minecraft:flower_forest"), "patch_wild_small_lamp_grass");
        addFeature(Set.of("minecraft:dark_forest"), "patch_wild_small_lamp_grass_common");
        addFeature(Set.of("minecraft:flower_forest", "minecraft:birch_forest", "minecraft:old_growth_birch_forest"), "patch_wild_sumeru_rose");
        addFeature(Set.of("minecraft:jungle", "minecraft:sparse_jungle", "minecraft:bamboo_jungle"), "patch_wild_sumeru_rose_common");
        addFeature(Set.of("minecraft:plains", "minecraft:flower_forest"), "patch_wild_windwheel_aster");
        addFeature(Set.of("minecraft:sunflower_plains"), "patch_wild_windwheel_aster_common");
        addFeature(Set.of("minecraft:stony_peaks", "minecraft:windswept_hills", "minecraft:windswept_gravelly_hills", "minecraft:windswept_forest"), "patch_wild_yunyan_lieye");
        addFeature(Set.of("minecraft:eroded_badlands", "minecraft:badlands", "minecraft:wooded_badlands"), "patch_wild_yunyan_lieye_common");
        // wild_mint 原为 #minecraft:is_overworld 标签
        addFeatureTag("minecraft:is_overworld", "patch_wild_mint");

        // ============ 生物生成（星螺） ============
        BiomeModifications.addSpawn(
            ctx -> ctx.getBiomeKey().location().equals(new ResourceLocation("beach")),
            MobCategory.CREATURE,
            TeyvatDelight.STARCONCH.get(),
            5, 1, 3);
    }

    private static void addFeature(Set<String> biomes, String feature) {
        BiomeModifications.addFeature(
            ctx -> inBiomes(ctx, biomes),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            placedFeatureKey(feature));
    }

    private static void addFeatureTag(String tag, String feature) {
        TagKey<net.minecraft.world.level.biome.Biome> biomeTag =
            TagKey.create(Registries.BIOME, ResourceLocation.tryParse(tag));
        BiomeModifications.addFeature(
            ctx -> ctx.hasTag(biomeTag),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            placedFeatureKey(feature));
    }

    private static ResourceKey<net.minecraft.world.level.levelgen.placement.PlacedFeature> placedFeatureKey(String feature) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
            new ResourceLocation(TeyvatDelight.MODID, feature));
    }

    private static boolean inBiomes(BiomeSelectionContext ctx, Set<String> biomes) {
        ResourceLocation biomeId = ctx.getBiomeKey().location();
        for (String b : biomes) {
            if (biomeId.equals(ResourceLocation.tryParse(b))) {
                return true;
            }
        }
        return false;
    }
}
