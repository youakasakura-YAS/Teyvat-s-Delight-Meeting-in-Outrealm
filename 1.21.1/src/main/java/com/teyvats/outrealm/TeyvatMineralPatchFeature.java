package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatMineralPatchConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class TeyvatMineralPatchFeature
extends Feature<TeyvatMineralPatchConfiguration> {
    public TeyvatMineralPatchFeature(Codec<TeyvatMineralPatchConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<TeyvatMineralPatchConfiguration> context) {
        TeyvatMineralPatchConfiguration config = (TeyvatMineralPatchConfiguration)context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockState state = config.state();
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        int attempts = Math.max(targetCount * 8, 32);
        int placed = 0;
        block0: for (int attempt = 0; attempt < attempts && placed < targetCount; ++attempt) {
            int z;
            int y;
            int x = origin.getX() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            BlockPos pos = new BlockPos(x, y = origin.getY() + random.nextInt(config.ySpread() * 2 + 1) - config.ySpread(), z = origin.getZ() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread());
            if (!level.ensureCanWrite(pos) || !level.isEmptyBlock(pos)) continue;
            if (state.hasProperty((Property)BlockStateProperties.FACING)) {
                for (Direction facing : Direction.values()) {
                    BlockState candidate = (BlockState)state.setValue((Property)BlockStateProperties.FACING, (Comparable)facing);
                    if (!candidate.canSurvive((LevelReader)level, pos)) continue;
                    level.setBlock(pos, candidate, 2);
                    ++placed;
                    continue block0;
                }
                continue;
            }
            if (!state.canSurvive((LevelReader)level, pos)) continue;
            level.setBlock(pos, state, 2);
            ++placed;
        }
        return placed > 0;
    }
}

