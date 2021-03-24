package cn.leomc.mobfarmutilities.forge.compat.top;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.compat.IInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;

import java.util.function.Function;

public class TOPCompat {

    public static boolean enabled;

    public static void enable() {
        if (enabled)
            return;
        enabled = true;
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        @Override
        public Void apply(ITheOneProbe iTheOneProbe) {
            iTheOneProbe.registerProvider(new IProbeInfoProvider() {
                @Override
                public String getID() {
                    return MobFarmUtilities.MODID + ":top_plugin";
                }

                @Override
                public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
                    BlockEntity blockEntity = level.getBlockEntity(iProbeHitData.getPos());
                    if (blockEntity instanceof IInfoProvider)
                        ((IInfoProvider) blockEntity).getInfo().forEach(iProbeInfo::text);
                }
            });
            return null;
        }
    }
}
