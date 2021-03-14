package cn.leomc.mobfarmutilities.forge;

import cn.leomc.mobfarmutilities.common.blockentity.SlaughtererBlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeSlaughtererBlockEntity extends SlaughtererBlockEntity {

    protected LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(upgradeHandler.getInventory()));

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemHandler.cast();
        return super.getCapability(cap, side);
    }

}
