package lagz.bedrock_cauldrons.common.block.entity;

import lagz.bedrock_cauldrons.core.registry.BCBlockEntityTypes;
import lagz.bedrock_cauldrons.core.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class PotionCauldronBlockEntity extends BlockEntity {
    protected static final Supplier<ItemStack> DEFAULT_POTION_STACK = () -> PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
    
    private ItemStack potionStack = DEFAULT_POTION_STACK.get();
    
    public PotionCauldronBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BCBlockEntityTypes.POTION_CAULDRON.get(), p_155229_, p_155230_);
    }
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("potion", this.potionStack.save(new CompoundTag()));
    }
    
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("potion", Tag.TAG_COMPOUND)) {
            ItemStack itemstack = ItemStack.of(tag.getCompound("potion"));
            if (!itemstack.isEmpty()) {
                this.potionStack = itemstack;
            }
        }
    }
    
    public Potion getPotion() {
        return PotionUtils.getPotion(this.potionStack);
    }
    
    public int getPotionColor() {
        return PotionUtils.getColor(this.potionStack);
    }
    
    public boolean isUncoloredWater() {
        if (this.getPotion() != Potions.WATER) {
            return false;
        }
        
        CompoundTag tag = this.potionStack.getTag();
        if (tag != null) {
            if (tag.contains(PotionUtils.TAG_CUSTOM_POTION_COLOR, Tag.TAG_ANY_NUMERIC)) {
                return false;
            }
            if (tag.contains(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, Tag.TAG_LIST)) {
                return tag.getList(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, Tag.TAG_COMPOUND).isEmpty();
            }
        }
        return true;
    }
    
    public static boolean hasCustomEffectsOrColor(ItemStack itemstack) {
        CompoundTag tag = itemstack.getTag();
        if (tag == null) {
            return false;
        }
        
        if (tag.contains(PotionUtils.TAG_CUSTOM_POTION_COLOR, Tag.TAG_ANY_NUMERIC)) {
            return true;
        }
        if (tag.contains(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, Tag.TAG_LIST)) {
            return !tag.getList(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, Tag.TAG_COMPOUND).isEmpty();
        }
        return false;
    }
    
    public boolean isWaterWithoutEffects() {
        return this.getPotion() == Potions.WATER && PotionUtils.getMobEffects(this.potionStack).isEmpty();
    }
    
    public boolean hasParticles() {
        return !this.isWaterWithoutEffects();
    }
    
    protected void setPotionStack(ItemStack stack) {
        this.potionStack = stack;
        this.setChanged();
    }
    
    public void initPotionStack(ItemStack initStack) {
        ItemStack setStack = PotionUtils.setPotion(new ItemStack(initStack.getItem()), PotionUtils.getPotion(initStack));
        
        CompoundTag initTag = initStack.getTag();
        if (initTag != null) {
            if (initTag.contains(PotionUtils.TAG_CUSTOM_POTION_COLOR, Tag.TAG_ANY_NUMERIC)) {
                setStack.getOrCreateTag().putInt(PotionUtils.TAG_CUSTOM_POTION_COLOR, initTag.getInt(PotionUtils.TAG_CUSTOM_POTION_COLOR));
            }
            if (initTag.contains(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, Tag.TAG_LIST)) {
                ListTag customEffects = initTag.getList(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, Tag.TAG_COMPOUND);
                if (!customEffects.isEmpty()) {
                    setStack.getOrCreateTag().put(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, customEffects.copy());
                }
            }
        }
        
        this.setPotionStack(setStack);
    }
    
    public void initRandomSwampHutPotionStack(RandomSource random) {
        this.setPotionStack(createRandomSwampHutPotionStack(random));
    }
    
    public ItemStack createPickupStack() {
        return this.potionStack.copyWithCount(1);
    }
    
    public static ItemStack createRandomSwampHutPotionStack(RandomSource random) {
        Item item = random.nextInt(2) == 0 ? Items.POTION : Items.SPLASH_POTION;
        ItemStack potionStack = new ItemStack(item);
        
        List<Potion> potions = ForgeRegistries.POTIONS.getValues().stream()
                .filter(potion ->
                        !potion.getEffects().isEmpty() &&
                                PotionBrewing.isBrewablePotion(potion))
                .toList();
        Potion potion = potions.get(random.nextInt(potions.size()));
        
        return PotionUtils.setPotion(potionStack, potion);
    }
    
    public boolean potionStackEquals(ItemStack stack) {
        if (this.potionStack.is(stack.getItem())) {
            if (this.getPotion() == PotionUtils.getPotion(stack)) {
                CompoundTag thisTag = this.potionStack.getTag();
                CompoundTag otherTag = stack.getTag();
                
                if (TagUtil.tagsContainEqualIntTag(thisTag, otherTag, PotionUtils.TAG_CUSTOM_POTION_COLOR)) {
                    return TagUtil.tagsContainEqualListTag(thisTag, otherTag, PotionUtils.TAG_CUSTOM_POTION_EFFECTS, Tag.TAG_COMPOUND);
                }
            }
        }
        return false;
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        
        Level level = this.getLevel();
        assert level != null : "Level should never be null when a blockentity is receiving a data packet";
        if (level.isClientSide) {
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }
}
