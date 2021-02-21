package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.block.FanBlock;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class BlockRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MobFarmUtilities.MODID, Registry.BLOCK_KEY);


    public static final RegistrySupplier<Block> FAN = register("fan", FanBlock::new);


    public static void register() {
        BLOCKS.register();
    }

    private static RegistrySupplier<Block> register(String name, Supplier<? extends Block> supplier) {
        return BLOCKS.register(name, supplier);
    }

}
