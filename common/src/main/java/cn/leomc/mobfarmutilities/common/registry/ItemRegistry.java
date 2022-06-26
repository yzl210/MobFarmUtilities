package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.item.FanBladeItem;
import cn.leomc.mobfarmutilities.common.item.LaserEmitterItem;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.*;

import java.util.function.Supplier;

public class ItemRegistry {

    protected static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.ITEM_REGISTRY);


    public static final RegistrySupplier<Item> FAN = register("fan", () -> new BlockItem(BlockRegistry.FAN.get(), new Item.Properties().tab(ModRegistry.ITEM_GROUP)));

    public static final RegistrySupplier<Item> FAN_BLADE = register("fan_blade", FanBladeItem::new);

    public static final RegistrySupplier<Item> ITEM_COLLECTOR = register("item_collector", () -> new BlockItem(BlockRegistry.ITEM_COLLECTOR.get(), new Item.Properties().tab(ModRegistry.ITEM_GROUP)));

    public static final RegistrySupplier<Item> EXPERIENCE_COLLECTOR = register("experience_collector", () -> new BlockItem(BlockRegistry.EXPERIENCE_COLLECTOR.get(), new Item.Properties().tab(ModRegistry.ITEM_GROUP)));

    public static final RegistrySupplier<Item> LASER_EMITTER = register("laser_emitter", LaserEmitterItem::new);

    public static final RegistrySupplier<Item> SLAUGHTERER = register("slaughterer", () -> new BlockItem(BlockRegistry.SLAUGHTERER.get(), new Item.Properties().tab(ModRegistry.ITEM_GROUP)));


    public static final RegistrySupplier<Item> LIQUID_EXPERIENCE_BUCKET = register("liquid_experience_bucket", () -> PlatformCompatibility.getBucketItem(FluidRegistry.LIQUID_EXPERIENCE, new Item.Properties().tab(ModRegistry.ITEM_GROUP).craftRemainder(Items.BUCKET).stacksTo(1)));


    public static final RegistrySupplier<Item> FAKE_SWORD = register("fake_sword", () -> new ShovelItem(Tiers.WOOD, 1.5F, -3.0F, new Item.Properties()));


    public static void register() {
        ITEMS.register();
    }

    private static RegistrySupplier<Item> register(String name, Supplier<? extends Item> supplier) {
        return ITEMS.register(name, supplier);
    }


}
