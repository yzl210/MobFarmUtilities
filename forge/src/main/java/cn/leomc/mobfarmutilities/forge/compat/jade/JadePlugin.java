package cn.leomc.mobfarmutilities.forge.compat.jade;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.compat.InfoList;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(MobFarmUtilities.MODID)
public class JadePlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        InfoList.INFO_PROVIDERS.forEach(blockClass -> registrar.registerComponentProvider(new ComponentProvider(), TooltipPosition.BODY, blockClass));
    }
}
