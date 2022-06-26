package cn.leomc.mobfarmutilities.common.network;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.network.message.ChangeExperienceMessage;
import cn.leomc.mobfarmutilities.common.network.message.ChangeGradeMessage;
import cn.leomc.mobfarmutilities.common.network.message.RedstoneModeChangeMessage;
import cn.leomc.mobfarmutilities.common.network.message.SyncDataMessage;
import dev.architectury.networking.NetworkChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;


public class NetworkHandler {
    public static NetworkChannel INSTANCE;

    public static void register() {
        INSTANCE = NetworkChannel.create(new ResourceLocation(MobFarmUtilities.MODID, "main"));

        INSTANCE.register(RedstoneModeChangeMessage.class, RedstoneModeChangeMessage::encode, RedstoneModeChangeMessage::decode, RedstoneModeChangeMessage::handle);
        INSTANCE.register(ChangeGradeMessage.class, ChangeGradeMessage::encode, ChangeGradeMessage::decode, ChangeGradeMessage::handle);
        INSTANCE.register(ChangeExperienceMessage.class, ChangeExperienceMessage::encode, ChangeExperienceMessage::decode, ChangeExperienceMessage::handle);
        INSTANCE.register(SyncDataMessage.class, SyncDataMessage::encode, SyncDataMessage::decode, SyncDataMessage::handle);
    }

    @Environment(EnvType.CLIENT)
    public static void syncData(BlockPos pos){
        INSTANCE.sendToServer(new SyncDataMessage(pos));
    }


}
