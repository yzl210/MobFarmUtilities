package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.recipe.LaserEmitterRefuelRecipe;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.forge.compat.top.TOPCompat;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;


@Mod(MobFarmUtilities.MODID)
public class MobFarmUtilitiesForge {

    public MobFarmUtilitiesForge() {
        EventBuses.registerModEventBus(MobFarmUtilities.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        new MobFarmUtilities();
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, this::onRegisterRecipeSerializers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInterModComms);
        if (FMLEnvironment.dist.isClient())
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.LIQUID_EXPERIENCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegistry.LIQUID_EXPERIENCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegistry.FLOWING_LIQUID_EXPERIENCE.get(), RenderType.translucent());
    }

    public void onRegisterRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, LaserEmitterRefuelRecipe.ID, LaserEmitterRefuelRecipe.TYPE);
        event.getRegistry().register(LaserEmitterRefuelRecipe.SERIALIZER.setRegistryName(LaserEmitterRefuelRecipe.ID));
    }



    public void onInterModComms(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe"))
            TOPCompat.enable();
    }


}
