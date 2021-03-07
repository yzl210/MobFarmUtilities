package cn.leomc.mobfarmutilities.common.compat;

import cn.leomc.mobfarmutilities.common.block.FanBlock;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class InfoList {

    public static List<Class<? extends Block>> INFO_PROVIDERS = new ArrayList<>();

    static {
        add(FanBlock.class);
    }

    public static void add(Class<? extends Block> blockClass) {
        INFO_PROVIDERS.add(blockClass);
    }

}
