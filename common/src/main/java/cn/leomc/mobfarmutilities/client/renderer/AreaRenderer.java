package cn.leomc.mobfarmutilities.client.renderer;

import cn.leomc.mobfarmutilities.client.utils.TextureUtils;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class AreaRenderer<T extends BlockEntity> extends BlockEntityRenderer<T> {
    int syncCount = 0;

    public AreaRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    @Override
    public void render(T blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (blockEntity instanceof IHasArea && ((IHasArea) blockEntity).doRenderArea()) {
            if (syncCount <= 0 && blockEntity instanceof BlockEntityExtension) {
                NetworkHandler.syncData(blockEntity.getBlockPos());
                syncCount = 20;
            }
            syncCount--;
            AABB aabb = ((IHasArea) blockEntity).getRenderAABB();
            if (aabb != null)
                TextureUtils.drawAABB(poseStack, multiBufferSource, aabb);

        }
    }
}
