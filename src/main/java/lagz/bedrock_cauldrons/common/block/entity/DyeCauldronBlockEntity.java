package lagz.bedrock_cauldrons.common.block.entity;

import lagz.bedrock_cauldrons.core.registry.BCBlockEntityTypes;
import lagz.bedrock_cauldrons.core.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.Level;
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
    }
    
    public void setColor(DyeItem dyeitem) {
        this.setColor(ColorUtil.getDyeColor(dyeitem));
    }
    
    public void setColorAndUpdate(int color) {
        this.setColor(color);
        
        Level level = this.getLevel();
        assert level != null : "Level should never be null when a player is interacting with blockentity";
        level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }
    
    public boolean isSameColor(int color) {
        return this.color == color;
    }
    
    public boolean isSameColor(DyeItem dyeitem) {
        return this.isSameColor(ColorUtil.getDyeColor(dyeitem));
    }
    
    public int getColorMixResult(DyeItem dyeitem) {
        return ColorUtil.averageIntColors(this.color, ColorUtil.getDyeColor(dyeitem));
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
