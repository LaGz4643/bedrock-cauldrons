package lagz.bedrock_cauldrons.common.block;

import lagz.bedrock_cauldrons.common.block.entity.PotionCauldronBlockEntity;
import lagz.bedrock_cauldrons.core.other.BCCauldronInteractions;
import lagz.bedrock_cauldrons.core.util.ColorUtil;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PotionCauldronBlock extends BedrockCauldronBlock {
    public PotionCauldronBlock(Properties properties) {
        super(properties, BCCauldronInteractions.POTION);
    }
    
    @Override
    public int getColor(BlockAndTintGetter blockGetter, BlockPos pos) {
        if (blockGetter.getBlockEntity(pos) instanceof PotionCauldronBlockEntity entity && entity.getPotion() != Potions.WATER) {
            return entity.getPotionColor();
        }
        return BiomeColors.getAverageWaterColor(blockGetter, pos);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotionCauldronBlockEntity(pos, state);
    }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(8) == 0 && level.getBlockEntity(pos) instanceof PotionCauldronBlockEntity entity && entity.getPotion() != Potions.WATER) {
            float[] color = ColorUtil.intColorToFloatColor(entity.getPotionColor());
            level.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.625D, pos.getY() + this.getContentHeight(state) + 4.0D / 16.0D, pos.getZ() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.625D, color[0], color[1], color[2]);
        }
    }
}
