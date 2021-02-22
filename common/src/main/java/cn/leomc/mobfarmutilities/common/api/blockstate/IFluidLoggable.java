package cn.leomc.mobfarmutilities.common.api.blockstate;

import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public interface IFluidLoggable extends IBucketPickupHandler, ILiquidContainer {

    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    default boolean isValidFluid(Fluid fluid) {
        return fluid == getSupportedFluid();
    }

    default Fluid getSupportedFluid() {
        return Fluids.WATER;
    }

    default FluidState getFluid(BlockState state) {
        if (state.get(WATERLOGGED)) {
            Fluid fluid = getSupportedFluid();
            if (fluid instanceof FlowingFluid) {
                return ((FlowingFluid) fluid).getStillFluidState(false);
            }
            return fluid.getDefaultState();
        }
        return Fluids.EMPTY.getDefaultState();
    }

    default void updateFluids(BlockState state, IWorld world, BlockPos currentPos) {
        if (state.get(WATERLOGGED)) {
            Fluid fluid = getSupportedFluid();
            world.getPendingFluidTicks().scheduleTick(currentPos, fluid, fluid.getTickRate(world));
        }
    }

    @Override
    default boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.get(WATERLOGGED) && isValidFluid(fluid);
    }

    @Override
    default boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, FluidState fluidState) {
        Fluid fluid = fluidState.getFluid();
        if (canContainFluid(world, pos, state, fluid)) {
            if (!world.isRemote()) {
                world.setBlockState(pos, state.with(WATERLOGGED, true), 3);
                world.getPendingFluidTicks().scheduleTick(pos, fluid, fluid.getTickRate(world));
            }
            return true;
        }
        return false;
    }

    @Override
    default Fluid pickupFluid(IWorld world, BlockPos pos, BlockState state) {
        if (state.get(WATERLOGGED)) {
            world.setBlockState(pos, state.with(WATERLOGGED, false), 3);
            return getSupportedFluid();
        }
        return Fluids.EMPTY;
    }
}