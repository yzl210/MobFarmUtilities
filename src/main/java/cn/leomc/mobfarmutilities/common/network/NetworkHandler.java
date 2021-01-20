package cn.leomc.mobfarmutilities.common.network;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.network.message.RedstoneModeChangeMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MobFarmUtilities.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        int id = 0;
        INSTANCE.registerMessage(id++, RedstoneModeChangeMessage.class, RedstoneModeChangeMessage::encode, RedstoneModeChangeMessage::decode, RedstoneModeChangeMessage::handle);
    }

}
