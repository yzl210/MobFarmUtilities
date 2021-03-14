package cn.leomc.mobfarmutilities.common.mixin;

import cn.leomc.mobfarmutilities.common.api.FakePlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerAdvancements {

    @Inject(
            method = "getPlayerAdvancements",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getPlayerAdvancements(ServerPlayer serverPlayer, CallbackInfoReturnable<net.minecraft.server.PlayerAdvancements> cir) {
        if (serverPlayer instanceof FakePlayer)
            cir.cancel();
    }

}
