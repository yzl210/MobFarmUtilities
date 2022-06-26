package cn.leomc.mobfarmutilities.common.fluid;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.registry.BlockRegistry;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;


public abstract class LiquidExperienceFluid extends BaseFlowingFluid {


    @Override
    public Item getBucket() {
        return ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get();
    }

    @Override
    public Fluid getFlowing() {
        return FluidRegistry.FLOWING_LIQUID_EXPERIENCE.get();
    }

    @Override
    public Fluid getSource() {
        return FluidRegistry.LIQUID_EXPERIENCE.get();
    }


    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return BlockRegistry.LIQUID_EXPERIENCE.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }


    public static class Flowing extends LiquidExperienceFluid {

        public static LiquidExperienceFluid getFluid() {
            return PlatformCompatibility.getFluid(true);
        }

        public static ResourceLocation getResourceLocation() {
            return new ResourceLocation(MobFarmUtilities.MODID, "block/liquid_experience/flow");
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            builder.add(LEVEL);
            super.createFluidStateDefinition(builder);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

    }

    public static class Still extends LiquidExperienceFluid {

        public static LiquidExperienceFluid getFluid() {
            return PlatformCompatibility.getFluid(false);
        }

        public static ResourceLocation getResourceLocation() {
            return new ResourceLocation(MobFarmUtilities.MODID, "block/liquid_experience/still");
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

    }


}
