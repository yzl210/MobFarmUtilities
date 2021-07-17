package cn.leomc.mobfarmutilities.forge;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ForgeBucketItem extends BucketItem {

    public static Field CONTENT_FIELD;

    static {
        try {
            CONTENT_FIELD = ObfuscationReflectionHelper.findField(BucketItem.class, "content");
        } catch (ObfuscationReflectionHelper.UnableToFindFieldException e) {
            e.printStackTrace();
            for (Field field : BucketItem.class.getDeclaredFields()) {
                if(Fluid.class.isAssignableFrom(field.getType())){
                    field.setAccessible(true);
                    CONTENT_FIELD = field;
                }
            }
        }
    }

    public ForgeBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        trySetFluid();
        return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    private void trySetFluid(){
        if(super.getFluid() == null)
            return;
        try {
            CONTENT_FIELD.set(this, super.getFluid());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
