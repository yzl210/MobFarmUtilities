package cn.leomc.mobfarmutilities;

import cn.leomc.mobfarmutilities.client.ClientProxy;
import cn.leomc.mobfarmutilities.client.utils.Textures;
import cn.leomc.mobfarmutilities.common.api.IProxy;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import cn.leomc.mobfarmutilities.server.ServerProxy;
import me.shedaniel.architectury.event.events.TextureStitchEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class MobFarmUtilities {

    public static final String MODID = "mobfarmutilities";

    public static final Logger LOGGER = LogManager.getLogger();

    public static IProxy PROXY = EnvExecutor.getEnvSpecific(() -> ClientProxy::new, () -> ServerProxy::new);

    public MobFarmUtilities() {
        FluidRegistry.register();
        ModRegistry.register();
        if (Platform.getEnvironment() == Env.CLIENT) {
            TextureStitchEvent.PRE.register(this::onPreTextureStitch);
            TextureStitchEvent.POST.register(this::onPostTextureStitch);
        }
    }


    @Environment(EnvType.CLIENT)
    private void onPreTextureStitch(AtlasTexture atlasTexture, Consumer<ResourceLocation> spriteAdder) {
        if (atlasTexture.getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {
            Textures.REGISTRIES.forEach(spriteAdder);
        }
    }

    @Environment(EnvType.CLIENT)
    private void onPostTextureStitch(AtlasTexture atlasTexture) {
        if (atlasTexture.getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {
            Textures.REGISTRIES.forEach(rl -> {
                Textures.TEXTURE_MAP.put(rl, atlasTexture.getSprite(rl));
            });
        }
    }


}
