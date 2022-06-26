package cn.leomc.mobfarmutilities.common.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Utils {

    public static int getPointsForNextLevel(int level) {
        if (level >= 30)
            return 112 + (level - 30) * 9;
        else
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
    }

    public static int getPointsForNextLevel(int currentLevel, int levels) {
        int needed = 0;
        for (int i = currentLevel; i < currentLevel + levels; i++)
            needed += getPointsForNextLevel(i);
        return needed;
    }

    @Environment(EnvType.CLIENT)
    public static net.minecraft.client.player.LocalPlayer getLocalPlayer() {
        return net.minecraft.client.Minecraft.getInstance().player;
    }

}
