package com.teyvats.outrealm;

import com.teyvats.outrealm.PearlBearingCoralShellBlock;
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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrowingCoralShellBlock
extends BushBlock {
    public static final MapCodec<GrowingCoralShellBlock> CODEC = GrowingCoralShellBlock.simpleCodec(GrowingCoralShellBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)8.0, (double)14.0);

    public GrowingCoralShellBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
    }

    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(TeyvatTags.Blocks.CHU_CI_ZHU_FIELDS);
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0) {
            BlockState next = (BlockState)((PearlBearingCoralShellBlock)((Object)TeyvatDelight.PEARL_BEARING_CORAL_SHELL.get())).defaultBlockState().setValue((Property)FACING, (Comparable)((Direction)state.getValue((Property)FACING)));
            level.setBlock(pos, next, 2);
        }
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.is(Items.KELP)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        if (!level.isClientSide) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            if (level.random.nextInt(8) == 0) {
                BlockState next = (BlockState)((PearlBearingCoralShellBlock)((Object)TeyvatDelight.PEARL_BEARING_CORAL_SHELL.get())).defaultBlockState().setValue((Property)FACING, (Comparable)((Direction)state.getValue((Property)FACING)));
                level.setBlock(pos, next, 2);
                level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            } else {
                level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 0.6f);
            }
        }
        return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)TeyvatDelight.CORAL_SHELL.get());
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && !player.isCreative()) {
            GrowingCoralShellBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)TeyvatDelight.CORAL_SHELL.get()));
        }
    }
}

