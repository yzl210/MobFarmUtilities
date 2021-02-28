package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.common.tileentity.ExperienceCollectorTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeExperienceCollectorTileEntity extends ExperienceCollectorTileEntity {

    public LazyOptional<IFluidTank> fluid = LazyOptional.of(() -> {
        FluidTank fluidTank = new FluidTank(limit);
        fluidTank.setFluid(new FluidStack(FluidRegistry.LIQUID_EXPERIENCE.get(), amount));
        return fluidTank;
    });

    public ForgeExperienceCollectorTileEntity() {
        super();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluid.cast();
        return super.getCapability(cap, side);
    }


}
