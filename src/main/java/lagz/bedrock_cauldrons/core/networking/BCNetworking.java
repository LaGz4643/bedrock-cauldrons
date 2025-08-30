package lagz.bedrock_cauldrons.core.networking;

import lagz.bedrock_cauldrons.common.network.MessageS2CAddPotionInteractParticles;
import lagz.bedrock_cauldrons.core.BedrockCauldrons;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class BCNetworking {
    public static final String PROTOCOL = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(BedrockCauldrons.MOD_ID, "network"))
            .networkProtocolVersion(() -> PROTOCOL)
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .simpleChannel();
    
    public static void registerMessages() {
        int id = -1;
        
        CHANNEL.registerMessage(++id, MessageS2CAddPotionInteractParticles.class, MessageS2CAddPotionInteractParticles::write, MessageS2CAddPotionInteractParticles::new, MessageS2CAddPotionInteractParticles::handle);
    }
    
    public static void sendAddPotionInteractParticlesMessage(Level level, BlockPos blockpos, int potionColor, double contentHeight) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockpos)), new MessageS2CAddPotionInteractParticles(blockpos, potionColor, contentHeight));
    }
}
