package cn.leomc.mobfarmutilities.common.api.blockstate;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;

public interface IHasDirection {

    DirectionProperty FACING = BlockStateProperties.FACING;

    default BlockState getDirection(BlockItemUseContext context, BlockState blockState) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking())
            return blockState.with(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
        return blockState.with(FACING, context.getNearestLookingDirection().getOpposite());
    }

}
