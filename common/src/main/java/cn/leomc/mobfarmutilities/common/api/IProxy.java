package cn.leomc.mobfarmutilities.common.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IProxy {

    void init();

    Player getClientPlayer();

    Level getClientWorld();

}
