package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class ForgeLiquidExperienceFluid extends LiquidExperienceFluid {


    @Override
    protected FluidAttributes createAttributes() {

        ResourceLocation still = LiquidExperienceFluid.Still.getResourceLocation();
        ResourceLocation flowing = LiquidExperienceFluid.Flowing.getResourceLocation();

        return FluidAttributes.builder(still, flowing).build(this);
    }

    public static class Flowing extends ForgeLiquidExperienceFluid {


        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            builder.add(LEVEL_1_8);
            super.fillStateContainer(builder);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL_1_8);
        }


    }

    public static class Still extends ForgeLiquidExperienceFluid {
        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

    }

}
