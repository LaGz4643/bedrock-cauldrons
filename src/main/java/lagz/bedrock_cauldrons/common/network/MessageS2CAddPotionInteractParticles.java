package lagz.bedrock_cauldrons.common.network;

import lagz.bedrock_cauldrons.common.network.handler.ClientMessageHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageS2CAddPotionInteractParticles {
    private final BlockPos blockpos;
    private final int potionColor;
    private final double contentHeight;
    
    public MessageS2CAddPotionInteractParticles(BlockPos blockpos, int potionColor, double contentHeight) {
        this.blockpos = blockpos;
        this.potionColor = potionColor;
        this.contentHeight = contentHeight;
    }
    
    public MessageS2CAddPotionInteractParticles(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readDouble());
    }
    
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockpos);
        buf.writeInt(this.potionColor);
        buf.writeDouble(this.contentHeight);
    }
    
    public BlockPos getBlockPos() {
        return this.blockpos;
    }
    
    public int getPotionColor() {
        return this.potionColor;
    }
    
    public double getContentHeight() {
        return this.contentHeight;
    }
    
    public static void handle(MessageS2CAddPotionInteractParticles message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> ClientMessageHandler.handleAddPotionInteractParticles(message));
            context.setPacketHandled(true);
        }
    }
}
