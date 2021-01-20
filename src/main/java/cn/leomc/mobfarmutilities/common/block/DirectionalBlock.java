package cn.leomc.mobfarmutilities.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;

public class DirectionalBlock extends net.minecraft.block.DirectionalBlock {
    protected DirectionalBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking())
            return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

}
