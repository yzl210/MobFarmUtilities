package cn.leomc.mobfarmutilities.fabric;

import cn.leomc.mobfarmutilities.client.screen.FanScreen;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;


public class MobFarmUtilitiesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ContainerRegistry.FAN.get(), FanScreen::new);
    }
}
