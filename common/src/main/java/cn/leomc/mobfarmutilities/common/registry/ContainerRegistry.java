package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.container.FanContainer;
import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class ContainerRegistry {

    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.MENU_KEY);


    public static final RegistrySupplier<ContainerType<FanContainer>> FAN = CONTAINERS.register("fan",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inventory.player.getEntityWorld().getTileEntity(pos);
                if (!(tileEntity instanceof FanTileEntity)) {
                    return null;
                }
                return new FanContainer(tileEntity, inventory.player, inventory, id);
            }));


    public static void register() {
        CONTAINERS.register();
    }

}
