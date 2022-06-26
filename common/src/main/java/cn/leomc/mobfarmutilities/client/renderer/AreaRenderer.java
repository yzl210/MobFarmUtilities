package cn.leomc.mobfarmutilities.client.renderer;

import cn.leomc.mobfarmutilities.client.utils.TextureUtils;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.blockentity.BaseBlockEntity;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class AreaRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private int syncCount = 0;

    public AreaRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull T blockEntity, float f, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        if (blockEntity instanceof IHasArea iHasArea && iHasArea.doRenderArea()) {
            if (syncCount <= 0 && blockEntity instanceof BaseBlockEntity) {
                NetworkHandler.syncData(blockEntity.getBlockPos());
                syncCount = 20;
            }
            syncCount--;
            AABB aabb = iHasArea.getRenderAABB();
            if (aabb != null)
                TextureUtils.drawAABB(poseStack, multiBufferSource, aabb);
        }
    }
}
