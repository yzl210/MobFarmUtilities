package cn.leomc.mobfarmutilities.common.api;

import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeItem;


public class FanUpgradeItemStackHandler extends UpgradeItemStackHandler {

    public FanUpgradeItemStackHandler() {
        super(UpgradeItem.Type.FAN_SPEED, UpgradeItem.Type.FAN_DISTANCE, UpgradeItem.Type.FAN_WIDTH, UpgradeItem.Type.FAN_HEIGHT);
    }


}
