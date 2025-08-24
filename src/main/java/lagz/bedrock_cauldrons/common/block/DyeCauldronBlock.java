package lagz.bedrock_cauldrons.common.block;

import lagz.bedrock_cauldrons.common.block.entity.DyeCauldronBlockEntity;
import lagz.bedrock_cauldrons.core.other.BCCauldronInteractions;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DyeCauldronBlock extends BedrockCauldronBlock {
    
    public DyeCauldronBlock(Properties properties) {
        super(properties, BCCauldronInteractions.DYE);
    }
    
    @Override
    public int getColor(BlockAndTintGetter blockGetter, BlockPos pos) {
        if (blockGetter.getBlockEntity(pos) instanceof DyeCauldronBlockEntity entity) {
            int color = entity.getColor();
            if (color != -1) {
                return color;
            }
        }
        return BiomeColors.getAverageWaterColor(blockGetter, pos);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockpos, BlockState blockstate) {
        return new DyeCauldronBlockEntity(blockpos, blockstate);
    }
}
