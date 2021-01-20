package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ItemRegistry {

    protected static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MobFarmUtilities.MODID);


    public static final RegistryObject<Item> FAN = register("fan", () -> new BlockItem(BlockRegistry.FAN.get(), new Item.Properties().group(ModRegistry.ITEM_GROUP)));

    public static final RegistryObject<Item> FAN_BLADE = register("fan_blade", () -> new Item(new Item.Properties().group(ModRegistry.ITEM_GROUP)));


    static {
        for (UpgradeItem.Type type : UpgradeItem.Type.values()) {
            type.setRegistryObject(register(type.getRegistryName(), () -> new UpgradeItem(type)));
        }
    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> register(String name, Supplier<? extends Item> supplier) {
        return ITEMS.register(name, supplier);
    }


}
