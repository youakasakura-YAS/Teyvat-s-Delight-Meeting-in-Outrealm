package com.teyvats.outrealm;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * 战利品注入（Fabric 等价实现）。
 * 原 NeoForge 通过 55 个 neoforge:add_table loot_modifier 把
 * teyvats_delight_meeting_in_outrealm:chests/mora_primogem_{low,mid,high} 注入 55 个原版箱子表；
 * Fabric 无 add_table 机制，这里用 LootTableEvents.MODIFY 在每个目标表上追加两个等价 pool。
 */
public final class TeyvatLootModifiers {
    private static final Map<ResourceLocation, Integer> TIER_BY_TABLE = new HashMap<>();

    static {
        // tier 0 = low，1 = mid，2 = high
        put("chests/abandoned_mineshaft", 0);
        put("chests/igloo_chest", 0);
        put("chests/ruined_portal", 0);
        put("chests/shipwreck_map", 0);
        put("chests/shipwreck_supply", 0);
        put("chests/simple_dungeon", 0);
        put("chests/spawn_bonus_chest", 0);
        put("chests/village/village_armorer", 0);
        put("chests/village/village_butcher", 0);
        put("chests/village/village_cartographer", 0);
        put("chests/village/village_desert_house", 0);
        put("chests/village/village_fisher", 0);
        put("chests/village/village_fletcher", 0);
        put("chests/village/village_mason", 0);
        put("chests/village/village_plains_house", 0);
        put("chests/village/village_savanna_house", 0);
        put("chests/village/village_shepherd", 0);
        put("chests/village/village_snowy_house", 0);
        put("chests/village/village_taiga_house", 0);
        put("chests/village/village_tannery", 0);
        put("chests/village/village_temple", 0);
        put("chests/village/village_toolsmith", 0);

        put("chests/desert_pyramid", 1);
        put("chests/jungle_temple", 1);
        put("chests/nether_bridge", 1);
        put("chests/pillager_outpost", 1);
        put("chests/shipwreck_treasure", 1);
        put("chests/stronghold_corridor", 1);
        put("chests/stronghold_crossing", 1);
        put("chests/stronghold_library", 1);
        put("chests/trial_chambers/corridor", 1);
        put("chests/trial_chambers/entrance", 1);
        put("chests/trial_chambers/intersection", 1);
        put("chests/trial_chambers/intersection_barrel", 1);
        put("chests/trial_chambers/reward_common", 1);
        put("chests/trial_chambers/supply", 1);
        put("chests/underwater_ruin_big", 1);
        put("chests/underwater_ruin_small", 1);
        put("chests/village/village_weaponsmith", 1);
        put("chests/woodland_mansion", 1);

        put("chests/ancient_city", 2);
        put("chests/ancient_city_ice_box", 2);
        put("chests/bastion_bridge", 2);
        put("chests/bastion_hoglin_stable", 2);
        put("chests/bastion_other", 2);
        put("chests/bastion_treasure", 2);
        put("chests/buried_treasure", 2);
        put("chests/end_city_treasure", 2);
        put("chests/trial_chambers/reward", 2);
        put("chests/trial_chambers/reward_ominous", 2);
        put("chests/trial_chambers/reward_ominous_common", 2);
        put("chests/trial_chambers/reward_ominous_rare", 2);
        put("chests/trial_chambers/reward_ominous_unique", 2);
        put("chests/trial_chambers/reward_rare", 2);
        put("chests/trial_chambers/reward_unique", 2);
    }

    private static void put(String path, int tier) {
        TIER_BY_TABLE.put(ResourceLocation.withDefaultNamespace(path), tier);
    }

    private TeyvatLootModifiers() {
    }

    public static void registerAll() {
        LootTableEvents.MODIFY.register((key, builder, source, context) -> {
            Integer tier = TIER_BY_TABLE.get(key.location());
            if (tier == null) {
                return;
            }
            TeyvatLootModifiers.addPools(builder, tier);
        });
    }

    private static void addPools(LootTable.Builder builder, int tier) {
        float moraMin;
        float moraMax;
        int moraWeight;
        int moraEmptyWeight;
        int primogemWeight;
        int primogemEmptyWeight;
        switch (tier) {
            case 0 -> {
                moraMin = 0.0f;
                moraMax = 2.0f;
                moraWeight = 75;
                moraEmptyWeight = 25;
                primogemWeight = 10;
                primogemEmptyWeight = 90;
            }
            case 1 -> {
                moraMin = 1.0f;
                moraMax = 3.0f;
                moraWeight = 60;
                moraEmptyWeight = 40;
                primogemWeight = 30;
                primogemEmptyWeight = 70;
            }
            default -> {
                moraMin = 1.0f;
                moraMax = 3.0f;
                moraWeight = 10;
                moraEmptyWeight = 90;
                primogemWeight = 50;
                primogemEmptyWeight = 50;
            }
        }
        builder.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                .add(LootItem.lootTableItem(TeyvatDelight.MORA.get()).setWeight(moraWeight)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(moraMin, moraMax))))
                .add(EmptyLootItem.emptyItem().setWeight(moraEmptyWeight)));
        builder.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                .add(LootItem.lootTableItem(TeyvatDelight.PRIMOGEM.get()).setWeight(primogemWeight)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f))))
                .add(EmptyLootItem.emptyItem().setWeight(primogemEmptyWeight)));
    }
}
