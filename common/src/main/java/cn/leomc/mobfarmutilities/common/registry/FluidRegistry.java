package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class FluidRegistry {

    protected static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.FLUID_REGISTRY);


    public static RegistrySupplier<FlowingFluid> LIQUID_EXPERIENCE = register("liquid_experience", LiquidExperienceFluid.Still::getFluid);
    public static RegistrySupplier<FlowingFluid> FLOWING_LIQUID_EXPERIENCE = register("flowing_liquid_experience", LiquidExperienceFluid.Flowing::getFluid);


    public static void register() {
        FLUIDS.register();
    }

    private static RegistrySupplier<FlowingFluid> register(String name, Supplier<? extends FlowingFluid> supplier) {
        return FLUIDS.register(name, supplier);
    }

}
