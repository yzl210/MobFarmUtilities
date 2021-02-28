package cn.leomc.mobfarmutilities.common.fluid;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.InvocationTargetException;


public abstract class LiquidExperienceFluid extends BaseFlowingFluid {


    @Override
    public Item getFilledBucket() {
        return ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get();
    }

    @Override
    public Fluid getFlowingFluid() {
        return FluidRegistry.FLOWING_LIQUID_EXPERIENCE.get();
    }

    @Override
    public Fluid getStillFluid() {
        return FluidRegistry.LIQUID_EXPERIENCE.get();
    }


    @Override
    protected BlockState getBlockState(FluidState state) {
        return BlockRegistry.LIQUID_EXPERIENCE.get().getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }


    public static class Flowing extends LiquidExperienceFluid {

        public static LiquidExperienceFluid getFluid() {
            if (Platform.isForge()) {
                Class<LiquidExperienceFluid> fluidClass;
                try {
                    fluidClass = (Class<LiquidExperienceFluid>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeLiquidExperienceFluid$Flowing");
                    return fluidClass.getConstructor().newInstance();
                } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return new Flowing();
        }

        public static ResourceLocation getResourceLocation() {
            return new ResourceLocation(MobFarmUtilities.MODID, "block/liquid_experience/flow");
        }

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

    public static class Still extends LiquidExperienceFluid {

        public static LiquidExperienceFluid getFluid() {
            if (Platform.isForge()) {
                Class<LiquidExperienceFluid> fluidClass;
                try {
                    fluidClass = (Class<LiquidExperienceFluid>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeLiquidExperienceFluid$Still");
                    return fluidClass.getConstructor().newInstance();
                } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return new Still();
        }

        public static ResourceLocation getResourceLocation() {
            return new ResourceLocation(MobFarmUtilities.MODID, "block/liquid_experience/still");
        }

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
