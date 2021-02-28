package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class FluidRegistry {

    protected static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.FLUID_KEY);


    public static RegistrySupplier<Fluid> LIQUID_EXPERIENCE = register("liquid_experience", LiquidExperienceFluid.Still::getFluid);
    public static RegistrySupplier<Fluid> FLOWING_LIQUID_EXPERIENCE = register("flowing_liquid_experience", LiquidExperienceFluid.Flowing::getFluid);


    public static void register() {
        FLUIDS.register();
    }

    private static RegistrySupplier<Fluid> register(String name, Supplier<? extends Fluid> supplier) {
        return FLUIDS.register(name, supplier);
    }

}
