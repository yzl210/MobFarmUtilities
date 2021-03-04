package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.api.InventoryWrapper;
import cn.leomc.mobfarmutilities.common.api.blockstate.IHasDirection;
import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;


public class FanBlock extends ActivatableBlock implements ITileEntityProvider, IHasDirection, ISidedInventoryProvider {

    public FanBlock() {
        super(BlockProperties.of(Material.IRON)
                .tool(ToolType.PICKAXE, 1)
                .setRequiresTool()
                .hardnessAndResistance(1.5F, 6.0F)
        );
    }

    public static FanTileEntity getTileEntity() {
        if (Platform.isForge()) {
            Class<FanTileEntity> tileEntityClass;
            try {
                tileEntityClass = (Class<FanTileEntity>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeFanTileEntity");
                return tileEntityClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new FanTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote || handIn != Hand.MAIN_HAND)
            return ActionResultType.CONSUME;

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            if (player instanceof ServerPlayerEntity)
                MenuRegistry.openExtendedMenu((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, packetBuffer -> packetBuffer.writeBlockPos(tileEntity.getPos()));
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
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDirection(context, super.getStateForPlacement(context));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return getTileEntity();
    }

    @Override
    public ISidedInventory createInventory(BlockState state, IWorld world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof FanTileEntity) {
            return new InventoryWrapper(((FanTileEntity) tileEntity).getUpgradeHandler().getInventory());
        }
        return null;
    }

}
