package cn.leomc.mobfarmutilities.common.utils;

import cn.leomc.mobfarmutilities.common.api.FakePlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ConcurrentHashMap;

public class FakePlayers {

    private static final ConcurrentHashMap<GameProfile, FakePlayer> FAKE_PLAYERS = new ConcurrentHashMap<>();

    public static FakePlayer getOrCreate(ServerLevel level, GameProfile gameProfile) {
        return getOrCreate(level, gameProfile, new Vec3(0.0D, 4096.0D, 0.0D));
    }

    public static FakePlayer getOrCreate(ServerLevel level, GameProfile gameProfile, Vec3 pos) {
        if (!FAKE_PLAYERS.containsKey(gameProfile))
            FAKE_PLAYERS.put(gameProfile, new FakePlayer(level, gameProfile, pos));
        return get(gameProfile);
    }

    public static FakePlayer get(GameProfile gameProfile) {
        return FAKE_PLAYERS.get(gameProfile);
    }

    public static void remove(GameProfile gameProfile) {
        if (FAKE_PLAYERS.containsKey(gameProfile)) {
            FAKE_PLAYERS.get(gameProfile).remove();
            FAKE_PLAYERS.remove(gameProfile);
        }
    }


    public static void onWorldUnloaded(ServerLevel level) {
        FAKE_PLAYERS.forEach((gameProfile, fakePlayer) -> {
            if (fakePlayer.level == level) {
                remove(gameProfile);
            }
        });
    }
}
