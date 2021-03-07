package cn.leomc.mobfarmutilities.forge.compat.jade;

import cn.leomc.mobfarmutilities.common.compat.InfoProvider;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ComponentProvider extends InfoProvider implements IComponentProvider {

    @Override
    public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
        appendInfo(tooltip, accessor.getTileEntity());
    }
}
