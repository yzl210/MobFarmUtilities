package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModRegistry {

    public static CreativeModeTab ITEM_GROUP = CreativeTabRegistry.create(new ResourceLocation(MobFarmUtilities.MODID, "default"), () -> new ItemStack(ItemRegistry.FAN.get()));

    public static void register() {
        FluidRegistry.register();
        BlockRegistry.register();
        ItemRegistry.register();
        BlockEntityRegistry.register();
        ContainerMenuRegistry.register();
        NetworkHandler.register();
    }

}
