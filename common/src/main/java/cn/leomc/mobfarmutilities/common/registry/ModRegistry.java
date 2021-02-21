package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModRegistry {


    public static ItemGroup ITEM_GROUP = CreativeTabs.create(new ResourceLocation(MobFarmUtilities.MODID, "default"), () -> new ItemStack(ItemRegistry.FAN.get()));

    public static void register() {
        BlockRegistry.register();
        ItemRegistry.register();
        TileEntityRegistry.register();
        ContainerRegistry.register();
        NetworkHandler.register();
    }

}
