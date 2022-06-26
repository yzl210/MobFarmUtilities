package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ForgeExperienceCollectorBlockEntity extends ExperienceCollectorBlockEntity {

    public LazyOptional<IFluidHandler> fluid = LazyOptional.of(() -> new IFluidHandler() {

        @Override
        public int getTanks() {
            return 1;
        }

        @NotNull
        @Override
        public FluidStack getFluidInTank(int i) {
            return new FluidStack(FluidRegistry.LIQUID_EXPERIENCE.get(), getAmount());
        }

        @Override
        public int getTankCapacity(int i) {
            return limit;
        }

        @Override
        public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
            return fluidStack.getFluid().isSame(FluidRegistry.LIQUID_EXPERIENCE.get());
        }

        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (!resource.isEmpty() && this.isFluidValid(0, resource)) {
                if (action.simulate()) {
                    if (getAmount() == 0) {
                        return Math.min(limit, resource.getAmount());
                    } else {
                        return !(resource.getFluid() == FluidRegistry.LIQUID_EXPERIENCE.get()) ? 0 : Math.min(limit - getAmount(), resource.getAmount());
                    }
                } else if (getAmount() == 0) {
                    setAmount(Math.min(limit, resource.getAmount()));
                    return getAmount();
                } else {
                    int filled = limit - getAmount();
                    if (resource.getAmount() < filled) {
                        addAmount(resource.getAmount());
                        filled = resource.getAmount();
                    } else {
                        setAmount(limit);
                    }

                    return filled;
                }
            } else {
                return 0;
            }
        }

        @Nonnull
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            return !resource.isEmpty() && resource.getFluid() == FluidRegistry.LIQUID_EXPERIENCE.get() ? this.drain(resource.getAmount(), action) : FluidStack.EMPTY;
        }

        @Nonnull
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            int drained = Math.min(getAmount(), maxDrain);

            FluidStack stack = new FluidStack(FluidRegistry.LIQUID_EXPERIENCE.get(), drained);
            if (action.execute() && drained > 0) {
                addAmount(-drained);
            }

            return stack;
        }


    });


    public ForgeExperienceCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluid.cast();
        return super.getCapability(cap, side);
    }
}
