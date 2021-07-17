package cn.leomc.mobfarmutilities.common.api;

import net.minecraft.world.level.material.FlowingFluid;

public class LiquidBlock extends net.minecraft.world.level.block.LiquidBlock {
    public LiquidBlock(FlowingFluid flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }
}
