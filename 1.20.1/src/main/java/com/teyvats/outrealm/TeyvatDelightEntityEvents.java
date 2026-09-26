package com.teyvats.outrealm;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Fabric 移植版实体注册：
 * - EntityAttributeCreationEvent   -> FabricDefaultAttributeRegistry（fabric-object-builder-api-v1）
 * - RegisterSpawnPlacementsEvent   -> SpawnPlacements.register（原版 API）
 */
public final class TeyvatDelightEntityEvents {
    private TeyvatDelightEntityEvents() {
    }

    public static void registerAll() {
        EntityType<StarconchEntity> starconch = TeyvatDelight.STARCONCH.get();
        // 属性：最大生命 1.0
        FabricDefaultAttributeRegistry.register(starconch, StarconchEntity.createAttributes());
        // 生成规则：地面、MOTION_BLOCKING_NO_LEAVES 高度图 + 自定义规则
        SpawnPlacements.register(
                starconch,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StarconchEntity::checkStarconchSpawnRules);
    }
}
