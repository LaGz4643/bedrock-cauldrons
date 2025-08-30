package lagz.bedrock_cauldrons.common.network;

import lagz.bedrock_cauldrons.common.network.handler.ClientMessageHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MessageS2CAddPotionEvaporateParticles(BlockPos blockpos) {
    
    public MessageS2CAddPotionEvaporateParticles(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }
    
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockpos);
    }
    
    public static void handle(MessageS2CAddPotionEvaporateParticles message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> ClientMessageHandler.handleAddPotionEvaporateParticles(message));
            context.setPacketHandled(true);
        }
    }
}
