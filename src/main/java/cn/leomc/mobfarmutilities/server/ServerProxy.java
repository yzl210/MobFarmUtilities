package cn.leomc.mobfarmutilities.server;

import cn.leomc.mobfarmutilities.common.api.IProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {
    @Override
    public void init() {

    }

    @Override
    public PlayerEntity getClientPlayer() {
        throw new IllegalStateException("Client Only");
    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Client Only");
    }
}
