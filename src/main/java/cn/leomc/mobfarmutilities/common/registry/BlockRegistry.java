package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.block.FanBlock;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MobFarmUtilities.MODID);


    public static final RegistryObject<Block> FAN = register("fan", FanBlock::new);


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static RegistryObject<Block> register(String name, Supplier<? extends Block> supplier) {
        return BLOCKS.register(name, supplier);
    }

}
