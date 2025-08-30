package lagz.bedrock_cauldrons.common.network.handler;

import lagz.bedrock_cauldrons.common.network.MessageS2CAddPotionEvaporateParticles;
import lagz.bedrock_cauldrons.common.network.MessageS2CAddPotionInteractParticles;
import lagz.bedrock_cauldrons.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;

public class ClientMessageHandler {
    
    public static void handleAddPotionInteractParticles(MessageS2CAddPotionInteractParticles message) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos blockpos = message.blockpos();
        assert level != null : "Level should never be null when handling messages";
        
        float[] color = ColorUtil.intColorToFloatColor(message.potionColor());
        for (int i = 0; i < 5; i++) {
            level.addParticle(ParticleTypes.ENTITY_EFFECT, blockpos.getX() + 0.5D + (level.getRandom().nextDouble() - 0.5D) * 5.0D / 8.0D, blockpos.getY() + message.contentHeight() + 2.0D / 16.0D, blockpos.getZ() + 0.5D + (level.getRandom().nextDouble() - 0.5D) * 5.0D / 8.0D, color[0], color[1], color[2]);
        }
    }
    
    public static void handleAddPotionEvaporateParticles(MessageS2CAddPotionEvaporateParticles message) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockPos blockpos = message.blockpos();
        assert level != null : "Level should never be null when handling messages";
        
        for (int i = 0; i < 8; ++i) {
            level.addParticle(ParticleTypes.POOF, blockpos.getX() + 0.5D + (level.getRandom().nextDouble() - 0.5D) * 5.0D / 8.0D, blockpos.getY() + 5.0D / 16.0D, blockpos.getZ() + 0.5D + (level.getRandom().nextDouble() - 0.5D) * 5.0D / 8.0D, 0.0D, 0.05D, 0.0D);
        }
    }
}
