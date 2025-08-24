package lagz.bedrock_cauldrons.common.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BedrockCauldronBlock extends LayeredCauldronBlock implements EntityBlock {
    public static final Predicate<Biome.Precipitation> NONE = precipitation -> false;
    
    public BedrockCauldronBlock(Properties properties, Map<Item, CauldronInteraction> cauldronInteractions) {
        super(properties, BedrockCauldronBlock.NONE, cauldronInteractions);
    }
    
    public abstract int getColor(BlockAndTintGetter blockGetter, BlockPos pos);
    
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(Items.CAULDRON);
    }
    
    @Override
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new IClientBlockExtensions() {
            @Override
            public boolean areBreakingParticlesTinted(BlockState state, ClientLevel level, BlockPos pos) {
                return false;
            }
        });
    }
    
    public static double getCauldronContentHeight(BlockState blockstate) {
        return blockstate.is(Blocks.CAULDRON) ? 4.0D / 16.0D : (6.0D + blockstate.getValue(LEVEL) * 3.0D) / 16.0D;
    }
}
