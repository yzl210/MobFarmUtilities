package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.TickableBlockEntity;
import dev.architectury.hooks.block.BlockEntityHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class BaseBlockEntity extends BlockEntity implements TickableBlockEntity {
    public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void syncData() {
        BlockEntityHooks.syncData(this);
    }

    @Override
    protected final void saveAdditional(@NotNull CompoundTag tag) {
        saveData(tag);
    }

    @Override
    public final void load(@NotNull CompoundTag tag) {
        if (tag.contains("isUpdatePacket") && tag.getBoolean("isUpdatePacket"))
            loadClientData(tag);
        else
            loadData(tag);
    }

    public abstract void loadData(CompoundTag tag);

    public abstract void saveData(CompoundTag tag);

    public void loadClientData(CompoundTag tag) {
    }


    public void saveClientData(CompoundTag tag) {
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveClientData(tag);
        tag.putBoolean("isUpdatePacket", true);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}