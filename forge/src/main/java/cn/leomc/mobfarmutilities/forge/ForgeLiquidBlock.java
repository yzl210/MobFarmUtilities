package cn.leomc.mobfarmutilities.forge;


import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ForgeLiquidBlock extends LiquidBlock {

    public static Field FLUID_FIELD;

    static {
        try {
            FLUID_FIELD = ObfuscationReflectionHelper.findField(LiquidBlock.class, "fluid");
        } catch (ObfuscationReflectionHelper.UnableToFindFieldException e) {
            e.printStackTrace();
            for (Field field : LiquidBlock.class.getDeclaredFields()) {
                if(FlowingFluid.class.isAssignableFrom(field.getType())){
                    field.setAccessible(true);
                    FLUID_FIELD = field;
                }
            }
        }
    }

    public ForgeLiquidBlock(Supplier<? extends FlowingFluid> supplier, Properties p_i48368_1_) {
        super(supplier, p_i48368_1_);
    }

    @Override
    public FlowingFluid getFluid() {
        trySetFluid();
        return super.getFluid();
    }

    private void trySetFluid(){
        if(super.getFluid() == null)
            return;
        try {
            FLUID_FIELD.set(this, super.getFluid());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
