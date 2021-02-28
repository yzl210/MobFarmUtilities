/*package cn.leomc.mobfarmutilities.forge;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class FluidStackWrapper implements IFluidHandler {
    protected FluidTank fluidTank;
    protected int limit;

    public FluidStackWrapper(Fluid fluid, int amount, int limit){
        this.limit = limit;
    }

    @Override
    public int getTanks() {
        return (int) fluidStack.getAmount().getNumerator();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int i) {
        return new FluidStack(fluidStack.getFluid(), (int) fluidStack.getAmount().getNumerator());
    }

    @Override
    public int getTankCapacity(int i) {
        return limit;
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!resource.isEmpty()) {
            if (action.simulate()) {
                if (this.fluidStack.isEmpty()) {
                    return Math.min(this.limit, resource.getAmount());
                } else {
                    return !isFluidStackEqual(resource) ? 0 : Math.min(this.limit - (int)this.fluidStack.getAmount().getNumerator(), resource.getAmount());
                }
            } else if (this.fluidStack.isEmpty()) {
                this.fluidStack.setAmount(Fraction.of(Math.min(this.limit, resource.getAmount()), 1000));
                return (int) this.fluidStack.getAmount().getNumerator();
            } else if (!isFluidStackEqual(resource)) {
                return 0;
            } else {
                int filled = this.limit - (int)this.fluidStack.getAmount().getNumerator();
                if (resource.getAmount() < filled) {
                    this.fluidStack.grow(Fraction.of(resource.getAmount(), 1000));
                    filled = resource.getAmount();
                } else {
                    this.fluidStack.setAmount(Fraction.of(limit, 1000));
                }
                return filled;
            }
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return !resource.isEmpty() && isFluidStackEqual(resource) ? this.drain(resource.getAmount(), action) : FluidStack.EMPTY;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = maxDrain;
        if (fluidStack.getAmount().getNumerator() < maxDrain) {
            drained = (int) fluidStack.getAmount().getNumerator();
        }

        FluidStack stack = new FluidStack(fluidStack.getFluid(), drained, fluidStack.getTag());
        if (action.execute() && drained > 0) {
            fluidStack.shrink(Fraction.of(drained, 1000));
        }

        return stack;
    }

    public boolean isFluidStackEqual(FluidStack other) {
        return fluidStack.getFluid() == other.getFluid() && this.isTagEqual(other);
    }

    private boolean isTagEqual(FluidStack other) {
        return fluidStack.getTag() == null ? other.getTag() == null : other.getTag() != null && fluidStack.getTag().equals(other.getTag());
    }


}


 */