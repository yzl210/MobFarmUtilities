package cn.leomc.mobfarmutilities.client;

import cn.leomc.mobfarmutilities.common.api.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientProxy implements IProxy {
    @Override
    public void init() {
    }

    @Override
    public Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public Level getClientWorld() {
        return Minecraft.getInstance().level;
    }
}
