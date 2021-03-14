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
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ContainerMenuRegistry {

    private static final DeferredRegister<MenuType<?>> CONTAINER_MENUS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.MENU_REGISTRY);


    public static final RegistrySupplier<MenuType<FanMenu>> FAN = CONTAINER_MENUS.register("fan",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity tileEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(tileEntity instanceof FanBlockEntity)) {
                    return null;
                }
                return new FanMenu(tileEntity, inventory.player, inventory, id);
            }));

    public static final RegistrySupplier<MenuType<ItemCollectorMenu>> ITEM_COLLECTOR = CONTAINER_MENUS.register("item_collector",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity tileEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(tileEntity instanceof ItemCollectorBlockEntity)) {
                    return null;
                }
                return new ItemCollectorMenu(tileEntity, inventory.player, inventory, id);
            }));


    public static final RegistrySupplier<MenuType<ExperienceCollectorMenu>> EXPERIENCE_COLLECTOR = CONTAINER_MENUS.register("experience_collector",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity tileEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(tileEntity instanceof ExperienceCollectorBlockEntity)) {
                    return null;
                }
                return new ExperienceCollectorMenu(tileEntity, inventory.player, inventory, id);
            }));


    public static final RegistrySupplier<MenuType<SlaughtererMenu>> SLAUGHTERER = CONTAINER_MENUS.register("slaughterer",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity tileEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(tileEntity instanceof SlaughtererBlockEntity)) {
                    return null;
                }
                return new SlaughtererMenu(tileEntity, inventory.player, inventory, id);
            }));

    public static void register() {
        CONTAINER_MENUS.register();
    }

}
