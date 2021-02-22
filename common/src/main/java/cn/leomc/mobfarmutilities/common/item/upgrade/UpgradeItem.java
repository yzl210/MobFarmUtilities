package cn.leomc.mobfarmutilities.common.item.upgrade;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.ITranslatable;
import cn.leomc.mobfarmutilities.common.registry.ModRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class UpgradeItem extends Item {

    private final Type type;

    public UpgradeItem(Type type) {
        super(new Properties().maxStackSize(type.getMaxPerStack()).group(ModRegistry.ITEM_GROUP));
        this.type = type;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(type.getDescription());
    }

    public Type getType() {
        return type;
    }

    public enum Type implements ITranslatable {
        FAN_DISTANCE(10),
        FAN_WIDTH(10),
        FAN_HEIGHT(10),
        FAN_SPEED(5);

        private final int max;
        private RegistrySupplier<Item> registrySupplier;

        Type(int max) {
            this.max = max;
        }

        public int getMaxPerStack() {
            return max;
        }

        public String getRegistryName() {
            return "upgrade_" + name().toLowerCase();
        }

        @Override
        public String getTranslationKey() {
            return "item." + MobFarmUtilities.MODID + "." + getRegistryName();
        }

        public ITextComponent getDescription() {
            return new TranslationTextComponent("text." + MobFarmUtilities.MODID + "." + getRegistryName(), max);
        }

        public RegistrySupplier<Item> getRegistrySupplier() {
            return registrySupplier;
        }

        public void setRegistrySupplier(RegistrySupplier<Item> registrySupplier) {
            this.registrySupplier = registrySupplier;
        }
    }

}
