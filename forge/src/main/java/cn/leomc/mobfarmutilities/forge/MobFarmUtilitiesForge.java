package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.screen.ExperienceCollectorScreen;
import cn.leomc.mobfarmutilities.client.screen.FanScreen;
import cn.leomc.mobfarmutilities.client.screen.ItemCollectorScreen;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerMenuRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.forge.compat.top.TOPCompat;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInterModComms);
        if (FMLEnvironment.dist.isClient())
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerMenuRegistry.FAN.get(), FanScreen::new);
        MenuScreens.register(ContainerMenuRegistry.ITEM_COLLECTOR.get(), ItemCollectorScreen::new);
        MenuScreens.register(ContainerMenuRegistry.EXPERIENCE_COLLECTOR.get(), ExperienceCollectorScreen::new);
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.LIQUID_EXPERIENCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegistry.LIQUID_EXPERIENCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegistry.FLOWING_LIQUID_EXPERIENCE.get(), RenderType.translucent());
    }

    public void onInterModComms(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe"))
            TOPCompat.enable();
    }


}
