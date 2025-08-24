package lagz.bedrock_cauldrons.common.block.entity;

import lagz.bedrock_cauldrons.common.block.DyeCauldronBlock;
import lagz.bedrock_cauldrons.core.registry.BCBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DyeCauldronBlockEntity extends BlockEntity {
    private int color = -1;
    
    public DyeCauldronBlockEntity(BlockPos blockpos, BlockState blockstate) {
        super(BCBlockEntityTypes.DYE_CAULDRON.get(), blockpos, blockstate);
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
        tag.putInt("color", this.color);
    }
    
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.color = tag.contains("color", CompoundTag.TAG_INT) ? tag.getInt("color") : -1;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(int color) {
        this.color = color;
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
    
    public void setColorFromDye(DyeItem dyeitem) {
        this.setColor(DyeCauldronBlock.getDyeColor(dyeitem));
    }
}
