package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import org.jetbrains.annotations.NotNull;

public abstract class ForgeLiquidExperienceFluid extends LiquidExperienceFluid {


    @Override
    protected @NotNull FluidAttributes createAttributes() {

        ResourceLocation still = LiquidExperienceFluid.Still.getResourceLocation();
        ResourceLocation flowing = LiquidExperienceFluid.Flowing.getResourceLocation();

        return FluidAttributes.builder(still, flowing).build(this);
    }

    public static class Flowing extends ForgeLiquidExperienceFluid {


        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            builder.add(LEVEL);
            super.createFluidStateDefinition(builder);
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return false;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }


    }

    public static class Still extends ForgeLiquidExperienceFluid {
        @Override
        public boolean isSource(@NotNull FluidState state) {
            return true;
        }

        @Override
        public int getAmount(@NotNull FluidState fluidState) {
            return 8;
        }

    }

}
