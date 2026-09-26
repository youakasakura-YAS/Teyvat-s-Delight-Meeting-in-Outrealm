package com.teyvats.outrealm;

import com.teyvats.outrealm.HorsetailBottomBlock;
import com.teyvats.outrealm.SeedDispensaryItem;
import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.TeyvatTags;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HorsetailTopBlock
extends BushBlock
implements BonemealableBlock {
    public static final MapCodec<HorsetailTopBlock> CODEC = HorsetailTopBlock.simpleCodec(HorsetailTopBlock::new);
    public static final IntegerProperty AGE = IntegerProperty.create((String)"age", (int)0, (int)2);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box((double)3.0, (double)0.0, (double)3.0, (double)13.0, (double)8.0, (double)13.0), Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)12.0, (double)14.0), Block.box((double)1.0, (double)0.0, (double)1.0, (double)15.0, (double)16.0, (double)15.0)};

    public HorsetailTopBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, (Comparable)Integer.valueOf(0)));
    }

    public MapCodec<HorsetailTopBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[(Integer)state.getValue((Property)AGE)];
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is((Block)TeyvatDelight.HORSETAIL_BOTTOM.get()) && (Integer)state.getValue((Property)HorsetailBottomBlock.AGE) >= 3;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) < 9 || (Integer)state.getValue((Property)AGE) >= 2) {
            return;
        }
        if (random.nextInt(7) == 0) {
            this.growOneStep(level, pos, state);
        }
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if ((Integer)state.getValue((Property)AGE) >= 2) {
            this.harvestAndReset(level, pos, state, player);
            return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if ((Integer)state.getValue((Property)AGE) >= 2) {
            this.harvestAndReset(level, pos, state, player);
            return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
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
            HorsetailTopBlock.dropForBreak(level, pos, state, player, tool);
        }
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            HorsetailTopBlock.dropForBreak(level, pos, state, null, ItemStack.EMPTY);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canSurvive((LevelReader)level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return (Integer)state.getValue((Property)AGE) < 2;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.growOneStep(level, pos, state);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)TeyvatDelight.HORSETAIL_SEEDS.get());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    void growOneStep(ServerLevel level, BlockPos pos, BlockState state) {
        int age = (Integer)state.getValue((Property)AGE);
        if (age < 2) {
            level.setBlock(pos, (BlockState)state.setValue((Property)AGE, (Comparable)Integer.valueOf(age + 1)), 2);
        }
    }

    void harvestAndReset(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            int count = 1 + (level.getBlockState(pos.below().below()).is(TeyvatTags.Blocks.CHU_CI_ZHU_FIELDS) ? 1 : 0);
            HorsetailTopBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.HORSETAIL.get(), count));
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, (ItemLike)TeyvatDelight.HORSETAIL_SEEDS.get());
            TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, player.getMainHandItem());
            level.setBlock(pos, (BlockState)state.setValue((Property)AGE, (Comparable)Integer.valueOf(0)), 2);
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    static void dropForBreak(Level level, BlockPos pos, BlockState state, @Nullable Player player, ItemStack tool) {
        HorsetailTopBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.HORSETAIL_SEEDS.get()));
        if ((Integer)state.getValue((Property)AGE) >= 2) {
            int count = 1 + (level.getBlockState(pos.below().below()).is(TeyvatTags.Blocks.CHU_CI_ZHU_FIELDS) ? 1 : 0);
            HorsetailTopBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.HORSETAIL.get(), count));
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, (ItemLike)TeyvatDelight.HORSETAIL_SEEDS.get());
            if (player != null) {
                TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, tool);
            }
        }
    }
}

