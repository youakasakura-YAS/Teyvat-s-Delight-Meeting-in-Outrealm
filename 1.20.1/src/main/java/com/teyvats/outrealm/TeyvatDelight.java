package com.teyvats.outrealm;

import com.google.common.collect.ImmutableSet;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.slf4j.Logger;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class TeyvatDelight implements ModInitializer {
    public static final String MODID = "teyvats_delight_meeting_in_outrealm";
    public static final Logger LOGGER = LogUtils.getLogger();

    // ============ 世界生成 Feature ============
    public static final TeyvatEntry<Feature<WildTeyvatCropPatchConfiguration>> WILD_CROP_PATCH_FEATURE = entry(() -> new WildTeyvatCropPatchFeature(WildTeyvatCropPatchConfiguration.CODEC));
    public static final TeyvatEntry<Feature<TeyvatMineralPatchConfiguration>> MINERAL_PATCH_FEATURE = entry(() -> new TeyvatMineralPatchFeature(TeyvatMineralPatchConfiguration.CODEC));

    // ============ 实体 ============
    public static final TeyvatEntry<EntityType<StarconchEntity>> STARCONCH = entry(() -> EntityType.Builder.of(StarconchEntity::new, MobCategory.CREATURE).sized(0.5f, 0.3f).clientTrackingRange(8).build("starconch"));

    // ============ 方块 ============
    public static final TeyvatEntry<Block> XUAN_CI_JADE_FIELD = entry(() -> new TeyvatFieldBlock(BlockBehaviour.Properties.copy(Blocks.FARMLAND)));
    public static final TeyvatEntry<Block> NI_CI_ZHI_FIELD = entry(() -> new TeyvatFieldBlock(BlockBehaviour.Properties.copy(Blocks.FARMLAND)));
    public static final TeyvatEntry<Block> CHU_CI_ZHU_FIELD = entry(() -> new TeyvatWaterFieldBlock(BlockBehaviour.Properties.copy(Blocks.MOSSY_COBBLESTONE).noOcclusion()));
    public static final TeyvatEntry<Block> XUAN_CI_PU_FIELD = entry(() -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).lightLevel(state -> 7)));
    public static final TeyvatEntry<Block> PRIMOGEM_BLOCK = entry(() -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final TeyvatEntry<Block> MORA_BLOCK = entry(() -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));

    public static final TeyvatEntry<VillagerProfession> TEYVAT_MERCHANT_PROFESSION = entry(() -> new VillagerProfession("teyvat_merchant",
            holder -> holder.is(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, id("teyvat_merchant"))),
            holder -> holder.is(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, id("teyvat_merchant"))),
            ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FARMER));

    public static final TeyvatEntry<ShipoCropBlock> BURIED_SHIPO_FRAGMENT = entry(() -> new ShipoCropBlock(shipoCropProperties(), 0, () -> (Block) TeyvatDelight.REPAIRING_SHIPO.get(), 5.0));
    public static final TeyvatEntry<ShipoCropBlock> REPAIRING_SHIPO = entry(() -> new ShipoCropBlock(shipoCropProperties(), 1, () -> (Block) TeyvatDelight.INTACT_SHIPO.get(), 6.0));
    public static final TeyvatEntry<ShipoCropBlock> INTACT_SHIPO = entry(() -> new ShipoCropBlock(shipoCropProperties(), 2, null, 8.0));
    public static final TeyvatEntry<NaturalShipoBlock> NATURAL_SHIPO = entry(() -> new NaturalShipoBlock(shipoBlockProperties()));

    public static final TeyvatEntry<ShipoCropBlock> BURIED_JINGHUAGUSUI_FRAGMENT = entry(() -> new ShipoCropBlock(shipoCropProperties(), 0, () -> (Block) TeyvatDelight.REPAIRING_JINGHUAGUSUI.get(), (Supplier<? extends ItemLike>) TeyvatDelight.JINGHUAGUSUI, 5.0));
    public static final TeyvatEntry<ShipoCropBlock> REPAIRING_JINGHUAGUSUI = entry(() -> new ShipoCropBlock(shipoCropProperties(), 1, () -> (Block) TeyvatDelight.INTACT_JINGHUAGUSUI.get(), (Supplier<? extends ItemLike>) TeyvatDelight.JINGHUAGUSUI, 6.0));
    public static final TeyvatEntry<ShipoCropBlock> INTACT_JINGHUAGUSUI = entry(() -> new ShipoCropBlock(shipoCropProperties(), 2, null, (Supplier<? extends ItemLike>) TeyvatDelight.JINGHUAGUSUI, 8.0));
    public static final TeyvatEntry<NaturalJinghuagusuiBlock> NATURAL_JINGHUAGUSUI = entry(() -> new NaturalJinghuagusuiBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion().lightLevel(state -> 7)));

    public static final TeyvatEntry<ShipoCropBlock> BURIED_YEBOSHI_FRAGMENT = entry(() -> new ShipoCropBlock(shipoCropProperties(), 0, () -> (Block) TeyvatDelight.REPAIRING_YEBOSHI.get(), (Supplier<? extends ItemLike>) TeyvatDelight.YEBOSHI, 8.0));
    public static final TeyvatEntry<ShipoCropBlock> REPAIRING_YEBOSHI = entry(() -> new ShipoCropBlock(shipoCropProperties(), 1, () -> (Block) TeyvatDelight.INTACT_YEBOSHI.get(), (Supplier<? extends ItemLike>) TeyvatDelight.YEBOSHI, 6.0));
    public static final TeyvatEntry<ShipoCropBlock> INTACT_YEBOSHI = entry(() -> new ShipoCropBlock(shipoCropProperties(), 2, null, (Supplier<? extends ItemLike>) TeyvatDelight.YEBOSHI, 9.0));
    public static final TeyvatEntry<NaturalNoctilucousJadeBlock> NATURAL_YEBOSHI = entry(() -> new NaturalNoctilucousJadeBlock(shipoBlockProperties(), (Supplier<? extends ItemLike>) TeyvatDelight.YEBOSHI, false));
    public static final TeyvatEntry<NaturalNoctilucousJadeBlock> NATURAL_DEEPSLATE_YEBOSHI = entry(() -> new NaturalNoctilucousJadeBlock(shipoBlockProperties(), (Supplier<? extends ItemLike>) TeyvatDelight.YEBOSHI, true));

    public static final TeyvatEntry<SmallLampGrassCropBlock> SMALL_LAMP_GRASS_CROP = entry(() -> new SmallLampGrassCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).lightLevel(SmallLampGrassCropBlock::getLightEmission)));
    public static final TeyvatEntry<WindwheelAsterCropBlock> WINDWHEEL_ASTER_CROP = entry(() -> new WindwheelAsterCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<CallaLilyCropBlock> CALLA_LILY_CROP = entry(() -> new CallaLilyCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<MintCropBlock> MINT_CROP = entry(() -> new MintCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<JueyunChiliCropBlock> JUEYUN_CHILI_CROP = entry(() -> new JueyunChiliCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<GlazeLilyCropBlock> GLAZE_LILY_CROP = entry(() -> new GlazeLilyCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<MufengMushroomCropBlock> MUFENG_MUSHROOM_CROP = entry(() -> new MufengMushroomCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<SumeruRoseCropBlock> SUMERU_ROSE_CROP = entry(() -> new SumeruRoseCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<GrainfruitCropBlock> GRAINFRUIT_CROP = entry(() -> new GrainfruitCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<FluorescentFungusCropBlock> FLUORESCENT_FUNGUS_CROP = entry(() -> new FluorescentFungusCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).lightLevel(FluorescentFungusCropBlock::getLightEmission)));
    public static final TeyvatEntry<SeaGanodermaCropBlock> SEA_GANODERMA_CROP = entry(() -> new SeaGanodermaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).lightLevel(SeaGanodermaCropBlock::getLightEmission)));
    public static final TeyvatEntry<DendrobiumCropBlock> DENDROBIUM_CROP = entry(() -> new DendrobiumCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<FrostlampFlowerCropBlock> FROSTLAMP_FLOWER_CROP = entry(() -> new FrostlampFlowerCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<GrowingCoralShellBlock> GROWING_CORAL_SHELL = entry(() -> new GrowingCoralShellBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).randomTicks().sound(SoundType.BONE_BLOCK)));
    public static final TeyvatEntry<PearlBearingCoralShellBlock> PEARL_BEARING_CORAL_SHELL = entry(() -> new PearlBearingCoralShellBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).lightLevel(state -> 7).sound(SoundType.BONE_BLOCK)));
    public static final TeyvatEntry<NaturalCoralPearlBlock> NATURAL_CORAL_PEARL = entry(() -> new NaturalCoralPearlBlock(BlockBehaviour.Properties.copy(Blocks.SEAGRASS).lightLevel(state -> 7).sound(SoundType.BONE_BLOCK)));
    public static final TeyvatEntry<HorsetailBottomBlock> HORSETAIL_BOTTOM = entry(() -> new HorsetailBottomBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).randomTicks()));
    public static final TeyvatEntry<HorsetailTopBlock> HORSETAIL_TOP = entry(() -> new HorsetailTopBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).randomTicks()));
    public static final TeyvatEntry<ThirstingJinxinFlowerBlock> THIRSTING_JINXIN_FLOWER = entry(() -> new ThirstingJinxinFlowerBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final TeyvatEntry<BlazingJinxinFlowerBlock> BLAZING_JINXIN_FLOWER = entry(() -> new BlazingJinxinFlowerBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).randomTicks().lightLevel(state -> 7)));
    public static final TeyvatEntry<BurntOutJinxinFlowerBlock> BURNT_OUT_JINXIN_FLOWER = entry(() -> new BurntOutJinxinFlowerBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static final TeyvatEntry<WildSmallLampGrassBlock> WILD_SMALL_LAMP_GRASS = entry(() -> new WildSmallLampGrassBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(state -> 7)));
    public static final TeyvatEntry<WildWindwheelAsterBlock> WILD_WINDWHEEL_ASTER = entry(() -> new WildWindwheelAsterBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildCallaLilyBlock> WILD_CALLA_LILY = entry(() -> new WildCallaLilyBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildMintBlock> WILD_MINT = entry(() -> new WildMintBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildJueyunChiliBlock> WILD_JUEYUN_CHILI = entry(() -> new WildJueyunChiliBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildGlazeLilyBlock> WILD_GLAZE_LILY = entry(() -> new WildGlazeLilyBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildMufengMushroomBlock> WILD_MUFENG_MUSHROOM = entry(() -> new WildMufengMushroomBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildSumeruRoseBlock> WILD_SUMERU_ROSE = entry(() -> new WildSumeruRoseBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildGrainfruitBlock> WILD_GRAINFRUIT = entry(() -> new WildGrainfruitBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildHorsetailBlock> WILD_HORSETAIL = entry(() -> new WildHorsetailBlock(BlockBehaviour.Properties.copy(Blocks.SEAGRASS)));
    public static final TeyvatEntry<WildJinxinFlowerBlock> WILD_JINXIN_FLOWER = entry(() -> new WildJinxinFlowerBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(state -> 7)));
    public static final TeyvatEntry<WildFluorescentFungusBlock> WILD_FLUORESCENT_FUNGUS = entry(() -> new WildFluorescentFungusBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(state -> 7)));
    public static final TeyvatEntry<WildSeaGanodermaBlock> WILD_SEA_GANODERMA = entry(() -> new WildSeaGanodermaBlock(BlockBehaviour.Properties.copy(Blocks.SEAGRASS).lightLevel(state -> 7)));
    public static final TeyvatEntry<WildDendrobiumBlock> WILD_DENDROBIUM = entry(() -> new WildDendrobiumBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final TeyvatEntry<WildFrostlampFlowerBlock> WILD_FROSTLAMP_FLOWER = entry(() -> new WildFrostlampFlowerBlock(BlockBehaviour.Properties.copy(Blocks.POPPY)));

    public static final TeyvatEntry<YunyanCrackleafCropBlock> YUNYAN_LIEYE_CROP = entry(() -> new YunyanCrackleafCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks()));
    public static final TeyvatEntry<WildYunyanCrackleafBlock> WILD_YUNYAN_LIEYE = entry(() -> new WildYunyanCrackleafBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH).noCollission().instabreak().sound(SoundType.GRASS)));

    // ============ 物品 ============
    public static final TeyvatEntry<Item> YUNYAN_LIEYE = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> YUNYAN_LIEYE_SEEDS = entry(() -> new ItemNameBlockItem((Block) YUNYAN_LIEYE_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_YUNYAN_LIEYE_ITEM = entry(() -> new BlockItem((Block) WILD_YUNYAN_LIEYE.get(), new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> CORAL_SHELL = entry(() -> new ItemNameBlockItem((Block) GROWING_CORAL_SHELL.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> CORAL_PEARL = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<BlockItem> NATURAL_CORAL_PEARL_ITEM = entry(() -> new BlockItem((Block) NATURAL_CORAL_PEARL.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> HORSETAIL = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> HORSETAIL_SEEDS = entry(() -> new ItemNameBlockItem((Block) HORSETAIL_BOTTOM.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> JINXIN_FLOWER = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> JINXIN_FLOWER_BUD = entry(() -> new ItemNameBlockItem((Block) THIRSTING_JINXIN_FLOWER.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> PEPPER = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> SALT = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> CHENYU_TEA = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> TOFU = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> GLABROUS_BEANS = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> SHRIMP_MEAT = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> ALMOND = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> MATSUTAKE = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> CRAB = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> SAUSAGE = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> MORA = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<Item> PRIMOGEM = entry(() -> new Item(new Item.Properties()));

    public static final TeyvatEntry<BlockItem> XUAN_CI_JADE_FIELD_ITEM = entry(() -> new BlockItem((Block) XUAN_CI_JADE_FIELD.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> NI_CI_ZHI_FIELD_ITEM = entry(() -> new BlockItem((Block) NI_CI_ZHI_FIELD.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> CHU_CI_ZHU_FIELD_ITEM = entry(() -> new BlockItem((Block) CHU_CI_ZHU_FIELD.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> XUAN_CI_PU_FIELD_ITEM = entry(() -> new BlockItem((Block) XUAN_CI_PU_FIELD.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> NATURAL_SHIPO_ITEM = entry(() -> new BlockItem((Block) NATURAL_SHIPO.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> NATURAL_YEBOSHI_ITEM = entry(() -> new BlockItem((Block) NATURAL_YEBOSHI.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> NATURAL_DEEPSLATE_YEBOSHI_ITEM = entry(() -> new BlockItem((Block) NATURAL_DEEPSLATE_YEBOSHI.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> NATURAL_JINGHUAGUSUI_ITEM = entry(() -> new BlockItem((Block) NATURAL_JINGHUAGUSUI.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_SMALL_LAMP_GRASS_ITEM = entry(() -> new BlockItem((Block) WILD_SMALL_LAMP_GRASS.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_WINDWHEEL_ASTER_ITEM = entry(() -> new BlockItem((Block) WILD_WINDWHEEL_ASTER.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_CALLA_LILY_ITEM = entry(() -> new BlockItem((Block) WILD_CALLA_LILY.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_MINT_ITEM = entry(() -> new BlockItem((Block) WILD_MINT.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_JUEYUN_CHILI_ITEM = entry(() -> new BlockItem((Block) WILD_JUEYUN_CHILI.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_GLAZE_LILY_ITEM = entry(() -> new BlockItem((Block) WILD_GLAZE_LILY.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_MUFENG_MUSHROOM_ITEM = entry(() -> new BlockItem((Block) WILD_MUFENG_MUSHROOM.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_SUMERU_ROSE_ITEM = entry(() -> new BlockItem((Block) WILD_SUMERU_ROSE.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_GRAINFRUIT_ITEM = entry(() -> new BlockItem((Block) WILD_GRAINFRUIT.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_HORSETAIL_ITEM = entry(() -> new BlockItem((Block) WILD_HORSETAIL.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_JINXIN_FLOWER_ITEM = entry(() -> new BlockItem((Block) WILD_JINXIN_FLOWER.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_FLUORESCENT_FUNGUS_ITEM = entry(() -> new BlockItem((Block) WILD_FLUORESCENT_FUNGUS.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_SEA_GANODERMA_ITEM = entry(() -> new BlockItem((Block) WILD_SEA_GANODERMA.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_DENDROBIUM_ITEM = entry(() -> new BlockItem((Block) WILD_DENDROBIUM.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> WILD_FROSTLAMP_FLOWER_ITEM = entry(() -> new BlockItem((Block) WILD_FROSTLAMP_FLOWER.get(), new Item.Properties()));

    public static final TeyvatEntry<Item> SMALL_LAMP_GRASS = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> SMALL_LAMP_GRASS_SEEDS = entry(() -> new ItemNameBlockItem((Block) SMALL_LAMP_GRASS_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> WINDWHEEL_ASTER = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> WINDWHEEL_ASTER_SEEDS = entry(() -> new ItemNameBlockItem((Block) WINDWHEEL_ASTER_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> CALLA_LILY = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> CALLA_LILY_SEEDS = entry(() -> new ItemNameBlockItem((Block) CALLA_LILY_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> MINT = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> MINT_SEEDS = entry(() -> new ItemNameBlockItem((Block) MINT_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> JUEYUN_CHILI = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> JUEYUN_CHILI_SEEDS = entry(() -> new ItemNameBlockItem((Block) JUEYUN_CHILI_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> GLAZE_LILY = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> GLAZE_LILY_SEEDS = entry(() -> new ItemNameBlockItem((Block) GLAZE_LILY_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> MUFENG_MUSHROOM = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> MUFENG_MUSHROOM_SPORES = entry(() -> new ItemNameBlockItem((Block) MUFENG_MUSHROOM_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> SUMERU_ROSE = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> SUMERU_ROSE_SEEDS = entry(() -> new ItemNameBlockItem((Block) SUMERU_ROSE_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> GRAINFRUIT = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> GRAINFRUIT_SEEDS = entry(() -> new ItemNameBlockItem((Block) GRAINFRUIT_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> FLUORESCENT_FUNGUS = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> FLUORESCENT_FUNGUS_SPORES = entry(() -> new ItemNameBlockItem((Block) FLUORESCENT_FUNGUS_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> SEA_GANODERMA = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> SEA_GANODERMA_SAMPLE = entry(() -> new ItemNameBlockItem((Block) SEA_GANODERMA_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> DENDROBIUM = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> DENDROBIUM_SEEDS = entry(() -> new ItemNameBlockItem((Block) DENDROBIUM_CROP.get(), new Item.Properties()));
    public static final TeyvatEntry<Item> FROSTLAMP_FLOWER = entry(() -> new Item(new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> FROSTLAMP_FLOWER_SEEDS = entry(() -> new ItemNameBlockItem((Block) FROSTLAMP_FLOWER_CROP.get(), new Item.Properties()));

    public static final TeyvatEntry<BlockItem> PRIMOGEM_BLOCK_ITEM = entry(() -> new BlockItem((Block) PRIMOGEM_BLOCK.get(), new Item.Properties()));
    public static final TeyvatEntry<BlockItem> MORA_BLOCK_ITEM = entry(() -> new BlockItem((Block) MORA_BLOCK.get(), new Item.Properties()));
    public static final TeyvatEntry<StarconchItem> STARCONCH_ITEM = entry(() -> new StarconchItem(new Item.Properties()));

    private static final Tier PRIMOGEM_KNIFE_TIER = new Tier() {
        @Override
        public int getUses() {
            return Tiers.NETHERITE.getUses();
        }

        @Override
        public float getSpeed() {
            return Tiers.NETHERITE.getSpeed();
        }

        @Override
        public float getAttackDamageBonus() {
            return Tiers.NETHERITE.getAttackDamageBonus();
        }

        @Override
        public int getLevel() {
            return Tiers.NETHERITE.getLevel();
        }

        @Override
        public int getEnchantmentValue() {
            return Tiers.NETHERITE.getEnchantmentValue();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(PRIMOGEM.get());
        }
    };
    public static final TeyvatEntry<KnifeItem> PRIMOGEM_KNIFE = entry(() -> new KnifeItem(PRIMOGEM_KNIFE_TIER, 6.0f, -2.0f, new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> SHIPO = entry(() -> new ItemNameBlockItem((Block) BURIED_SHIPO_FRAGMENT.get(), new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> JINGHUAGUSUI = entry(() -> new ItemNameBlockItem((Block) BURIED_JINGHUAGUSUI_FRAGMENT.get(), new Item.Properties()));
    public static final TeyvatEntry<ItemNameBlockItem> YEBOSHI = entry(() -> new ItemNameBlockItem((Block) BURIED_YEBOSHI_FRAGMENT.get(), new Item.Properties()));
    public static final TeyvatEntry<SeedDispensaryItem> SEED_DISPENSARY = entry(() -> new SeedDispensaryItem(new Item.Properties().stacksTo(1)));

    // ============ 菜品 ============
    public static final TeyvatEntry<TeyvatDishItem> TEYVAT_FRIED_EGG = registerDish("teyvat_fried_egg", new Item.Properties().stacksTo(16).food(dishFood(2.0f, 3.0f)));
    public static final TeyvatEntry<TeyvatDishItem> TEYVAT_SCORCHED_EGG = registerDish("teyvat_scorched_egg", new Item.Properties().stacksTo(16).food(dishFoodBuilder(3.0f, 4.0f).effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> MUSHROOM_CHICKEN_SKEWER = registerDish("mushroom_chicken_skewer", new Item.Properties().stacksTo(16).food(dishFood(3.0f, 4.0f)));
    public static final TeyvatEntry<TeyvatDishItem> FRUITY_SKEWERS = registerDish("fruity_skewers", new Item.Properties().stacksTo(16).food(dishFoodBuilder(4.0f, 5.0f).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> GRILLED_STEAK = registerDish("grilled_steak", new Item.Properties().stacksTo(16).food(dishFood(4.5f, 7.0f)));
    public static final TeyvatEntry<TeyvatDishItem> OUTRIDERS_CHAMPION_STEAK = registerDish("outriders_champion_steak", new Item.Properties().stacksTo(16).food(dishFoodBuilder(5.5f, 8.0f).effect(new MobEffectInstance(MobEffects.JUMP, 400, 1), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> STIR_FRIED_FILET = registerDish("stir_fried_filet", new Item.Properties().stacksTo(16).food(dishFoodBuilder(3.0f, 4.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> RADISH_VEGGIE_SOUP = registerDish("radish_veggie_soup", new Item.Properties().stacksTo(16).food(dishFoodBuilder(2.0f, 3.0f).effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> MONDSTADT_GRILLED_FISH = registerDish("mondstadt_grilled_fish", new Item.Properties().stacksTo(16).food(dishFood(4.0f, 5.5f)));
    public static final TeyvatEntry<TeyvatDishItem> MORA_MEAT = registerDish("mora_meat", new Item.Properties().stacksTo(16).food(dishFood(3.0f, 4.0f)));
    public static final TeyvatEntry<TeyvatDishItem> LONG_NIGHT_FLAME = registerDish("long_night_flame", dishItem(dishFoodBuilder(4.0f, 5.0f).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 12000, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> SMOKED_FISH_STEAK = registerDish("smoked_fish_steak", dishItem(dishFoodBuilder(3.0f, 4.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> CATCH_OF_THE_DARK_DEPTHS = registerDish("catch_of_the_dark_depths", dishItem(dishFoodBuilder(4.0f, 6.5f).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 4), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> DARK_EGG = registerDish("dark_egg", dishItem(dishFoodBuilder(5.0f, 6.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> MINT_SAUCE_GRILLED_FISH = registerDish("mint_sauce_grilled_fish", dishItem(dishFoodBuilder(3.0f, 5.5f).build()));
    public static final TeyvatEntry<TeyvatDishItem> GRAINFRUIT_CUP = registerDish("grainfruit_cup", dishItem(dishFood(3.0f, 4.0f)));
    public static final TeyvatEntry<TeyvatDishItem> JADE_PATTERN_TEA_EGG = registerDish("jade_pattern_tea_egg", dishItem(dishFoodBuilder(3.0f, 4.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> CHENYU_TEA_BREW = registerDish("chenyu_tea_brew", dishItem(dishFoodBuilder(1.0f, 2.0f).effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> LEISURE_TEA = registerDish("leisure_tea", dishItem(dishFoodBuilder(5.0f, 6.5f).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> HONEY_CHAR_SIU = registerDish("honey_char_siu", dishItem(dishFoodBuilder(4.0f, 5.5f).build()));
    public static final TeyvatEntry<TeyvatDishItem> ALMOND_TROUT = registerDish("almond_trout", dishItem(dishFoodBuilder(3.0f, 6.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> CONFIT_DUCK_LEG = registerDish("confit_duck_leg", dishItem(dishFoodBuilder(4.0f, 5.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> MINT_BEAN_SOUP = registerDish("mint_bean_soup", dishItem(dishFoodBuilder(3.0f, 5.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> LAMBAD_FISH_ROLL = registerDish("lambad_fish_roll", dishItem(dishFoodBuilder(3.0f, 4.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> WARMTH = registerDish("warmth", dishItem(dishFoodBuilder(3.0f, 6.0f).effect(new MobEffectInstance(MobEffects.REGENERATION, 400, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> SOBA_NOODLES = registerDish("soba_noodles", dishItem(dishFoodBuilder(3.0f, 5.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> STRATAGEM = registerDish("stratagem", dishItem(dishFoodBuilder(8.0f, 9.0f).effect(new MobEffectInstance(MobEffects.REGENERATION, 300, 1), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> SATIETY_GEL = registerDish("satiety_gel", dishItem(dishFoodBuilder(3.0f, 4.0f).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0), 1.0f).effect(new MobEffectInstance(MobEffects.JUMP, 400, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> ETERNAL_FAITH = registerDish("eternal_faith", dishItem(dishFoodBuilder(4.0f, 5.0f).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 400, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> BIRD_EGG_YAKI = registerDish("bird_egg_yaki", dishItem(dishFoodBuilder(3.0f, 4.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> BIRD_EGG_SUSHI = registerDish("bird_egg_sushi", dishItem(dishFood(7.0f, 8.0f)));
    public static final TeyvatEntry<TeyvatDishItem> SWEET_SHRIMP_SUSHI = registerDish("sweet_shrimp_sushi", dishItem(dishFood(5.0f, 6.0f)));
    public static final TeyvatEntry<TeyvatDishItem> RAIN_OR_SHINE = registerDish("rain_or_shine", dishItem(dishFoodBuilder(4.0f, 5.0f).effect(new MobEffectInstance(MobEffects.JUMP, 300, 2), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> DRY_BRAISED_FISH = registerDish("dry_braised_fish", dishItem(dishFoodBuilder(3.0f, 4.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> MISO_SOUP = registerDish("miso_soup", dishItem(dishFoodBuilder(2.0f, 5.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> RICE_BUNS = registerDish("rice_buns", dishItem(dishFoodBuilder(4.0f, 3.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> MINT_JELLY = registerDish("mint_jelly", dishItem(dishFoodBuilder(2.0f, 3.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> DEFINITELY_NOT_BAR_FOOD = registerDish("definitely_not_bar_food", dishItem(dishFoodBuilder(5.0f, 6.5f).effect(new MobEffectInstance(MobEffects.REGENERATION, 400, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> QIANKUN_MORA_MEAT = registerDish("qiankun_mora_meat", dishItem(dishFoodBuilder(4.0f, 5.0f).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> GRILLED_TIGER_FISH = registerDish("grilled_tiger_fish", dishItem(dishFood(4.0f, 5.5f)));
    public static final TeyvatEntry<TeyvatDishItem> LARGE_BOWL_OF_TEA = registerDish("large_bowl_of_tea", dishItem(dishFood(0.5f, 1.0f)));
    public static final TeyvatEntry<TeyvatDishItem> FLAMING_STIR_FRIED_FILET = registerDish("flaming_stir_fried_filet", dishItem(dishFoodBuilder(4.0f, 5.0f).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> SURVIVAL_GRILLED_FISH = registerDish("survival_grilled_fish", dishItem(dishFoodBuilder(4.0f, 7.0f).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1), 1.0f).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 400, 1), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> XIAN_TIAO_QIANG = registerGoldDish("xian_tiao_qiang", dishItem(dishFoodBuilder(10.0f, 16.0f).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1), 1.0f).build()));
    public static final TeyvatEntry<TeyvatDishItem> OLD_COURTYARD_SMOKED_SAUSAGE = registerDish("old_courtyard_smoked_sausage", dishItem(dishFoodBuilder(3.0f, 4.0f).build()));

    // ============ 创造模式标签页 ============
    public static final TeyvatEntry<CreativeModeTab> TEYVAT_DELIGHT_TAB = entry(() -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.teyvats_delight_meeting_in_outrealm")).icon(() -> PRIMOGEM_KNIFE.get().getDefaultInstance()).displayItems((parameters, output) -> {
        output.accept(XUAN_CI_JADE_FIELD_ITEM.get());
        output.accept(NI_CI_ZHI_FIELD_ITEM.get());
        output.accept(CHU_CI_ZHU_FIELD_ITEM.get());
        output.accept(XUAN_CI_PU_FIELD_ITEM.get());
        output.accept(SHIPO.get());
        output.accept(NATURAL_SHIPO_ITEM.get());
        output.accept(YEBOSHI.get());
        output.accept(NATURAL_YEBOSHI_ITEM.get());
        output.accept(NATURAL_DEEPSLATE_YEBOSHI_ITEM.get());
        output.accept(JINGHUAGUSUI.get());
        output.accept(NATURAL_JINGHUAGUSUI_ITEM.get());
        output.accept(SMALL_LAMP_GRASS.get());
        output.accept(SMALL_LAMP_GRASS_SEEDS.get());
        output.accept(WILD_SMALL_LAMP_GRASS_ITEM.get());
        output.accept(WINDWHEEL_ASTER.get());
        output.accept(WINDWHEEL_ASTER_SEEDS.get());
        output.accept(WILD_WINDWHEEL_ASTER_ITEM.get());
        output.accept(CALLA_LILY.get());
        output.accept(CALLA_LILY_SEEDS.get());
        output.accept(WILD_CALLA_LILY_ITEM.get());
        output.accept(MINT.get());
        output.accept(MINT_SEEDS.get());
        output.accept(WILD_MINT_ITEM.get());
        output.accept(JUEYUN_CHILI.get());
        output.accept(JUEYUN_CHILI_SEEDS.get());
        output.accept(WILD_JUEYUN_CHILI_ITEM.get());
        output.accept(GLAZE_LILY.get());
        output.accept(GLAZE_LILY_SEEDS.get());
        output.accept(WILD_GLAZE_LILY_ITEM.get());
        output.accept(MUFENG_MUSHROOM.get());
        output.accept(MUFENG_MUSHROOM_SPORES.get());
        output.accept(WILD_MUFENG_MUSHROOM_ITEM.get());
        output.accept(SUMERU_ROSE.get());
        output.accept(SUMERU_ROSE_SEEDS.get());
        output.accept(WILD_SUMERU_ROSE_ITEM.get());
        output.accept(GRAINFRUIT.get());
        output.accept(GRAINFRUIT_SEEDS.get());
        output.accept(WILD_GRAINFRUIT_ITEM.get());
        output.accept(HORSETAIL.get());
        output.accept(HORSETAIL_SEEDS.get());
        output.accept(WILD_HORSETAIL_ITEM.get());
        output.accept(JINXIN_FLOWER.get());
        output.accept(JINXIN_FLOWER_BUD.get());
        output.accept(WILD_JINXIN_FLOWER_ITEM.get());
        output.accept(FLUORESCENT_FUNGUS.get());
        output.accept(FLUORESCENT_FUNGUS_SPORES.get());
        output.accept(WILD_FLUORESCENT_FUNGUS_ITEM.get());
        output.accept(SEA_GANODERMA.get());
        output.accept(SEA_GANODERMA_SAMPLE.get());
        output.accept(WILD_SEA_GANODERMA_ITEM.get());
        output.accept(DENDROBIUM.get());
        output.accept(DENDROBIUM_SEEDS.get());
        output.accept(WILD_DENDROBIUM_ITEM.get());
        output.accept(FROSTLAMP_FLOWER.get());
        output.accept(FROSTLAMP_FLOWER_SEEDS.get());
        output.accept(WILD_FROSTLAMP_FLOWER_ITEM.get());
        output.accept(YUNYAN_LIEYE.get());
        output.accept(YUNYAN_LIEYE_SEEDS.get());
        output.accept(WILD_YUNYAN_LIEYE_ITEM.get());
        output.accept(CORAL_SHELL.get());
        output.accept(CORAL_PEARL.get());
        output.accept(NATURAL_CORAL_PEARL_ITEM.get());
        output.accept(PEPPER.get());
        output.accept(SALT.get());
        output.accept(CHENYU_TEA.get());
        output.accept(TOFU.get());
        output.accept(GLABROUS_BEANS.get());
        output.accept(SHRIMP_MEAT.get());
        output.accept(ALMOND.get());
        output.accept(MATSUTAKE.get());
        output.accept(CRAB.get());
        output.accept(SAUSAGE.get());
        output.accept(MORA.get());
        output.accept(MORA_BLOCK_ITEM.get());
        output.accept(PRIMOGEM.get());
        output.accept(PRIMOGEM_BLOCK_ITEM.get());
        output.accept(STARCONCH_ITEM.get());
        output.accept(PRIMOGEM_KNIFE.get());
        output.accept(SEED_DISPENSARY.get());
        output.accept(MUSHROOM_CHICKEN_SKEWER.get());
        output.accept(FRUITY_SKEWERS.get());
        output.accept(TEYVAT_FRIED_EGG.get());
        output.accept(GRILLED_STEAK.get());
        output.accept(RADISH_VEGGIE_SOUP.get());
        output.accept(MONDSTADT_GRILLED_FISH.get());
        output.accept(MORA_MEAT.get());
        output.accept(STIR_FRIED_FILET.get());
        output.accept(SURVIVAL_GRILLED_FISH.get());
        output.accept(TEYVAT_SCORCHED_EGG.get());
        output.accept(OUTRIDERS_CHAMPION_STEAK.get());
        output.accept(FLAMING_STIR_FRIED_FILET.get());
        output.accept(LARGE_BOWL_OF_TEA.get());
        output.accept(GRILLED_TIGER_FISH.get());
        output.accept(QIANKUN_MORA_MEAT.get());
        output.accept(DEFINITELY_NOT_BAR_FOOD.get());
        output.accept(MINT_JELLY.get());
        output.accept(RICE_BUNS.get());
        output.accept(HONEY_CHAR_SIU.get());
        output.accept(LEISURE_TEA.get());
        output.accept(CHENYU_TEA_BREW.get());
        output.accept(JADE_PATTERN_TEA_EGG.get());
        output.accept(BIRD_EGG_YAKI.get());
        output.accept(MISO_SOUP.get());
        output.accept(DRY_BRAISED_FISH.get());
        output.accept(RAIN_OR_SHINE.get());
        output.accept(SWEET_SHRIMP_SUSHI.get());
        output.accept(BIRD_EGG_SUSHI.get());
        output.accept(ETERNAL_FAITH.get());
        output.accept(SATIETY_GEL.get());
        output.accept(STRATAGEM.get());
        output.accept(SOBA_NOODLES.get());
        output.accept(WARMTH.get());
        output.accept(LAMBAD_FISH_ROLL.get());
        output.accept(MINT_BEAN_SOUP.get());
        output.accept(CONFIT_DUCK_LEG.get());
        output.accept(ALMOND_TROUT.get());
        output.accept(GRAINFRUIT_CUP.get());
        output.accept(MINT_SAUCE_GRILLED_FISH.get());
        output.accept(DARK_EGG.get());
        output.accept(CATCH_OF_THE_DARK_DEPTHS.get());
        output.accept(SMOKED_FISH_STEAK.get());
        output.accept(LONG_NIGHT_FLAME.get());
        output.accept(OLD_COURTYARD_SMOKED_SAUSAGE.get());
        output.accept(XIAN_TIAO_QIANG.get());
    }).build());

    // ============ Fabric 入口 ============
    @Override
    public void onInitialize() {
        // 世界生成 Feature
        Registry.register(BuiltInRegistries.FEATURE, id("wild_crop_patch"), WILD_CROP_PATCH_FEATURE.get());
        Registry.register(BuiltInRegistries.FEATURE, id("mineral_patch"), MINERAL_PATCH_FEATURE.get());

        // 实体
        Registry.register(BuiltInRegistries.ENTITY_TYPE, id("starconch"), STARCONCH.get());

        // 方块
        registerBlock("xuan_ci_jade_field", XUAN_CI_JADE_FIELD);
        registerBlock("ni_ci_zhi_field", NI_CI_ZHI_FIELD);
        registerBlock("chu_ci_zhu_field", CHU_CI_ZHU_FIELD);
        registerBlock("xuan_ci_pu_field", XUAN_CI_PU_FIELD);
        registerBlock("primogem_block", PRIMOGEM_BLOCK);
        registerBlock("mora_block", MORA_BLOCK);
        registerBlock("buried_shipo_fragment", BURIED_SHIPO_FRAGMENT);
        registerBlock("repairing_shipo", REPAIRING_SHIPO);
        registerBlock("intact_shipo", INTACT_SHIPO);
        registerBlock("natural_shipo", NATURAL_SHIPO);
        registerBlock("buried_jinghuagusui_fragment", BURIED_JINGHUAGUSUI_FRAGMENT);
        registerBlock("repairing_jinghuagusui", REPAIRING_JINGHUAGUSUI);
        registerBlock("intact_jinghuagusui", INTACT_JINGHUAGUSUI);
        registerBlock("natural_jinghuagusui", NATURAL_JINGHUAGUSUI);
        registerBlock("buried_yeboshi_fragment", BURIED_YEBOSHI_FRAGMENT);
        registerBlock("repairing_yeboshi", REPAIRING_YEBOSHI);
        registerBlock("intact_yeboshi", INTACT_YEBOSHI);
        registerBlock("natural_yeboshi", NATURAL_YEBOSHI);
        registerBlock("natural_deepslate_yeboshi", NATURAL_DEEPSLATE_YEBOSHI);
        registerBlock("small_lamp_grass_crop", SMALL_LAMP_GRASS_CROP);
        registerBlock("windwheel_aster_crop", WINDWHEEL_ASTER_CROP);
        registerBlock("calla_lily_crop", CALLA_LILY_CROP);
        registerBlock("mint_crop", MINT_CROP);
        registerBlock("jueyun_chili_crop", JUEYUN_CHILI_CROP);
        registerBlock("glaze_lily_crop", GLAZE_LILY_CROP);
        registerBlock("mufeng_mushroom_crop", MUFENG_MUSHROOM_CROP);
        registerBlock("sumeru_rose_crop", SUMERU_ROSE_CROP);
        registerBlock("grainfruit_crop", GRAINFRUIT_CROP);
        registerBlock("fluorescent_fungus_crop", FLUORESCENT_FUNGUS_CROP);
        registerBlock("sea_ganoderma_crop", SEA_GANODERMA_CROP);
        registerBlock("dendrobium_crop", DENDROBIUM_CROP);
        registerBlock("frostlamp_flower_crop", FROSTLAMP_FLOWER_CROP);
        registerBlock("growing_coral_shell", GROWING_CORAL_SHELL);
        registerBlock("pearl_bearing_coral_shell", PEARL_BEARING_CORAL_SHELL);
        registerBlock("natural_coral_pearl", NATURAL_CORAL_PEARL);
        registerBlock("horsetail_bottom", HORSETAIL_BOTTOM);
        registerBlock("horsetail_top", HORSETAIL_TOP);
        registerBlock("thirsting_jinxin_flower", THIRSTING_JINXIN_FLOWER);
        registerBlock("blazing_jinxin_flower", BLAZING_JINXIN_FLOWER);
        registerBlock("burnt_out_jinxin_flower", BURNT_OUT_JINXIN_FLOWER);
        registerBlock("wild_small_lamp_grass", WILD_SMALL_LAMP_GRASS);
        registerBlock("wild_windwheel_aster", WILD_WINDWHEEL_ASTER);
        registerBlock("wild_calla_lily", WILD_CALLA_LILY);
        registerBlock("wild_mint", WILD_MINT);
        registerBlock("wild_jueyun_chili", WILD_JUEYUN_CHILI);
        registerBlock("wild_glaze_lily", WILD_GLAZE_LILY);
        registerBlock("wild_mufeng_mushroom", WILD_MUFENG_MUSHROOM);
        registerBlock("wild_sumeru_rose", WILD_SUMERU_ROSE);
        registerBlock("wild_grainfruit", WILD_GRAINFRUIT);
        registerBlock("wild_horsetail", WILD_HORSETAIL);
        registerBlock("wild_jinxin_flower", WILD_JINXIN_FLOWER);
        registerBlock("wild_fluorescent_fungus", WILD_FLUORESCENT_FUNGUS);
        registerBlock("wild_sea_ganoderma", WILD_SEA_GANODERMA);
        registerBlock("wild_dendrobium", WILD_DENDROBIUM);
        registerBlock("wild_frostlamp_flower", WILD_FROSTLAMP_FLOWER);
        registerBlock("yunyan_lieye_crop", YUNYAN_LIEYE_CROP);
        registerBlock("wild_yunyan_lieye", WILD_YUNYAN_LIEYE);

        // 物品
        registerItem("yunyan_lieye", YUNYAN_LIEYE);
        registerItem("yunyan_lieye_seeds", YUNYAN_LIEYE_SEEDS);
        registerItem("wild_yunyan_lieye", WILD_YUNYAN_LIEYE_ITEM);
        registerItem("coral_shell", CORAL_SHELL);
        registerItem("coral_pearl", CORAL_PEARL);
        registerItem("natural_coral_pearl", NATURAL_CORAL_PEARL_ITEM);
        registerItem("horsetail", HORSETAIL);
        registerItem("horsetail_seeds", HORSETAIL_SEEDS);
        registerItem("jinxin_flower", JINXIN_FLOWER);
        registerItem("jinxin_flower_bud", JINXIN_FLOWER_BUD);
        registerItem("pepper", PEPPER);
        registerItem("salt", SALT);
        registerItem("chenyu_tea", CHENYU_TEA);
        registerItem("tofu", TOFU);
        registerItem("glabrous_beans", GLABROUS_BEANS);
        registerItem("shrimp_meat", SHRIMP_MEAT);
        registerItem("almond", ALMOND);
        registerItem("matsutake", MATSUTAKE);
        registerItem("crab", CRAB);
        registerItem("sausage", SAUSAGE);
        registerItem("mora", MORA);
        registerItem("primogem", PRIMOGEM);
        registerItem("xuan_ci_jade_field", XUAN_CI_JADE_FIELD_ITEM);
        registerItem("ni_ci_zhi_field", NI_CI_ZHI_FIELD_ITEM);
        registerItem("chu_ci_zhu_field", CHU_CI_ZHU_FIELD_ITEM);
        registerItem("xuan_ci_pu_field", XUAN_CI_PU_FIELD_ITEM);
        registerItem("natural_shipo", NATURAL_SHIPO_ITEM);
        registerItem("natural_yeboshi", NATURAL_YEBOSHI_ITEM);
        registerItem("natural_deepslate_yeboshi", NATURAL_DEEPSLATE_YEBOSHI_ITEM);
        registerItem("natural_jinghuagusui", NATURAL_JINGHUAGUSUI_ITEM);
        registerItem("wild_small_lamp_grass", WILD_SMALL_LAMP_GRASS_ITEM);
        registerItem("wild_windwheel_aster", WILD_WINDWHEEL_ASTER_ITEM);
        registerItem("wild_calla_lily", WILD_CALLA_LILY_ITEM);
        registerItem("wild_mint", WILD_MINT_ITEM);
        registerItem("wild_jueyun_chili", WILD_JUEYUN_CHILI_ITEM);
        registerItem("wild_glaze_lily", WILD_GLAZE_LILY_ITEM);
        registerItem("wild_mufeng_mushroom", WILD_MUFENG_MUSHROOM_ITEM);
        registerItem("wild_sumeru_rose", WILD_SUMERU_ROSE_ITEM);
        registerItem("wild_grainfruit", WILD_GRAINFRUIT_ITEM);
        registerItem("wild_horsetail", WILD_HORSETAIL_ITEM);
        registerItem("wild_jinxin_flower", WILD_JINXIN_FLOWER_ITEM);
        registerItem("wild_fluorescent_fungus", WILD_FLUORESCENT_FUNGUS_ITEM);
        registerItem("wild_sea_ganoderma", WILD_SEA_GANODERMA_ITEM);
        registerItem("wild_dendrobium", WILD_DENDROBIUM_ITEM);
        registerItem("wild_frostlamp_flower", WILD_FROSTLAMP_FLOWER_ITEM);
        registerItem("small_lamp_grass", SMALL_LAMP_GRASS);
        registerItem("small_lamp_grass_seeds", SMALL_LAMP_GRASS_SEEDS);
        registerItem("windwheel_aster", WINDWHEEL_ASTER);
        registerItem("windwheel_aster_seeds", WINDWHEEL_ASTER_SEEDS);
        registerItem("calla_lily", CALLA_LILY);
        registerItem("calla_lily_seeds", CALLA_LILY_SEEDS);
        registerItem("mint", MINT);
        registerItem("mint_seeds", MINT_SEEDS);
        registerItem("jueyun_chili", JUEYUN_CHILI);
        registerItem("jueyun_chili_seeds", JUEYUN_CHILI_SEEDS);
        registerItem("glaze_lily", GLAZE_LILY);
        registerItem("glaze_lily_seeds", GLAZE_LILY_SEEDS);
        registerItem("mufeng_mushroom", MUFENG_MUSHROOM);
        registerItem("mufeng_mushroom_spores", MUFENG_MUSHROOM_SPORES);
        registerItem("sumeru_rose", SUMERU_ROSE);
        registerItem("sumeru_rose_seeds", SUMERU_ROSE_SEEDS);
        registerItem("grainfruit", GRAINFRUIT);
        registerItem("grainfruit_seeds", GRAINFRUIT_SEEDS);
        registerItem("fluorescent_fungus", FLUORESCENT_FUNGUS);
        registerItem("fluorescent_fungus_spores", FLUORESCENT_FUNGUS_SPORES);
        registerItem("sea_ganoderma", SEA_GANODERMA);
        registerItem("sea_ganoderma_sample", SEA_GANODERMA_SAMPLE);
        registerItem("dendrobium", DENDROBIUM);
        registerItem("dendrobium_seeds", DENDROBIUM_SEEDS);
        registerItem("frostlamp_flower", FROSTLAMP_FLOWER);
        registerItem("frostlamp_flower_seeds", FROSTLAMP_FLOWER_SEEDS);
        registerItem("primogem_block", PRIMOGEM_BLOCK_ITEM);
        registerItem("mora_block", MORA_BLOCK_ITEM);
        registerItem("starconch", STARCONCH_ITEM);
        registerItem("primogem_knife", PRIMOGEM_KNIFE);
        registerItem("shipo", SHIPO);
        registerItem("jinghuagusui", JINGHUAGUSUI);
        registerItem("yeboshi", YEBOSHI);
        registerItem("seed_dispensary", SEED_DISPENSARY);

        // 菜品
        registerItem("teyvat_fried_egg", TEYVAT_FRIED_EGG);
        registerItem("teyvat_scorched_egg", TEYVAT_SCORCHED_EGG);
        registerItem("mushroom_chicken_skewer", MUSHROOM_CHICKEN_SKEWER);
        registerItem("fruity_skewers", FRUITY_SKEWERS);
        registerItem("grilled_steak", GRILLED_STEAK);
        registerItem("outriders_champion_steak", OUTRIDERS_CHAMPION_STEAK);
        registerItem("stir_fried_filet", STIR_FRIED_FILET);
        registerItem("radish_veggie_soup", RADISH_VEGGIE_SOUP);
        registerItem("mondstadt_grilled_fish", MONDSTADT_GRILLED_FISH);
        registerItem("mora_meat", MORA_MEAT);
        registerItem("long_night_flame", LONG_NIGHT_FLAME);
        registerItem("smoked_fish_steak", SMOKED_FISH_STEAK);
        registerItem("catch_of_the_dark_depths", CATCH_OF_THE_DARK_DEPTHS);
        registerItem("dark_egg", DARK_EGG);
        registerItem("mint_sauce_grilled_fish", MINT_SAUCE_GRILLED_FISH);
        registerItem("grainfruit_cup", GRAINFRUIT_CUP);
        registerItem("jade_pattern_tea_egg", JADE_PATTERN_TEA_EGG);
        registerItem("chenyu_tea_brew", CHENYU_TEA_BREW);
        registerItem("leisure_tea", LEISURE_TEA);
        registerItem("honey_char_siu", HONEY_CHAR_SIU);
        registerItem("almond_trout", ALMOND_TROUT);
        registerItem("confit_duck_leg", CONFIT_DUCK_LEG);
        registerItem("mint_bean_soup", MINT_BEAN_SOUP);
        registerItem("lambad_fish_roll", LAMBAD_FISH_ROLL);
        registerItem("warmth", WARMTH);
        registerItem("soba_noodles", SOBA_NOODLES);
        registerItem("stratagem", STRATAGEM);
        registerItem("satiety_gel", SATIETY_GEL);
        registerItem("eternal_faith", ETERNAL_FAITH);
        registerItem("bird_egg_yaki", BIRD_EGG_YAKI);
        registerItem("bird_egg_sushi", BIRD_EGG_SUSHI);
        registerItem("sweet_shrimp_sushi", SWEET_SHRIMP_SUSHI);
        registerItem("rain_or_shine", RAIN_OR_SHINE);
        registerItem("dry_braised_fish", DRY_BRAISED_FISH);
        registerItem("miso_soup", MISO_SOUP);
        registerItem("rice_buns", RICE_BUNS);
        registerItem("mint_jelly", MINT_JELLY);
        registerItem("definitely_not_bar_food", DEFINITELY_NOT_BAR_FOOD);
        registerItem("qiankun_mora_meat", QIANKUN_MORA_MEAT);
        registerItem("grilled_tiger_fish", GRILLED_TIGER_FISH);
        registerItem("large_bowl_of_tea", LARGE_BOWL_OF_TEA);
        registerItem("flaming_stir_fried_filet", FLAMING_STIR_FRIED_FILET);
        registerItem("survival_grilled_fish", SURVIVAL_GRILLED_FISH);
        registerItem("xian_tiao_qiang", XIAN_TIAO_QIANG);
        registerItem("old_courtyard_smoked_sausage", OLD_COURTYARD_SMOKED_SAUSAGE);

        // POI 与村民职业（PointOfInterestHelper 会同步更新 PoiTypes.TYPE_BY_STATE 缓存，确保村民识别工作方块）
        PointOfInterestHelper.register(id("teyvat_merchant"), 1, 1, MORA_BLOCK.get());
        Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, id("teyvat_merchant"), TEYVAT_MERCHANT_PROFESSION.get());

        // 创造模式标签页
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("teyvat_delight"), TEYVAT_DELIGHT_TAB.get());

        // 世界生成挂载（原 NeoForge biome_modifier → Fabric BiomeModifications）
        TeyvatWorldgen.registerAll();

        // 事件 / 实体属性与生成 / 村民交易 / 战利品注入
        TeyvatDelightEvents.registerAll();
        TeyvatDelightEntityEvents.registerAll();
        TeyvatLootModifiers.registerAll();

        LOGGER.info("提瓦特乐事 (Fabric) 已加载。");
    }

    // ============ 辅助方法 ============
    private static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }

    private static <T> TeyvatEntry<T> entry(Supplier<T> factory) {
        return new TeyvatEntry<>(factory);
    }

    private static void registerBlock(String name, TeyvatEntry<? extends Block> entry) {
        Registry.register(BuiltInRegistries.BLOCK, id(name), entry.get());
    }

    private static void registerItem(String name, TeyvatEntry<? extends Item> entry) {
        Registry.register(BuiltInRegistries.ITEM, id(name), entry.get());
    }

    private static TeyvatEntry<TeyvatDishItem> registerDish(String name, Item.Properties properties) {
        return entry(() -> new TeyvatDishItem(properties, null, 1));
    }

    private static TeyvatEntry<TeyvatDishItem> registerGoldDish(String name, Item.Properties properties) {
        return entry(() -> new TeyvatDishItem(properties, ChatFormatting.GOLD, 5));
    }

    private static Item.Properties dishItem(FoodProperties food) {
        return new Item.Properties().stacksTo(16).food(food);
    }

    private static FoodProperties dishFood(float hungerIcons, float saturationIcons) {
        return dishFoodBuilder(hungerIcons, saturationIcons).build();
    }

    private static BlockBehaviour.Properties shipoCropProperties() {
        return BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).randomTicks().noOcclusion().lightLevel(state -> 7);
    }

    private static BlockBehaviour.Properties shipoBlockProperties() {
        return BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).noOcclusion().lightLevel(state -> 7);
    }

    private static FoodProperties.Builder dishFoodBuilder(float hungerIcons, float saturationIcons) {
        return foodBuilder(Math.round(hungerIcons * 2.0f), saturationIcons * 2.0f);
    }

    private static FoodProperties food(int nutrition, float saturationPoints) {
        return foodBuilder(nutrition, saturationPoints).build();
    }

    private static FoodProperties.Builder foodBuilder(int nutrition, float saturationPoints) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationPoints / ((float) nutrition * 2.0f));
    }
}
