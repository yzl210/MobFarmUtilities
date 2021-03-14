package cn.leomc.mobfarmutilities.common.api;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class FlowingFluidBlock extends LiquidBlock {
    public FlowingFluidBlock(FlowingFluid flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }
}
