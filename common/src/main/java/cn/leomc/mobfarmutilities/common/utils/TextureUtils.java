package cn.leomc.mobfarmutilities.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

public class TextureUtils {

    public static TextureAtlasSprite getAtlasTexture(ResourceLocation rl) {
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(rl);
    }

}
