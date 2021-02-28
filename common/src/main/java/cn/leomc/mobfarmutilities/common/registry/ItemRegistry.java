package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeItem;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ItemRegistry {

    protected static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.ITEM_KEY);


    public static final RegistrySupplier<Item> FAN = register("fan", () -> new BlockItem(BlockRegistry.FAN.get(), new Item.Properties().group(ModRegistry.ITEM_GROUP)));

    public static final RegistrySupplier<Item> FAN_BLADE = register("fan_blade", () -> new Item(new Item.Properties().group(ModRegistry.ITEM_GROUP)));

    public static final RegistrySupplier<Item> ITEM_COLLECTOR = register("item_collector", () -> new BlockItem(BlockRegistry.ITEM_COLLECTOR.get(), new Item.Properties().group(ModRegistry.ITEM_GROUP)));

    public static final RegistrySupplier<Item> EXPERIENCE_COLLECTOR = register("experience_collector", () -> new BlockItem(BlockRegistry.EXPERIENCE_COLLECTOR.get(), new Item.Properties().group(ModRegistry.ITEM_GROUP)));


    public static final RegistrySupplier<Item> LIQUID_EXPERIENCE_BUCKET = register("liquid_experience_bucket", () -> PlatformCompatibility.getBucketItem(FluidRegistry.LIQUID_EXPERIENCE, new Item.Properties().group(ModRegistry.ITEM_GROUP).containerItem(Items.BUCKET).maxStackSize(1)));


    static {
        for (UpgradeItem.Type type : UpgradeItem.Type.values()) {
            type.setRegistrySupplier(register(type.getRegistryName(), () -> new UpgradeItem(type)));
        }
    }


    public static void register() {
        ITEMS.register();
    }

    private static RegistrySupplier<Item> register(String name, Supplier<? extends Item> supplier) {
        return ITEMS.register(name, supplier);
    }


}
