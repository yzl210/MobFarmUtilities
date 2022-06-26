package cn.leomc.mobfarmutilities.fabric.compat.wthit;


import cn.leomc.mobfarmutilities.common.compat.InfoList;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;

public class WTHITCompat implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        InfoList.INFO_PROVIDERS.forEach(blockClass -> registrar.addComponent(new ComponentProvider(), TooltipPosition.BODY, blockClass));
    }
}
