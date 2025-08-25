package lagz.bedrock_cauldrons.core.networking;

import lagz.bedrock_cauldrons.common.network.MessageS2CAddPotionCauldronInteractParticles;
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
        
        CHANNEL.registerMessage(++id, MessageS2CAddPotionCauldronInteractParticles.class, MessageS2CAddPotionCauldronInteractParticles::write, MessageS2CAddPotionCauldronInteractParticles::new, MessageS2CAddPotionCauldronInteractParticles::handle);
    }
    
    public static void sendAddPotionCauldronInteractParticlesMessage(Level level, BlockPos blockpos, int potionColor, double contentHeight) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockpos)), new MessageS2CAddPotionCauldronInteractParticles(blockpos, potionColor, contentHeight));
    }
}
