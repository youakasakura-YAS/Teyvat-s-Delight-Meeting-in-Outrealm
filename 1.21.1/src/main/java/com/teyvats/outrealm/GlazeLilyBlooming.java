package com.teyvats.outrealm;

import com.teyvats.outrealm.GlazeLilyCropBlock;
import com.teyvats.outrealm.TeyvatDelight;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

final class GlazeLilyBlooming {
    static final BooleanProperty BLOOMING = BooleanProperty.create((String)"blooming");

    private GlazeLilyBlooming() {
    }

    static boolean shouldBloom(Level level) {
        long dayTime = level.getDayTime() % 24000L;
        return dayTime >= 13000L && dayTime < 23000L;
    }

    static boolean isGlazeLily(BlockState state) {
        return state.is((Block)TeyvatDelight.GLAZE_LILY_CROP.get()) || state.is((Block)TeyvatDelight.WILD_GLAZE_LILY.get());
    }

    static boolean updateIfNeeded(ServerLevel level, BlockPos pos, BlockState state, boolean nightBlooming) {
        if (!state.hasProperty((Property)BLOOMING)) {
            return false;
        }
        boolean shouldBloom = nightBlooming;
        if (state.is((Block)TeyvatDelight.GLAZE_LILY_CROP.get())) {
            boolean bl = shouldBloom = shouldBloom && (Integer)state.getValue((Property)CropBlock.AGE) >= ((GlazeLilyCropBlock)((Object)TeyvatDelight.GLAZE_LILY_CROP.get())).getMaxAge();
        }
        if ((Boolean)state.getValue((Property)BLOOMING) == shouldBloom) {
            return false;
        }
        level.setBlock(pos, (BlockState)state.setValue((Property)BLOOMING, (Comparable)Boolean.valueOf(shouldBloom)), 3);
        return true;
    }
}

