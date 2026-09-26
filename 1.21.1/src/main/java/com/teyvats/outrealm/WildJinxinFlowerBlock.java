package com.teyvats.outrealm;

import com.teyvats.outrealm.JinxinFlowerBehavior;
import com.teyvats.outrealm.SeedDispensaryItem;
import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import com.teyvats.outrealm.TeyvatDelight;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WildJinxinFlowerBlock
extends BushBlock {
    public static final MapCodec<WildJinxinFlowerBlock> CODEC = WildJinxinFlowerBlock.simpleCodec(WildJinxinFlowerBlock::new);
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)13.0, (double)14.0);

    public WildJinxinFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected MapCodec<WildJinxinFlowerBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return WildJinxinFlowerBlock.canGrowOn(state);
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!JinxinFlowerBehavior.isCoolingItem(stack)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        if (!level.isClientSide) {
            WildJinxinFlowerBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.JINXIN_FLOWER_BUD.get()));
            WildJinxinFlowerBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.JINXIN_FLOWER.get()));
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, (ItemLike)TeyvatDelight.JINXIN_FLOWER_BUD.get());
            TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, player.getMainHandItem());
            TeyvatCropDropTracker.skipNextDrop(level, pos);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            JinxinFlowerBehavior.playCoolingSound(level, pos);
        }
        return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)this.asItem());
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            TeyvatCropDropTracker.skipNextDrop(level, pos);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        TeyvatCropDropTracker.consumeSkipDrop(level, pos);
        if (!level.isClientSide && !player.isCreative()) {
            JinxinFlowerBehavior.dropBud(level, pos);
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, (ItemLike)TeyvatDelight.JINXIN_FLOWER_BUD.get());
        }
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            JinxinFlowerBehavior.dropBud(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public static boolean canGrowOn(BlockState state) {
        return state.is(Blocks.RED_SAND) || state.is(Blocks.NETHERRACK) || state.is(Blocks.CRIMSON_NYLIUM) || state.is(Blocks.CRIMSON_ROOTS) || state.is(Blocks.CRIMSON_FUNGUS) || WildJinxinFlowerBlock.isTerracotta(state);
    }

    private static boolean isTerracotta(BlockState state) {
        return state.is(Blocks.TERRACOTTA) || state.is(Blocks.WHITE_TERRACOTTA) || state.is(Blocks.ORANGE_TERRACOTTA) || state.is(Blocks.MAGENTA_TERRACOTTA) || state.is(Blocks.LIGHT_BLUE_TERRACOTTA) || state.is(Blocks.YELLOW_TERRACOTTA) || state.is(Blocks.LIME_TERRACOTTA) || state.is(Blocks.PINK_TERRACOTTA) || state.is(Blocks.GRAY_TERRACOTTA) || state.is(Blocks.LIGHT_GRAY_TERRACOTTA) || state.is(Blocks.CYAN_TERRACOTTA) || state.is(Blocks.PURPLE_TERRACOTTA) || state.is(Blocks.BLUE_TERRACOTTA) || state.is(Blocks.BROWN_TERRACOTTA) || state.is(Blocks.GREEN_TERRACOTTA) || state.is(Blocks.RED_TERRACOTTA) || state.is(Blocks.BLACK_TERRACOTTA);
    }
}

