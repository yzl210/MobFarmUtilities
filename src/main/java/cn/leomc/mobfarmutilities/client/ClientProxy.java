package cn.leomc.mobfarmutilities.client;

import cn.leomc.mobfarmutilities.client.screen.FanScreen;
import cn.leomc.mobfarmutilities.common.api.IProxy;
import cn.leomc.mobfarmutilities.common.registry.ContainerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {
    @Override
    public void init() {
        ScreenManager.registerFactory(ContainerRegistry.FAN.get(), FanScreen::new);
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}
