package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.block.ExperienceCollectorBlock;
import cn.leomc.mobfarmutilities.common.block.FanBlock;
import cn.leomc.mobfarmutilities.common.block.ItemCollectorBlock;
import cn.leomc.mobfarmutilities.common.block.SlaughtererBlock;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class BlockRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> FAN = register("fan", FanBlock::new);

    public static final RegistrySupplier<Block> ITEM_COLLECTOR = register("item_collector", ItemCollectorBlock::new);

    public static final RegistrySupplier<Block> EXPERIENCE_COLLECTOR = register("experience_collector", ExperienceCollectorBlock::new);

    public static final RegistrySupplier<Block> SLAUGHTERER = register("slaughterer", SlaughtererBlock::new);


    //Fluid

    public static final RegistrySupplier<Block> LIQUID_EXPERIENCE = register("liquid_experience", () -> PlatformCompatibility.getFlowingFluidBlock(FluidRegistry.LIQUID_EXPERIENCE, BlockBehaviour.Properties.of(Material.WATER).strength(100F).noDrops()));


    public static void register() {
        BLOCKS.register();
    }

    private static RegistrySupplier<Block> register(String name, Supplier<? extends Block> supplier) {
        return BLOCKS.register(name, supplier);
    }

}
