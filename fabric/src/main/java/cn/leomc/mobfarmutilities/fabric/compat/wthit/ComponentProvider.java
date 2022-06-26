package cn.leomc.mobfarmutilities.fabric.compat.wthit;

import cn.leomc.mobfarmutilities.common.compat.InfoProvider;
import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;

public class ComponentProvider extends InfoProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        appendInfo(tooltip::addLine, accessor.getBlockEntity());
    }
}
