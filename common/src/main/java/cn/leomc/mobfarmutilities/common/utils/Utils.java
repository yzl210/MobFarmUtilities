package cn.leomc.mobfarmutilities.common.utils;

public class Utils {

    public static int getPointsForNextLevel(int level) {
        int needed = 0;
        if (level >= 0 && level < 15)
            needed = 2 * level + 7;
        else if (level >= 15 && level < 30)
            needed = 5 * level - 38;
        else if (level >= 30)
            needed = 9 * level - 158;

        return needed;
    }

    public static int getPointsForNextLevel(int currentLevel, int levels) {
        int needed = 0;
        for (int i = currentLevel; i < currentLevel + levels; i++)
            needed += getPointsForNextLevel(i);
        return needed;
    }

}
