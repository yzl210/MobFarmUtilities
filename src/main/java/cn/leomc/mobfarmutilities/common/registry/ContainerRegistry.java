package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.container.FanContainer;
import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {

    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MobFarmUtilities.MODID);


    public static final RegistryObject<ContainerType<FanContainer>> FAN = CONTAINERS.register("fan",
            () -> IForgeContainerType.create((windowId, inv, data) ->
            {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inv.player.getEntityWorld().getTileEntity(pos);
                if (!(tileEntity instanceof FanTileEntity)) {
                    return null;
                }
                return new FanContainer(tileEntity, inv.player, inv, windowId);
            }));


    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

}
