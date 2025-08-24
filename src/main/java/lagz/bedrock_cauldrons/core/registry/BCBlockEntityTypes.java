package lagz.bedrock_cauldrons.core.registry;

import lagz.bedrock_cauldrons.common.block.entity.DyeCauldronBlockEntity;
import lagz.bedrock_cauldrons.common.block.entity.PotionCauldronBlockEntity;
import lagz.bedrock_cauldrons.core.BedrockCauldrons;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BCBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BedrockCauldrons.MOD_ID);
    
    public static final RegistryObject<BlockEntityType<PotionCauldronBlockEntity>> POTION_CAULDRON = BLOCK_ENTITY_TYPES.register("potion_cauldron", () -> BlockEntityType.Builder.of(PotionCauldronBlockEntity::new, BCBlocks.POTION_CAULDRON.get()).build(null));
    public static final RegistryObject<BlockEntityType<DyeCauldronBlockEntity>> DYE_CAULDRON = BLOCK_ENTITY_TYPES.register("dye_cauldron", () -> BlockEntityType.Builder.of(DyeCauldronBlockEntity::new, BCBlocks.DYE_CAULDRON.get()).build(null));
}
