package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.screen.ExperienceCollectorScreen;
import cn.leomc.mobfarmutilities.client.screen.FanScreen;
import cn.leomc.mobfarmutilities.client.screen.ItemCollectorScreen;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;


@Mod(MobFarmUtilities.MODID)
public class MobFarmUtilitiesForge {

    public MobFarmUtilitiesForge() {
        EventBuses.registerModEventBus(MobFarmUtilities.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        new MobFarmUtilities();
        if (FMLEnvironment.dist.isClient())
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
    }

    @OnlyIn(Dist.CLIENT)
    public void onCommonSetup(FMLCommonSetupEvent event) {
        ScreenManager.registerFactory(ContainerRegistry.FAN.get(), FanScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.ITEM_COLLECTOR.get(), ItemCollectorScreen::new);
        ScreenManager.registerFactory(ContainerRegistry.EXPERIENCE_COLLECTOR.get(), ExperienceCollectorScreen::new);
    }


}
