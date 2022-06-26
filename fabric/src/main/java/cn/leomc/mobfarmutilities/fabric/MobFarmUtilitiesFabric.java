package cn.leomc.mobfarmutilities.fabric;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.recipe.LaserEmitterRefuelRecipe;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import cn.leomc.mobfarmutilities.common.utils.fabric.PlatformCompatibilityImpl;
import cn.leomc.mobfarmutilities.fabric.api.IItemStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Registry;


public class MobFarmUtilitiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new MobFarmUtilities();

        Registry.register(Registry.RECIPE_TYPE, LaserEmitterRefuelRecipe.ID, LaserEmitterRefuelRecipe.TYPE);
        Registry.register(Registry.RECIPE_SERIALIZER, LaserEmitterRefuelRecipe.ID, LaserEmitterRefuelRecipe.SERIALIZER);

        ServerWorldEvents.UNLOAD.register(PlatformCompatibilityImpl::unloadLevel);

        ItemStorage.SIDED.registerForBlockEntities((blockEntity, context) -> {
            if(blockEntity instanceof IItemStorage storage)
                return storage.getStorage();
            return null;
        }, BlockEntityRegistry.FAN.get(), BlockEntityRegistry.ITEM_COLLECTOR.get(), BlockEntityRegistry.SLAUGHTERER.get());

        FluidStorage.SIDED.registerForBlockEntity((blockEntity, context) -> {
            if(blockEntity instanceof FabricExperienceCollectorBlockEntity experienceCollector)
                return experienceCollector.getFluidStorage();
            return null;
        }, BlockEntityRegistry.EXPERIENCE_COLLECTOR.get());
    }

}
