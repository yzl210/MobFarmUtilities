package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModRegistry {


    public static ItemGroup ITEM_GROUP = new ItemGroup(MobFarmUtilities.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemRegistry.FAN.get());
        }
    };

    public static void register(IEventBus eventBus) {
        BlockRegistry.register(eventBus);
        ItemRegistry.register(eventBus);
        TileEntityRegistry.register(eventBus);
        ContainerRegistry.register(eventBus);
        NetworkHandler.register();
    }

}
