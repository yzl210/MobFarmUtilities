package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.screen.ExperienceCollectorScreen;
import cn.leomc.mobfarmutilities.client.screen.FanScreen;
import cn.leomc.mobfarmutilities.client.screen.ItemCollectorScreen;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;


@Mod(MobFarmUtilities.MODID)
public class MobFarmUtilitiesForge {

    public MobFarmUtilitiesForge() {
        EventBuses.registerModEventBus(MobFarmUtilities.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        new MobFarmUtilities();
        if (FMLEnvironment.dist.isClient())
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerRegistry.FAN.get(), FanScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.ITEM_COLLECTOR.get(), ItemCollectorScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.EXPERIENCE_COLLECTOR.get(), ExperienceCollectorScreen::new);
        RenderTypeLookup.setRenderLayer(BlockRegistry.LIQUID_EXPERIENCE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.LIQUID_EXPERIENCE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.FLOWING_LIQUID_EXPERIENCE.get(), RenderType.getTranslucent());

    }


}
