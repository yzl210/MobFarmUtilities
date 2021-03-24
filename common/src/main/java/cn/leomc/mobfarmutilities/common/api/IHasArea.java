package cn.leomc.mobfarmutilities.common.api;

import net.minecraft.world.phys.AABB;

public interface IHasArea {


    AABB getAABB();

    AABB getRenderAABB();

    boolean doRenderArea();

    void setRenderArea(boolean showArea);

    default void switchRenderArea() {
        setRenderArea(!doRenderArea());
    }

}
