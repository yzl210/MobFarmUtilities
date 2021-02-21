package cn.leomc.mobfarmutilities.client;

import cn.leomc.mobfarmutilities.common.api.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {
    @Override
    public void init() {
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
