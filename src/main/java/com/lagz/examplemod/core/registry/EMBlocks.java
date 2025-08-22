package com.lagz.examplemod.core.registry;

import com.lagz.examplemod.core.ExampleMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EMBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MOD_ID);

//    public static final RegistryObject<Block> EXAMPLE_BLOCK = createBlock("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.0F, 1.0F)));
//    public static final RegistryObject<Block> EXAMPLE_BLOCK_WITHOUT_ITEM = createBlockWithoutItem("example_block_without_item", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.0F, 1.0F).sound(SoundType.WOOD)));

    private static RegistryObject<Block> createBlock(String id, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(id, blockSupplier);
        EMItems.createItem(id, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Block> createBlockWithoutItem(String id, Supplier<Block> blockSupplier) {
        return BLOCKS.register(id, blockSupplier);
    }

//    private static final class EMProperties {
//        private static final BlockBehaviour.Properties EXAMPLE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.0F, 1.0F).requiresCorrectToolForDrops();
//    }
}
