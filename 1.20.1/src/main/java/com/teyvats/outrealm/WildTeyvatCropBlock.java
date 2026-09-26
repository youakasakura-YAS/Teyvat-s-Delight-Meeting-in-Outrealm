package com.teyvats.outrealm;

import com.teyvats.outrealm.SeedDispensaryItem;
import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatTags;
import com.teyvats.outrealm.WildMufengMushroomBlock;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class WildTeyvatCropBlock
extends BushBlock {
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)13.0, (double)14.0);
    private final Supplier<? extends ItemLike> cropItem;
    private final Supplier<? extends ItemLike> seedItem;
    private final Surface surface;

    protected WildTeyvatCropBlock(BlockBehaviour.Properties properties, Supplier<? extends ItemLike> cropItem, Supplier<? extends ItemLike> seedItem, Surface surface) {
        super(properties);
        this.cropItem = cropItem;
        this.seedItem = seedItem;
        this.surface = surface;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return switch (this.surface.ordinal()) {
            default -> throw new IllegalStateException("Unexpected value");
            case 0 -> state.is(TeyvatTags.Blocks.WILD_GRASS_OR_DIRT_CROP_SURFACES);
            case 1 -> state.is(TeyvatTags.Blocks.WILD_ROCKY_CROP_SURFACES);
            case 2 -> state.is(TeyvatTags.Blocks.WILD_GRASS_DIRT_OR_MUD_CROP_SURFACES);
            case 3 -> {
                if (WildTeyvatCropBlock.isCallaLilySurface(state) && WildTeyvatCropBlock.hasAdjacentWater(level, pos)) {
                    yield true;
                }
                yield false;
            }
            case 4 -> WildMufengMushroomBlock.canAttachTo(state);
            case 5 -> {
                if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.NETHERRACK) || state.is(Blocks.WARPED_NYLIUM) || state.is(Blocks.WARPED_ROOTS) || state.is(Blocks.WARPED_FUNGUS)) {
                    yield true;
                }
                yield false;
            }
            case 6 -> state.is(TeyvatTags.Blocks.WILD_FLUORESCENT_FUNGUS_SURFACES);
            case 7 -> state.is(TeyvatTags.Blocks.WILD_DENDROBIUM_SURFACES);
            case 8 -> state.is(TeyvatTags.Blocks.WILD_FROSTLAMP_SURFACES);
            case 9 -> state.is(TeyvatTags.Blocks.WILD_YUNYAN_CRACKLEAF_SURFACES);
        };
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)this.asItem());
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && !player.isCreative()) {
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, this.seedItem.get());
            TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, tool);
        }
    }

    private static boolean hasAdjacentWater(BlockGetter level, BlockPos groundPos) {
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

    protected static enum Surface {
        GRASS_OR_DIRT,
        ROCKY,
        GRASS_DIRT_OR_MUD,
        SAND_NEAR_WATER,
        MUFENG_BUILDING,
        GRAINFRUIT,
        FLUORESCENT_FUNGUS,
        DENDROBIUM,
        FROSTLAMP,
        STONE_OR_TERRACOTTA;

    }
}

