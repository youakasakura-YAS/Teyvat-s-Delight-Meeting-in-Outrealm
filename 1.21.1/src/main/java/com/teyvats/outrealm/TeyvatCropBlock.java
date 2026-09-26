package com.teyvats.outrealm;

import com.teyvats.outrealm.SeedDispensaryItem;
import com.teyvats.outrealm.TeyvatBonusDrops;
import com.teyvats.outrealm.TeyvatCropDropTracker;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class TeyvatCropBlock
extends CropBlock {
    private final TagKey<Block> preferredFieldTag;
    private final Supplier<? extends ItemLike> cropItem;
    private final Supplier<? extends ItemLike> seedItem;
    private final int maxAge;
    private final int harvestCount;
    private final int preferredFieldHarvestBonus;

    protected TeyvatCropBlock(BlockBehaviour.Properties properties, TagKey<Block> preferredFieldTag, Supplier<? extends ItemLike> cropItem, Supplier<? extends ItemLike> seedItem) {
        this(properties, preferredFieldTag, cropItem, seedItem, 7, 1, 1);
    }

    protected TeyvatCropBlock(BlockBehaviour.Properties properties, TagKey<Block> preferredFieldTag, Supplier<? extends ItemLike> cropItem, Supplier<? extends ItemLike> seedItem, int maxAge, int harvestCount, int preferredFieldHarvestBonus) {
        super(properties);
        this.preferredFieldTag = preferredFieldTag;
        this.cropItem = cropItem;
        this.seedItem = seedItem;
        this.maxAge = maxAge;
        this.harvestCount = harvestCount;
        this.preferredFieldHarvestBonus = preferredFieldHarvestBonus;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND) || state.is(this.preferredFieldTag);
    }

    protected ItemLike getBaseSeedId() {
        return this.seedItem.get();
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) < 9) {
            return;
        }
        int age = this.getAge(state);
        if (age >= this.getMaxAge()) {
            return;
        }
        float growthSpeed = this.isOnPreferredField((LevelReader)level, pos) ? this.getPreferredFieldGrowthSpeed(state, (BlockGetter)level, pos) : TeyvatCropBlock.getGrowthSpeed(state.getBlock(), (BlockGetter)level, (BlockPos)pos) / 5.0f;
        int chance = Math.max(1, (int)(25.0f / growthSpeed) + 1);
        if (random.nextInt(chance) == 0) {
            level.setBlock(pos, this.getStateForAge(age + 1), 2);
        }
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !this.isMaxAge(state) && this.isOnPreferredField(level, pos);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (this.isMaxAge(state)) {
            this.harvestAndReplant(level, pos, state, player);
            return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.isMaxAge(state)) {
            this.harvestAndReplant(level, pos, state, player);
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
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005f);
        TeyvatCropDropTracker.consumeSkipDrop(level, pos);
        if (!level.isClientSide && !player.isCreative()) {
            this.dropForBreak(level, pos, state, player, tool);
        }
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && !TeyvatCropDropTracker.consumeSkipDrop(level, pos)) {
            this.dropForBreak(level, pos, state, null, ItemStack.EMPTY);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public void harvestAndReplant(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            TeyvatCropBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack(this.cropItem.get(), this.getHarvestCount((LevelReader)level, pos)));
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, this.getBaseSeedId());
            TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, player.getMainHandItem());
            level.setBlock(pos, this.getStateForAge(0), 2);
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    private void dropForBreak(Level level, BlockPos pos, BlockState state, @Nullable Player player, ItemStack tool) {
        TeyvatCropBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack(this.seedItem.get()));
        if (this.isMaxAge(state)) {
            TeyvatCropBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack(this.cropItem.get(), this.getHarvestCount((LevelReader)level, pos)));
            SeedDispensaryItem.dropExtraSeedIfHeld(level, pos, player, this.getBaseSeedId());
            if (player != null) {
                TeyvatBonusDrops.dropMoraFromCrop(level, pos, player, tool);
            }
        }
    }

    private int getHarvestCount(LevelReader level, BlockPos pos) {
        return this.harvestCount + (this.isOnPreferredField(level, pos) ? this.preferredFieldHarvestBonus : 0);
    }

    private boolean isOnPreferredField(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(this.preferredFieldTag);
    }

    private float getPreferredFieldGrowthSpeed(BlockState cropState, BlockGetter level, BlockPos pos) {
        boolean verticalCrop;
        Block cropBlock = cropState.getBlock();
        float speed = 1.0f;
        BlockPos below = pos.below();
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                float soilSpeed = 0.0f;
                BlockPos soilPos = below.offset(x, 0, z);
                BlockState soilState = level.getBlockState(soilPos);
                if (soilState.is(this.preferredFieldTag)) {
                    soilSpeed = 3.0f;
                } else if (soilState.getBlock() instanceof FarmBlock) {
                    soilSpeed = soilState.getValue(FarmBlock.MOISTURE) > 0 ? 3.0f : 1.0f;
                }
                if (x != 0 || z != 0) {
                    soilSpeed /= 4.0f;
                }
                speed += soilSpeed;
            }
        }
        boolean horizontalCrop = level.getBlockState(pos.west()).is(cropBlock) || level.getBlockState(pos.east()).is(cropBlock);
        boolean bl = verticalCrop = level.getBlockState(pos.north()).is(cropBlock) || level.getBlockState(pos.south()).is(cropBlock);
        if (horizontalCrop && verticalCrop) {
            speed /= 2.0f;
        } else {
            boolean diagonalCrop;
            boolean bl2 = diagonalCrop = level.getBlockState(pos.west().north()).is(cropBlock) || level.getBlockState(pos.east().north()).is(cropBlock) || level.getBlockState(pos.east().south()).is(cropBlock) || level.getBlockState(pos.west().south()).is(cropBlock);
            if (diagonalCrop) {
                speed /= 2.0f;
            }
        }
        return speed;
    }
}

