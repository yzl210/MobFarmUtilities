package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.blockentity.FanBlockEntity;
import cn.leomc.mobfarmutilities.common.blockentity.ItemCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.blockentity.SlaughtererBlockEntity;
import cn.leomc.mobfarmutilities.common.menu.ExperienceCollectorMenu;
import cn.leomc.mobfarmutilities.common.menu.FanMenu;
import cn.leomc.mobfarmutilities.common.menu.ItemCollectorMenu;
import cn.leomc.mobfarmutilities.common.menu.SlaughtererMenu;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class ContainerMenuRegistry {

    private static final DeferredRegister<MenuType<?>> CONTAINER_MENUS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.MENU_REGISTRY);


    public static final RegistrySupplier<MenuType<FanMenu>> FAN = CONTAINER_MENUS.register("fan",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                if (inventory.player.getLevel().getBlockEntity(data.readBlockPos()) instanceof FanBlockEntity fan)
                    return new FanMenu(fan, inventory.player, inventory, id);
                return null;
            }));

    public static final RegistrySupplier<MenuType<ItemCollectorMenu>> ITEM_COLLECTOR = CONTAINER_MENUS.register("item_collector",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                if (inventory.player.getLevel().getBlockEntity(data.readBlockPos()) instanceof ItemCollectorBlockEntity itemCollector)
                    return new ItemCollectorMenu(itemCollector, inventory.player, inventory, id);
                return null;
            }));


    public static final RegistrySupplier<MenuType<ExperienceCollectorMenu>> EXPERIENCE_COLLECTOR = CONTAINER_MENUS.register("experience_collector",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                if (inventory.player.getLevel().getBlockEntity(data.readBlockPos()) instanceof ExperienceCollectorBlockEntity experienceCollector)
                    return new ExperienceCollectorMenu(experienceCollector, inventory.player, inventory, id);
                return null;
            }));


    public static final RegistrySupplier<MenuType<SlaughtererMenu>> SLAUGHTERER = CONTAINER_MENUS.register("slaughterer",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                if (inventory.player.getLevel().getBlockEntity(data.readBlockPos()) instanceof SlaughtererBlockEntity slaughterer)
                    return new SlaughtererMenu(slaughterer, inventory.player, inventory, id);
                return null;
            }));

    public static void register() {
        CONTAINER_MENUS.register();
    }

}
