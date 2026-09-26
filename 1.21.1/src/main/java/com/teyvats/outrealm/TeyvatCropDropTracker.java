package com.teyvats.outrealm;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

final class TeyvatCropDropTracker {
    private static final Set<DropKey> SKIPPED_DROPS = ConcurrentHashMap.newKeySet();
    private static final Map<DropKey, BlockState> REMEMBERED_STATES = new ConcurrentHashMap<DropKey, BlockState>();

    private TeyvatCropDropTracker() {
    }

    static void skipNextDrop(Level level, BlockPos pos) {
        SKIPPED_DROPS.add(TeyvatCropDropTracker.key(level, pos));
    }

    static boolean consumeSkipDrop(Level level, BlockPos pos) {
        return SKIPPED_DROPS.remove(TeyvatCropDropTracker.key(level, pos));
    }

    static void rememberState(Level level, BlockPos pos, BlockState state) {
        REMEMBERED_STATES.put(TeyvatCropDropTracker.key(level, pos), state);
    }

    static Optional<BlockState> consumeRememberedState(Level level, BlockPos pos) {
        return Optional.ofNullable(REMEMBERED_STATES.remove(TeyvatCropDropTracker.key(level, pos)));
    }

    private static DropKey key(Level level, BlockPos pos) {
        return new DropKey(level.dimension().location(), pos.asLong());
    }

    private record DropKey(ResourceLocation dimension, long pos) {
    }
}

