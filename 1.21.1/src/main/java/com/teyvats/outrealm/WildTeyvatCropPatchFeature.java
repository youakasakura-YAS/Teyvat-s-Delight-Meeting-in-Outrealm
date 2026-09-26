package com.teyvats.outrealm;

import com.teyvats.outrealm.NaturalCoralPearlBlock;
import com.teyvats.outrealm.WildCallaLilyBlock;
import com.teyvats.outrealm.WildDendrobiumBlock;
import com.teyvats.outrealm.WildFluorescentFungusBlock;
import com.teyvats.outrealm.WildFrostlampFlowerBlock;
import com.teyvats.outrealm.WildGrainfruitBlock;
import com.teyvats.outrealm.WildHorsetailBlock;
import com.teyvats.outrealm.WildJinxinFlowerBlock;
import com.teyvats.outrealm.WildMufengMushroomBlock;
import com.teyvats.outrealm.WildSeaGanodermaBlock;
import com.teyvats.outrealm.WildSumeruRoseBlock;
import com.teyvats.outrealm.WildTeyvatCropPatchConfiguration;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class WildTeyvatCropPatchFeature
extends Feature<WildTeyvatCropPatchConfiguration> {
    public WildTeyvatCropPatchFeature(Codec<WildTeyvatCropPatchConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<WildTeyvatCropPatchConfiguration> context) {
        WildTeyvatCropPatchConfiguration config = (WildTeyvatCropPatchConfiguration)context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockState state = config.state();
        if (state.getBlock() instanceof WildMufengMushroomBlock) {
            if (config.mufengTopOnlyStripped()) {
                return WildTeyvatCropPatchFeature.placeTopOnlyStrippedMufengPatch(config, level, random, origin, state);
            }
            return WildTeyvatCropPatchFeature.placeClingingPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildCallaLilyBlock) {
            return WildTeyvatCropPatchFeature.placeWildCallaLilyPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildHorsetailBlock) {
            return WildTeyvatCropPatchFeature.placeWildHorsetailPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildSeaGanodermaBlock) {
            return WildTeyvatCropPatchFeature.placeWildSeaGanodermaPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof NaturalCoralPearlBlock) {
            return WildTeyvatCropPatchFeature.placeWildCoralPearlPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildJinxinFlowerBlock) {
            return WildTeyvatCropPatchFeature.placeDeepScanningPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildFluorescentFungusBlock) {
            return WildTeyvatCropPatchFeature.placeDeepScanningPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildDendrobiumBlock) {
            return WildTeyvatCropPatchFeature.placeDeepScanningPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildFrostlampFlowerBlock) {
            return WildTeyvatCropPatchFeature.placeFrostlampPatch(config, level, random, origin, state);
        }
        if (state.getBlock() instanceof WildGrainfruitBlock || state.getBlock() instanceof WildSumeruRoseBlock) {
            return WildTeyvatCropPatchFeature.placeSurfaceScanningPatch(config, level, random, origin, state);
        }
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        int attempts = Math.max(targetCount * 4, 20);
        int placed = 0;
        for (int attempt = 0; attempt < attempts && placed < targetCount; ++attempt) {
            int z;
            int y;
            int x = origin.getX() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            BlockPos pos = new BlockPos(x, y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z = origin.getZ() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread()), z);
            if (!level.ensureCanWrite(pos) || !level.isEmptyBlock(pos) || !state.canSurvive((LevelReader)level, pos)) continue;
            level.setBlock(pos, state, 2);
            ++placed;
        }
        return placed > 0;
    }

    private static boolean placeWildCallaLilyPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        int attempts = Math.max(targetCount * 8, 32);
        int placed = 0;
        for (int attempt = 0; attempt < attempts && placed < targetCount; ++attempt) {
            int z;
            int y;
            int x = origin.getX() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            BlockPos pos = new BlockPos(x, y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z = origin.getZ() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread()), z);
            if (!WildTeyvatCropPatchFeature.tryPlaceWildCallaLily(level, state, pos)) continue;
            ++placed;
        }
        return placed > 0;
    }

    private static boolean tryPlaceWildCallaLily(WorldGenLevel level, BlockState state, BlockPos pos) {
        BlockPos groundPos = pos.below();
        if (!(level.ensureCanWrite(pos) && level.isEmptyBlock(pos) && WildTeyvatCropPatchFeature.isCallaLilySurface(level.getBlockState(groundPos)) && WildTeyvatCropPatchFeature.hasAdjacentWater(level, groundPos) && state.canSurvive((LevelReader)level, pos))) {
            return false;
        }
        level.setBlock(pos, state, 2);
        return true;
    }

    private static boolean placeClingingPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        int attempts = Math.max(targetCount * 48, 96);
        int placed = 0;
        for (int attempt = 0; attempt < attempts && placed < targetCount; ++attempt) {
            int supportY;
            int x = origin.getX() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            int z = origin.getZ() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            for (int yOffset = 0; yOffset < 7 && placed < targetCount && (supportY = topY - 1 - yOffset) >= level.getMinBuildHeight(); ++yOffset) {
                BlockPos supportPos = new BlockPos(x, supportY, z);
                if (!WildMufengMushroomBlock.canAttachTo(level.getBlockState(supportPos)) || !WildTeyvatCropPatchFeature.tryPlaceFromSupport(level, random, state, supportPos, topY)) continue;
                ++placed;
            }
        }
        return placed > 0;
    }

    private static boolean tryPlaceFromSupport(WorldGenLevel level, RandomSource random, BlockState baseState, BlockPos supportPos, int columnTopY) {
        Direction[] outwardDirections = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        int start = random.nextInt(outwardDirections.length);
        for (int i = 0; i < outwardDirections.length; ++i) {
            BlockState placedState;
            Direction outwardDirection = outwardDirections[(start + i) % outwardDirections.length];
            BlockPos pos = WildTeyvatCropPatchFeature.getTargetPos(supportPos, outwardDirection);
            if (pos == null || !WildTeyvatCropPatchFeature.isGoodMufengTarget(level, pos) || outwardDirection != Direction.UP && (supportPos.getY() < columnTopY - 4 || !level.getBlockState(pos.below()).isAir()) || !(placedState = (BlockState)baseState.setValue((Property)WildMufengMushroomBlock.FACING, (Comparable)outwardDirection)).canSurvive((LevelReader)level, pos)) continue;
            level.setBlock(pos, placedState, 2);
            return true;
        }
        return false;
    }

    private static BlockPos getTargetPos(BlockPos supportPos, Direction outwardDirection) {
        if (!WildMufengMushroomBlock.isAllowedAttachmentDirection(outwardDirection)) {
            return null;
        }
        return supportPos.relative(outwardDirection);
    }

    private static boolean isGoodMufengTarget(WorldGenLevel level, BlockPos pos) {
        return level.ensureCanWrite(pos) && level.isEmptyBlock(pos) && level.canSeeSky(pos);
    }

    private static boolean placeWildHorsetailPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        BlockPos basePos = level.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int placed = 0;
        for (int attempt = 0; attempt < config.tries() && placed < targetCount; ++attempt) {
            mutable.set((Vec3i)basePos).move(random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1), random.nextInt(config.ySpread() + 1) - random.nextInt(config.ySpread() + 1), random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1));
            if (!WildTeyvatCropPatchFeature.tryPlaceWildHorsetail(level, state, (BlockPos)mutable)) continue;
            ++placed;
        }
        return placed > 0;
    }

    private static boolean placeWildSeaGanodermaPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        BlockPos basePos = level.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int placed = 0;
        for (int attempt = 0; attempt < config.tries() && placed < targetCount; ++attempt) {
            mutable.set((Vec3i)basePos).move(random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1), random.nextInt(config.ySpread() + 1) - random.nextInt(config.ySpread() + 1), random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1));
            if (!WildTeyvatCropPatchFeature.tryPlaceWildSeaGanoderma(level, state, (BlockPos)mutable)) continue;
            ++placed;
        }
        return placed > 0;
    }

    private static boolean tryPlaceWildSeaGanoderma(WorldGenLevel level, BlockState state, BlockPos waterPos) {
        if (!level.ensureCanWrite(waterPos) || !level.getBlockState(waterPos).is(Blocks.WATER)) {
            return false;
        }
        BlockState placedState = (BlockState)state.setValue((Property)WildSeaGanodermaBlock.WATERLOGGED, (Comparable)Boolean.valueOf(true));
        if (!placedState.canSurvive((LevelReader)level, waterPos)) {
            return false;
        }
        level.setBlock(waterPos, placedState, 2);
        return true;
    }

    private static boolean placeWildCoralPearlPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        BlockPos basePos = level.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int placed = 0;
        for (int attempt = 0; attempt < config.tries() && placed < targetCount; ++attempt) {
            mutable.set((Vec3i)basePos).move(random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1), random.nextInt(config.ySpread() + 1) - random.nextInt(config.ySpread() + 1), random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1));
            if (!WildTeyvatCropPatchFeature.tryPlaceWildCoralPearl(level, state, (BlockPos)mutable, random)) continue;
            ++placed;
        }
        return placed > 0;
    }

    private static boolean tryPlaceWildCoralPearl(WorldGenLevel level, BlockState state, BlockPos pos, RandomSource random) {
        if (!level.ensureCanWrite(pos)) {
            return false;
        }
        boolean isWater = level.getFluidState(pos).is(FluidTags.WATER);
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockState placedState = (BlockState)((BlockState)state.setValue((Property)NaturalCoralPearlBlock.WATERLOGGED, (Comparable)Boolean.valueOf(isWater))).setValue((Property)NaturalCoralPearlBlock.FACING, (Comparable)facing);
        if (isWater ? level.getFluidState(pos).getAmount() != 8 : !level.isEmptyBlock(pos)) {
            return false;
        }
        if (!placedState.canSurvive((LevelReader)level, pos)) {
            return false;
        }
        level.setBlock(pos, placedState, 2);
        return true;
    }

    private static boolean placeDeepScanningPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        int attempts = Math.max(targetCount * 12, 48);
        int placed = 0;
        block0: for (int attempt = 0; attempt < attempts && placed < targetCount; ++attempt) {
            int x = origin.getX() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            int z = origin.getZ() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            int minY = Math.max(level.getMinBuildHeight() + 1, topY - 96);
            for (int y = topY; y >= minY; --y) {
                BlockPos pos = new BlockPos(x, y, z);
                if (!level.ensureCanWrite(pos) || !level.isEmptyBlock(pos) || !state.canSurvive((LevelReader)level, pos)) continue;
                level.setBlock(pos, state, 2);
                ++placed;
                continue block0;
            }
        }
        return placed > 0;
    }

    private static boolean placeSurfaceScanningPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        int attempts = Math.max(targetCount * 16, 48);
        int placed = 0;
        block0: for (int attempt = 0; attempt < attempts && placed < targetCount; ++attempt) {
            int x = origin.getX() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            int z = origin.getZ() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            int minY = Math.max(level.getMinBuildHeight() + 1, topY - 48);
            for (int y = topY; y >= minY; --y) {
                BlockPos pos = new BlockPos(x, y, z);
                if (!level.ensureCanWrite(pos) || !level.isEmptyBlock(pos) || !state.canSurvive((LevelReader)level, pos)) continue;
                level.setBlock(pos, state, 2);
                ++placed;
                continue block0;
            }
        }
        return placed > 0;
    }

    private static boolean placeFrostlampPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = minCount + random.nextInt(maxCount - minCount + 1);
        int attempts = Math.max(targetCount * 16, 48);
        int placed = 0;
        for (int attempt = 0; attempt < attempts && placed < targetCount; ++attempt) {
            int z;
            int topY;
            int x = origin.getX() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread();
            BlockPos airPos = new BlockPos(x, topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z = origin.getZ() + random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread()), z);
            BlockPos surfacePos = airPos.below();
            BlockState surfaceState = level.getBlockState(surfacePos);
            if (surfaceState.is(Blocks.SNOW)) {
                if (!level.ensureCanWrite(surfacePos) || !state.canSurvive((LevelReader)level, surfacePos)) continue;
                level.setBlock(surfacePos, state, 2);
                ++placed;
                continue;
            }
            if (!level.ensureCanWrite(airPos) || !level.isEmptyBlock(airPos) || !state.canSurvive((LevelReader)level, airPos)) continue;
            level.setBlock(airPos, state, 2);
            ++placed;
        }
        return placed > 0;
    }

    private static boolean tryPlaceWildHorsetail(WorldGenLevel level, BlockState state, BlockPos waterPos) {
        BlockPos topPos = waterPos.above();
        if (!(level.ensureCanWrite(waterPos) && level.ensureCanWrite(topPos) && level.getBlockState(waterPos).is(Blocks.WATER) && level.isEmptyBlock(topPos))) {
            return false;
        }
        BlockState lowerState = (BlockState)((BlockState)state.setValue((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER)).setValue((Property)WildHorsetailBlock.WATERLOGGED, (Comparable)Boolean.valueOf(true));
        if (!lowerState.canSurvive((LevelReader)level, waterPos)) {
            return false;
        }
        DoublePlantBlock.placeAt((LevelAccessor)level, (BlockState)lowerState, (BlockPos)waterPos, (int)2);
        return true;
    }

    private static boolean placeTopOnlyStrippedMufengPatch(WildTeyvatCropPatchConfiguration config, WorldGenLevel level, RandomSource random, BlockPos origin, BlockState state) {
        ArrayList<BlockPos> candidates = new ArrayList<BlockPos>();
        int spread = config.xzSpread();
        for (int xOffset = -spread; xOffset <= spread; ++xOffset) {
            for (int zOffset = -spread; zOffset <= spread; ++zOffset) {
                int x = origin.getX() + xOffset;
                int z = origin.getZ() + zOffset;
                int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos supportPos = new BlockPos(x, topY - 1, z);
                BlockPos pos = supportPos.above();
                if (!WildMufengMushroomBlock.isStrippedWoodAttachment(level.getBlockState(supportPos)) || !WildTeyvatCropPatchFeature.isGoodMufengTarget(level, pos)) continue;
                candidates.add(pos);
            }
        }
        int minCount = Math.min(config.minCount(), config.maxCount());
        int maxCount = Math.max(config.minCount(), config.maxCount());
        int targetCount = Math.min(candidates.size(), minCount + random.nextInt(maxCount - minCount + 1));
        int placed = 0;
        while (placed < targetCount && !candidates.isEmpty()) {
            BlockPos pos = (BlockPos)candidates.remove(random.nextInt(candidates.size()));
            BlockState placedState = (BlockState)state.setValue((Property)WildMufengMushroomBlock.FACING, (Comparable)Direction.UP);
            if (!placedState.canSurvive((LevelReader)level, pos)) continue;
            level.setBlock(pos, placedState, 2);
            ++placed;
        }
        return placed > 0;
    }

    private static boolean hasAdjacentWater(WorldGenLevel level, BlockPos groundPos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = groundPos.relative(direction);
            if (!level.getFluidState(neighborPos).is(FluidTags.WATER) && !level.getBlockState(neighborPos).is(Blocks.FROSTED_ICE)) continue;
            return true;
        }
        return false;
    }

    private static boolean isCallaLilySurface(BlockState state) {
        return state.is(BlockTags.SAND) || state.is(Blocks.DIRT);
    }
}

