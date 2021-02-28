package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.block.ExperienceCollectorBlock;
import cn.leomc.mobfarmutilities.common.block.FanBlock;
import cn.leomc.mobfarmutilities.common.block.ItemCollectorBlock;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class BlockRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.BLOCK_KEY);

    public static final RegistrySupplier<Block> FAN = register("fan", FanBlock::new);

    public static final RegistrySupplier<Block> ITEM_COLLECTOR = register("item_collector", ItemCollectorBlock::new);

    public static final RegistrySupplier<Block> EXPERIENCE_COLLECTOR = register("experience_collector", ExperienceCollectorBlock::new);


    //Fluid

    public static final RegistrySupplier<Block> LIQUID_EXPERIENCE = register("liquid_experience", () -> PlatformCompatibility.getFlowingFluidBlock(FluidRegistry.LIQUID_EXPERIENCE, AbstractBlock.Properties.create(Material.WATER).hardnessAndResistance(100F).noDrops()));


    public static void register() {
        BLOCKS.register();
    }

    private static RegistrySupplier<Block> register(String name, Supplier<? extends Block> supplier) {
        return BLOCKS.register(name, supplier);
    }

}
