package cn.leomc.mobfarmutilities.forge.compat.jade;

import cn.leomc.mobfarmutilities.common.compat.InfoProvider;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;

public class ComponentProvider extends InfoProvider implements IComponentProvider {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        appendInfo(iTooltip::add, accessor.getBlockEntity());
    }
}
