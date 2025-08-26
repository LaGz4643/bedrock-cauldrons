package lagz.bedrock_cauldrons.common.block.entity;

import lagz.bedrock_cauldrons.core.registry.BCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
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
        this.potionStack = tag.contains("potion", CompoundTag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("potion")) : DEFAULT_POTION_STACK.get();
    }
    
    public Potion getPotion() {
        return PotionUtils.getPotion(this.potionStack);
    }
    
    public int getPotionColor() {
        return PotionUtils.getColor(this.potionStack);
    }
    
    private void setPotionStack(ItemStack stack) {
        this.potionStack = stack;
        this.setChanged();
    }
    
    public void initPotionStack(ItemStack stack) {
        this.setPotionStack(stack.copyWithCount(1));
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
            return Objects.equals(this.potionStack.getTag(), stack.getTag());
        }
        return false;
    }
    
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        if (this.getLevel().isClientSide) {
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }
}
