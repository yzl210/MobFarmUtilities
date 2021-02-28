package cn.leomc.mobfarmutilities.common.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public abstract class BaseFlowingFluid extends FlowingFluid {

    @Override
    public boolean isEquivalentTo(Fluid fluid) {
        return fluid == getStillFluid() || fluid == getFlowingFluid();
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld world, BlockPos pos, BlockState state) {
        TileEntity tileEntity = state.getBlock().isTileEntityProvider() ? world.getTileEntity(pos) : null;
        Block.spawnDrops(state, world, pos, tileEntity);
    }

    @Override
    protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 1;
    }

    @Override
    public int getTickRate(IWorldReader iWorldReader) {
        return 10;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }
}
