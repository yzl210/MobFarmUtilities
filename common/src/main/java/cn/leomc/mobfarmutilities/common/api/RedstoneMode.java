package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.utils.TextureUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public enum RedstoneMode implements ITranslatable, IStringSerializable {
    IGNORED,
    HIGH,
    LOW;

    @Environment(EnvType.CLIENT)
    private TextureAtlasSprite sprite;

    public static String getModeTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".mode";
    }

    public RedstoneMode next() {
        switch (this) {
            case HIGH:
                return LOW;
            case LOW:
                return IGNORED;
            case IGNORED:
                return HIGH;
            default:
                throw new IllegalStateException("What am I?");
        }
    }

    @Override
    public String getString() {
        return toString().toLowerCase();
    }

    @Override
    public String getTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".mode." + getString();
    }

    public ResourceLocation getTextureResourceLocation() {
        switch (this) {
            case HIGH:
                return new ResourceLocation("minecraft", "block/redstone_torch");
            case LOW:
                return new ResourceLocation("minecraft", "block/redstone_torch_off");
            case IGNORED:
                return new ResourceLocation("minecraft", "item/gunpowder");
            default:
                throw new IllegalStateException("What am I?");
        }
    }

    @Environment(EnvType.CLIENT)
    public TextureAtlasSprite getTextureAtlasSprite() {
        if (sprite == null)
            sprite = TextureUtils.getAtlasTexture(getTextureResourceLocation());
        return sprite;
    }

}
