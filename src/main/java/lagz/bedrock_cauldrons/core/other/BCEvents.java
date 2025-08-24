package lagz.bedrock_cauldrons.core.other;

import lagz.bedrock_cauldrons.core.BedrockCauldrons;
import lagz.bedrock_cauldrons.core.registry.BCBlocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = BedrockCauldrons.MOD_ID)
public class BCEvents {
    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) {
            BlockState state = event.getLevel().getBlockState(event.getPos());
            if (state.is(Blocks.WATER_CAULDRON) || state.is(BCBlocks.POTION_CAULDRON.get()) || state.is(BCBlocks.DYE_CAULDRON.get())) {
                if (state.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                    event.setCancellationResult(InteractionResult.FAIL);
                    event.setCanceled(true);
                }
            }
        }
    }
}
