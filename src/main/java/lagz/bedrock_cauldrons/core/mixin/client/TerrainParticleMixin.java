package lagz.bedrock_cauldrons.core.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TerrainParticle.class)
public class TerrainParticleMixin {
    @Redirect(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/extensions/common/IClientBlockExtensions;areBreakingParticlesTinted(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean redirectAreBreakingParticlesTinted(IClientBlockExtensions instance, BlockState state, ClientLevel level, BlockPos pos) {
        if (state.is(Blocks.WATER_CAULDRON)) {
            return false;
        }
        return instance.areBreakingParticlesTinted(state, level, pos);
    }
}
