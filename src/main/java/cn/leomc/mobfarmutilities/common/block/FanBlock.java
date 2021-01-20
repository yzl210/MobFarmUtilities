package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;


public class FanBlock extends ActivatableBlock {

    public FanBlock() {
        super(Properties.create(Material.IRON)
                .setRequiresTool()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .hardnessAndResistance(1.5F, 6.0F)
        );
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote || handIn != Hand.MAIN_HAND)
            return ActionResultType.CONSUME;

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            if (player instanceof ServerPlayerEntity)
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            return ActionResultType.SUCCESS;
        } else
            throw new IllegalStateException("Named Container not found!");
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock())
            return;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof FanTileEntity) {
            ((FanTileEntity) tileEntity).dropAllItems();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FanTileEntity();
    }
}
