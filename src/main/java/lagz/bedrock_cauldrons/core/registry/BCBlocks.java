package lagz.bedrock_cauldrons.core.registry;

import lagz.bedrock_cauldrons.common.block.DyeCauldronBlock;
import lagz.bedrock_cauldrons.common.block.PotionCauldronBlock;
import lagz.bedrock_cauldrons.core.BedrockCauldrons;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BedrockCauldrons.MOD_ID);
    
    public static final RegistryObject<Block> POTION_CAULDRON = BLOCKS.register("potion_cauldron", () -> new PotionCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));
    public static final RegistryObject<Block> DYE_CAULDRON = BLOCKS.register("dye_cauldron", () -> new DyeCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));
}
