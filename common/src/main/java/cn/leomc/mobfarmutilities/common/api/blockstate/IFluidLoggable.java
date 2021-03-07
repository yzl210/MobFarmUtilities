package cn.leomc.mobfarmutilities.common.api.blockstate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public interface IFluidLoggable extends BucketPickup, LiquidBlockContainer {

    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    default boolean isValidFluid(Fluid fluid) {
        return fluid == getSupportedFluid();
    }

    default Fluid getSupportedFluid() {
        return Fluids.WATER;
    }

    default FluidState getFluid(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            Fluid fluid = getSupportedFluid();
            if (fluid instanceof FlowingFluid) {
                return ((FlowingFluid) fluid).getSource(false);
            }
            return fluid.defaultFluidState();
        }
        return Fluids.EMPTY.defaultFluidState();
    }

    default void updateFluids(BlockState state, LevelAccessor world, BlockPos currentPos) {
        if (state.getValue(WATERLOGGED)) {
            Fluid fluid = getSupportedFluid();
            world.getLiquidTicks().scheduleTick(currentPos, fluid, fluid.getTickDelay(world));
        }
    }

    @Override
    default boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(WATERLOGGED) && isValidFluid(fluid);
    }

    @Override
    default boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        Fluid fluid = fluidState.getType();
        if (canPlaceLiquid(world, pos, state, fluid)) {
            if (!world.isClientSide()) {
                world.setBlock(pos, state.setValue(WATERLOGGED, true), 3);
                world.getLiquidTicks().scheduleTick(pos, fluid, fluid.getTickDelay(world));
            }
            return true;
        }
        return false;
    }

    @Override
    default Fluid takeLiquid(LevelAccessor world, BlockPos pos, BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            world.setBlock(pos, state.setValue(WATERLOGGED, false), 3);
            return getSupportedFluid();
        }
        return Fluids.EMPTY;
    }
}