package lagz.bedrock_cauldrons.core.mixin;

import lagz.bedrock_cauldrons.core.registry.BCBlocks;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PoiTypes.class)
public class PoiTypesMixin {
    @Inject(method = "forState", at = @At("HEAD"), cancellable = true)
    private static void returnPoiForBedrockCauldrons(BlockState blockstate, CallbackInfoReturnable<Optional<Holder<PoiType>>> cir) {
        if (blockstate.is(BCBlocks.POTION_CAULDRON.get()) || blockstate.is(BCBlocks.DYE_CAULDRON.get())) {
            cir.setReturnValue(ForgeRegistries.POI_TYPES.getHolder(PoiTypes.LEATHERWORKER));
        }
    }
    
    @Inject(method = "hasPoi", at = @At("HEAD"), cancellable = true)
    private static void returnTrueForBedrockCauldrons(BlockState blockstate, CallbackInfoReturnable<Boolean> cir) {
        if (blockstate.is(BCBlocks.POTION_CAULDRON.get()) || blockstate.is(BCBlocks.DYE_CAULDRON.get())) {
            cir.setReturnValue(true);
        }
    }
}
