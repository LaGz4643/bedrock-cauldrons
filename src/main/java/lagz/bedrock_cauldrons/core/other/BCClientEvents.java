package lagz.bedrock_cauldrons.core.other;

import lagz.bedrock_cauldrons.common.block.BedrockCauldronBlock;
import lagz.bedrock_cauldrons.core.BedrockCauldrons;
import lagz.bedrock_cauldrons.core.registry.BCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, modid = BedrockCauldrons.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BCClientEvents {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((BlockState state, BlockAndTintGetter blockGetter, BlockPos pos, int index) ->
                        blockGetter != null && pos != null ? ((BedrockCauldronBlock) state.getBlock()).getColor(blockGetter, pos) : -1,
                BCBlocks.POTION_CAULDRON.get(), BCBlocks.DYE_CAULDRON.get());
    }
}
