package cn.leomc.mobfarmutilities.server;

import cn.leomc.mobfarmutilities.common.api.IProxy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerProxy implements IProxy {
    @Override
    public void init() {

    }

    @Override
    public Player getClientPlayer() {
        throw new IllegalStateException("Client Only");
    }

    @Override
    public Level getClientWorld() {
        throw new IllegalStateException("Client Only");
    }
}
