package cn.leomc.mobfarmutilities;

import cn.leomc.mobfarmutilities.client.ClientProxy;
import cn.leomc.mobfarmutilities.common.api.IProxy;
import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import cn.leomc.mobfarmutilities.server.ServerProxy;
import me.shedaniel.architectury.event.events.TextureStitchEvent;
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
        TextureStitchEvent.PRE.register(this::onTextureStitch);
        ModRegistry.register();
    }

    @Environment(EnvType.CLIENT)
    private void onTextureStitch(AtlasTexture atlasTexture, Consumer<ResourceLocation> spriteAdder) {
        if (atlasTexture.getTextureLocation() == PlayerContainer.LOCATION_BLOCKS_TEXTURE) {
            spriteAdder.accept(new ResourceLocation(MODID, "gui/generic"));
            spriteAdder.accept(new ResourceLocation(MODID, "gui/slot_small"));
        }
    }


}
