package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.client.utils.TextureUtils;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum RedstoneMode implements ITranslatable, IStringSerializable {
    IGNORED,
    HIGH,
    LOW;

    @Environment(EnvType.CLIENT)
    private net.minecraft.client.renderer.texture.TextureAtlasSprite sprite;

    public static String getModeTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".mode";
    }

    public static void updateRedstone(BlockState state, World world, BlockPos pos) {
        RedstoneMode mode = state.get(ActivatableBlock.MODE);
        if (mode == RedstoneMode.HIGH)
            if (world.isBlockPowered(pos))
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, true));
            else
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, false));
        if (mode == RedstoneMode.LOW)
            if (world.isBlockPowered(pos))
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, false));
            else
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, true));
        if (mode == RedstoneMode.IGNORED)
            world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, true));
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
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getTextureAtlasSprite() {
        if (sprite == null)
            sprite = TextureUtils.getAtlasTexture(getTextureResourceLocation());
        return sprite;
    }

}
