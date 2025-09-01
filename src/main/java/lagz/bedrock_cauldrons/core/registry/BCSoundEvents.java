package lagz.bedrock_cauldrons.core.registry;

import lagz.bedrock_cauldrons.core.BedrockCauldrons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BCSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BedrockCauldrons.MOD_ID);
    
    public static final RegistryObject<SoundEvent> DYE_WATER = register("block.dye_cauldron.dye_water");
    public static final RegistryObject<SoundEvent> DYE_ITEM = register("block.dye_cauldron.dye_item");
    public static final RegistryObject<SoundEvent> POTION_EVAPORATE = register("block.potion_cauldron.potion_evaporate");
    public static final RegistryObject<SoundEvent> TIP_ARROWS = register("block.potion_cauldron.tip_arrows");
    
    private static RegistryObject<SoundEvent> register(String id) {
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BedrockCauldrons.MOD_ID, id)));
    }
}
