package lagz.bedrock_cauldrons.common.network.handler;

import lagz.bedrock_cauldrons.common.network.MessageS2CAddPotionCauldronInteractParticles;
import lagz.bedrock_cauldrons.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.network.NetworkEvent;

public class ClientMessageHandler {
    
    public static void handleAddCauldronInteractParticles(MessageS2CAddPotionCauldronInteractParticles message, NetworkEvent.Context context) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos blockpos = message.getBlockPos();
        
        float[] color = ColorUtil.intColorToFloatColor(message.getPotionColor());
        for (int i = 0; i < 8; i++) {
            level.addParticle(ParticleTypes.ENTITY_EFFECT, blockpos.getX() + 0.5D, blockpos.getY() + message.getContentHeight() + 2.0D / 16.0D, blockpos.getZ() + 0.5D, color[0], color[1], color[2]);
        }
    }
}
