package com.teyvats.outrealm;

import com.teyvats.outrealm.TeyvatDelight;
import com.teyvats.outrealm.WildTeyvatCropBlock;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.properties.Property;

public class WildMufengMushroomBlock
extends WildTeyvatCropBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public WildMufengMushroomBlock(BlockBehaviour.Properties properties) {
        super(properties, (Supplier<? extends ItemLike>)TeyvatDelight.MUFENG_MUSHROOM, (Supplier<? extends ItemLike>)TeyvatDelight.MUFENG_MUSHROOM_SPORES, WildTeyvatCropBlock.Surface.MUFENG_BUILDING);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        BlockState state = (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)facing);
        return WildMufengMushroomBlock.isAllowedAttachmentDirection(facing) && state.canSurvive((LevelReader)context.getLevel(), context.getClickedPos()) ? state : null;
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = (Direction)state.getValue((Property)FACING);
        return (WildMufengMushroomBlock.isAllowedAttachmentDirection(facing) || WildMufengMushroomBlock.isLegacyTopFacing(facing)) && WildMufengMushroomBlock.canAttachTo(level.getBlockState(WildMufengMushroomBlock.getSupportPos(pos, facing)));
    }

    @Override
    public boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return WildMufengMushroomBlock.canAttachTo(state);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{FACING});
    }

    public static boolean isAllowedAttachmentDirection(Direction direction) {
        return direction != Direction.DOWN;
    }

    private static boolean isLegacyTopFacing(Direction direction) {
        return direction == Direction.DOWN;
    }

    private static BlockPos getSupportPos(BlockPos pos, Direction facing) {
        return WildMufengMushroomBlock.isLegacyTopFacing(facing) ? pos.below() : pos.relative(facing.getOpposite());
    }

    public static boolean canAttachTo(BlockState state) {
        return WildMufengMushroomBlock.isStrippedWoodAttachment(state) || state.is(BlockTags.PLANKS) || state.is(Blocks.COBBLESTONE) || state.is(Blocks.MOSSY_COBBLESTONE) || state.is(Blocks.WHITE_GLAZED_TERRACOTTA) || state.is(Blocks.ORANGE_GLAZED_TERRACOTTA) || state.is(Blocks.MAGENTA_GLAZED_TERRACOTTA) || state.is(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA) || state.is(Blocks.YELLOW_GLAZED_TERRACOTTA) || state.is(Blocks.LIME_GLAZED_TERRACOTTA) || state.is(Blocks.PINK_GLAZED_TERRACOTTA) || state.is(Blocks.GRAY_GLAZED_TERRACOTTA) || state.is(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA) || state.is(Blocks.CYAN_GLAZED_TERRACOTTA) || state.is(Blocks.PURPLE_GLAZED_TERRACOTTA) || state.is(Blocks.BLUE_GLAZED_TERRACOTTA) || state.is(Blocks.BROWN_GLAZED_TERRACOTTA) || state.is(Blocks.GREEN_GLAZED_TERRACOTTA) || state.is(Blocks.RED_GLAZED_TERRACOTTA) || state.is(Blocks.BLACK_GLAZED_TERRACOTTA) || WildMufengMushroomBlock.isTerracotta(state);
    }

    public static boolean isStrippedWoodAttachment(BlockState state) {
        return state.is(BlockTags.LOGS);
    }

    private static boolean isTerracotta(BlockState state) {
        return state.is(Blocks.TERRACOTTA) || state.is(Blocks.WHITE_TERRACOTTA) || state.is(Blocks.ORANGE_TERRACOTTA) || state.is(Blocks.MAGENTA_TERRACOTTA) || state.is(Blocks.LIGHT_BLUE_TERRACOTTA) || state.is(Blocks.YELLOW_TERRACOTTA) || state.is(Blocks.LIME_TERRACOTTA) || state.is(Blocks.PINK_TERRACOTTA) || state.is(Blocks.GRAY_TERRACOTTA) || state.is(Blocks.LIGHT_GRAY_TERRACOTTA) || state.is(Blocks.CYAN_TERRACOTTA) || state.is(Blocks.PURPLE_TERRACOTTA) || state.is(Blocks.BLUE_TERRACOTTA) || state.is(Blocks.BROWN_TERRACOTTA) || state.is(Blocks.GREEN_TERRACOTTA) || state.is(Blocks.RED_TERRACOTTA) || state.is(Blocks.BLACK_TERRACOTTA);
    }
}

