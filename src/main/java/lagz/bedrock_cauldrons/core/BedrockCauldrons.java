package lagz.bedrock_cauldrons.core;

import lagz.bedrock_cauldrons.core.networking.BCNetworking;
import lagz.bedrock_cauldrons.core.other.BCCauldronInteractions;
import lagz.bedrock_cauldrons.core.registry.BCBlockEntityTypes;
import lagz.bedrock_cauldrons.core.registry.BCBlocks;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BedrockCauldrons.MOD_ID)
public class BedrockCauldrons {
    public static final String MOD_ID = "bedrock_cauldrons";
//    private static final Logger LOGGER = LogUtils.getLogger();

    public BedrockCauldrons() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BCBlocks.BLOCKS.register(modEventBus);
        BCBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        
        BCNetworking.registerMessages();

//        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::dataSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BCCauldronInteractions.registerCauldronInteractions();
        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
//            ItemBlockRenderTypes.setRenderLayer(Blocks.WATER_CAULDRON, RenderType.tripwire());
        });
    }

    private void dataSetup(GatherDataEvent event) {

    }
}
