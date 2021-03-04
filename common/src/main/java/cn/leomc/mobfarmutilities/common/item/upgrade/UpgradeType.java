package cn.leomc.mobfarmutilities.common.item.upgrade;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.ITranslatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public enum UpgradeType implements ITranslatable {

    FAN_DISTANCE(10, 2, Items.SUGAR, null),
    FAN_WIDTH(10, 4, Items.IRON_INGOT, new ResourceLocation("forge", "ingots/iron")),
    FAN_HEIGHT(10, 4, Items.REDSTONE, new ResourceLocation("forge", "dusts/redstone")),
    FAN_SPEED(5, 4, Items.FEATHER, new ResourceLocation("forge", "feathers"));

    private final int maxLevel;
    private final int requiredCount;
    private final Item delegate;
    private final ResourceLocation tagRL;
    private final ITag<Item> tag;

    UpgradeType(int maxLevel, int requiredCount, Item delegate, ResourceLocation tag) {
        this.maxLevel = maxLevel;
        this.requiredCount = requiredCount;
        this.delegate = delegate;
        this.tagRL = tag;
        this.tag = ItemTags.getCollection().get(tag);
    }


    public int getMaxLevel() {
        return maxLevel;
    }

    public List<Item> getSupportedItems() {
        List<Item> itemList = new ArrayList<>();
        if (tag != null)
            itemList.addAll(tag.getAllElements());
        if (delegate != null && !itemList.contains(delegate))
            itemList.add(delegate);
        return itemList;
    }

    public ResourceLocation getTagResourceLocation() {
        return tagRL;
    }

    public String getTagName() {
        return tagRL == null ? "" : tagRL.toString();
    }

    public ITag<Item> getTag() {
        return tag;
    }

    public Item getDelegate() {
        return delegate;
    }

    public int getRequiredCount() {
        return requiredCount;
    }

    public boolean isEnough(ItemStack itemStack) {
        return getSupportedItems().contains(itemStack.getItem()) && itemStack.getCount() >= getRequiredCount();
    }


    @Override
    public String getTranslationKey() {
        return "text." + MobFarmUtilities.MODID + ".upgrade." + name().toLowerCase();
    }

    @Environment(EnvType.CLIENT)
    public String getLocalizedName() {
        return I18n.format(getTranslationKey());
    }


}
