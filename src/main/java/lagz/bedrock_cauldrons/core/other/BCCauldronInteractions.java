package lagz.bedrock_cauldrons.core.other;

import lagz.bedrock_cauldrons.common.block.BedrockCauldronBlock;
import lagz.bedrock_cauldrons.common.block.entity.DyeCauldronBlockEntity;
import lagz.bedrock_cauldrons.common.block.entity.PotionCauldronBlockEntity;
import lagz.bedrock_cauldrons.core.networking.BCNetworking;
import lagz.bedrock_cauldrons.core.registry.BCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class BCCauldronInteractions {
    public static final Map<Item, CauldronInteraction> POTION = CauldronInteraction.newInteractionMap();
    public static final Map<Item, CauldronInteraction> DYE = CauldronInteraction.newInteractionMap();
    
    public static void registerCauldronInteractions() {
        registerVanillaCauldronInteractions();
        registerPotionCauldronInteractions();
        registerDyeCauldronInteractions();
    }
    
    private static InteractionResult mixPotions(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (1.0F + level.getRandom().nextFloat() * 0.2F) * 0.7F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            for (int i = 0; i < 8; ++i) {
                ((ServerLevel) level).sendParticles(ParticleTypes.POOF, (pos.getX() + 0.5D) + (level.getRandom().nextDouble() - 0.5D) * 0.625D, pos.getY() + 5.0D / 16.0D, (pos.getZ() + 0.5D) + (level.getRandom().nextDouble() - 0.5D) * 0.625D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    
    
    public static final CauldronInteraction EMPTY_FILL_POTION = (state, level, pos, player, hand, stack) -> {
        if (!level.isClientSide) {
            Item item = stack.getItem();
            BlockState placeBlockState = BCBlocks.POTION_CAULDRON.get().defaultBlockState();
            level.setBlockAndUpdate(pos, placeBlockState);
            if (level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity entity) {
                entity.initPotionStack(stack);
                if (entity.getPotion() != Potions.WATER) {
                    BCNetworking.sendAddPotionInteractParticlesMessage(level, pos, entity.getPotionColor(), BedrockCauldronBlock.getCauldronContentHeight(placeBlockState));
                }
            }
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        
        return InteractionResult.sidedSuccess(level.isClientSide);
    };
    public static final CauldronInteraction WATER_FILL_POTION = (state, level, pos, player, hand, stack) -> {
        if (state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
            return BCCauldronInteractions.mixPotions(level, pos, player, hand, stack);
        }
        return InteractionResult.PASS;
    };
    
    public static final CauldronInteraction WATER_DYE = (state, level, pos, player, hand, stack) -> {
        Item item = stack.getItem();
        if (item instanceof DyeItem dyeitem) {
            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                
                int waterLevel = state.getValue(LayeredCauldronBlock.LEVEL);
                level.setBlockAndUpdate(pos, BCBlocks.DYE_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, waterLevel));
                if (level.getBlockEntity(pos) instanceof DyeCauldronBlockEntity entity) {
                    entity.setColorFromDye(dyeitem);
                }
                
                level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    };
    
    private static void registerVanillaCauldronInteractions() {
        CauldronInteraction.EMPTY.put(Items.POTION, (state, level, pos, player, hand, stack) -> {
            if (PotionUtils.getPotion(stack) != Potions.WATER) {
                // Custom behavior
                return EMPTY_FILL_POTION.interact(state, level, pos, player, hand, stack);
            } else {
                // Vanilla behavior
                if (!level.isClientSide) {
                    Item item = stack.getItem();
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        });
        CauldronInteraction.EMPTY.put(Items.SPLASH_POTION, EMPTY_FILL_POTION);
        CauldronInteraction.EMPTY.put(Items.LINGERING_POTION, EMPTY_FILL_POTION);
        
        CauldronInteraction.WATER.put(Items.POTION, (state, level, pos, player, hand, stack) -> {
            if (state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
                if (PotionUtils.getPotion(stack) == Potions.WATER) {
                    // Vanilla behavior
                    if (!level.isClientSide) {
                        player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                        level.setBlockAndUpdate(pos, state.cycle(LayeredCauldronBlock.LEVEL));
                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                    }
                    
                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    // Custom behavior
                    return BCCauldronInteractions.mixPotions(level, pos, player, hand, stack);
                }
            } else {
                return InteractionResult.PASS;
            }
        });
        CauldronInteraction.WATER.put(Items.SPLASH_POTION, WATER_FILL_POTION);
        CauldronInteraction.WATER.put(Items.LINGERING_POTION, WATER_FILL_POTION);
        
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof DyeItem)
                .forEach(item -> CauldronInteraction.WATER.put(item, WATER_DYE));
    }
    
    
    public static final CauldronInteraction FILL_POTION = (state, level, pos, player, hand, stack) -> {
        if (state.getValue(LayeredCauldronBlock.LEVEL) != 3 && level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity entity) {
            if (entity.potionStackEquals(stack)) {
                if (!level.isClientSide) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    BlockState placeBlockState = state.cycle(LayeredCauldronBlock.LEVEL);
                    level.setBlockAndUpdate(pos, placeBlockState);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                    if (entity.getPotion() != Potions.WATER) {
                        BCNetworking.sendAddPotionInteractParticlesMessage(level, pos, entity.getPotionColor(), BedrockCauldronBlock.getCauldronContentHeight(placeBlockState));
                    }
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return BCCauldronInteractions.mixPotions(level, pos, player, hand, stack);
            }
        }
        return InteractionResult.PASS;
    };
    
    private static void registerPotionCauldronInteractions() {
        CauldronInteraction.addDefaultInteractions(POTION);
        POTION.put(Items.GLASS_BOTTLE, (state, level, pos, player, hand, stack) -> {
            if (level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity entity) {
                if (!level.isClientSide) {
                    Item item = stack.getItem();
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, entity.createPickupStack()));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    if (entity.getPotion() != Potions.WATER) {
                        BCNetworking.sendAddPotionInteractParticlesMessage(level, pos, entity.getPotionColor(), BedrockCauldronBlock.getCauldronContentHeight(level.getBlockState(pos)));
                    }
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.PASS;
        });
        POTION.put(Items.POTION, FILL_POTION);
        POTION.put(Items.SPLASH_POTION, FILL_POTION);
        POTION.put(Items.LINGERING_POTION, FILL_POTION);
        POTION.put(Items.ARROW, (state, level, pos, player, hand, stack) -> {
            if (level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity entity) {
                if (!level.isClientSide) {
                    Item item = stack.getItem();
                    
                    int maxTippedArrows = switch (state.getValue(LayeredCauldronBlock.LEVEL)) {
                        case 2 -> 32;
                        case 3 -> 64;
                        default -> 16;
                    };
                    int tippedArrowCount = Math.min(stack.getCount(), maxTippedArrows);
                    ItemStack tippedArrowStack = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW, tippedArrowCount), entity.getPotion());
                    
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(tippedArrowCount);
                    }
                    
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, tippedArrowStack);
                    } else if (!player.getInventory().add(tippedArrowStack)) {
                        player.drop(tippedArrowStack, false);
                    }
                    
                    for (int i = 0; i < tippedArrowCount; i++) {
                        player.awardStat(Stats.ITEM_USED.get(item));
                    }
                    
                    int levelsUsed;
                    if (tippedArrowCount <= 16) {
                        levelsUsed = 1;
                    } else if (tippedArrowCount <= 32) {
                        levelsUsed = 2;
                    } else {
                        levelsUsed = 3;
                    }
                    BedrockCauldronBlock.lowerFillLevelBy(state, level, pos, levelsUsed);
                    
                    level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (entity.getPotion() != Potions.WATER) {
                        BCNetworking.sendAddPotionInteractParticlesMessage(level, pos, entity.getPotionColor(), BedrockCauldronBlock.getCauldronContentHeight(level.getBlockState(pos)));
                    }
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.PASS;
        });
    }
    
    
    public static final CauldronInteraction MIX_DYE = (state, level, pos, player, hand, stack) -> {
        Item item = stack.getItem();
        if (item instanceof DyeItem dyeitem) {
            if (level.getBlockEntity(pos) instanceof DyeCauldronBlockEntity entity && !entity.isDyeColor(dyeitem)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    
                    entity.mixDye(dyeitem);
                    
                    level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    };
    
    public static final CauldronInteraction DYEABLE_ITEM = (state, level, pos, player, hand, stack) -> {
        Item item = stack.getItem();
        if (item instanceof DyeableLeatherItem dyeableleatheritem && level.getBlockEntity(pos) instanceof DyeCauldronBlockEntity entity && dyeableleatheritem.getColor(stack) != entity.getColor()) {
            if (!level.isClientSide) {
                dyeableleatheritem.setColor(stack, entity.getColor());
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    };
    
    public static final CauldronInteraction DYE_FILL_POTION = (state, level, pos, player, hand, stack) -> {
        if (state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
            if (stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER) {
                if (!level.isClientSide) {
                    Item item = stack.getItem();
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    int dyeLevel = state.getValue(LayeredCauldronBlock.LEVEL);
                    level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, dyeLevel).cycle(LayeredCauldronBlock.LEVEL));
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return BCCauldronInteractions.mixPotions(level, pos, player, hand, stack);
        }
        return InteractionResult.PASS;
    };
    
    private static void registerDyeCauldronInteractions() {
        CauldronInteraction.addDefaultInteractions(DYE);
        
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof DyeItem)
                .forEach(item -> DYE.put(item, MIX_DYE));
        
        DYE.put(Items.LEATHER_BOOTS, DYEABLE_ITEM);
        DYE.put(Items.LEATHER_LEGGINGS, DYEABLE_ITEM);
        DYE.put(Items.LEATHER_CHESTPLATE, DYEABLE_ITEM);
        DYE.put(Items.LEATHER_HELMET, DYEABLE_ITEM);
        DYE.put(Items.LEATHER_HORSE_ARMOR, DYEABLE_ITEM);
        
        DYE.put(Items.BUCKET, (state, level, pos, player, hand, stack) ->
                CauldronInteraction.fillBucket(state, level, pos, player, hand, stack,
                        new ItemStack(Items.WATER_BUCKET),
                        blockstate -> blockstate.getValue(LayeredCauldronBlock.LEVEL) == 3,
                        SoundEvents.BUCKET_FILL));
        
        DYE.put(Items.GLASS_BOTTLE, (state, level, pos, player, hand, stack) -> {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        });
        
        DYE.put(Items.POTION, DYE_FILL_POTION);
        DYE.put(Items.SPLASH_POTION, DYE_FILL_POTION);
        DYE.put(Items.LINGERING_POTION, DYE_FILL_POTION);
    }
}
