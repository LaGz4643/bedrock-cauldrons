package com.lagz.examplemod.core.registry;

import com.lagz.examplemod.core.ExampleMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EMItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MOD_ID);

//    public static final RegistryObject<Item> EXAMPLE_ITEM = createItem("example_item", () -> new Item(new Item.Properties()));

    protected static RegistryObject<Item> createItem(String id, Supplier<Item> itemSupplier) {
        return ITEMS.register(id, itemSupplier);
    }
}
