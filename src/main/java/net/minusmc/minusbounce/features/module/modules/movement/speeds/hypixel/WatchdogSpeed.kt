package net.minusmc.minusbounce.features.module.modules.movement.speeds.hypixel

import net.minusmc.minusbounce.event.MoveEvent
import net.minusmc.minusbounce.features.module.modules.movement.speeds.SpeedMode
import net.minusmc.minusbounce.utils.player.MovementUtils
import net.minecraft.potion.Potion
import net.minusmc.minusbounce.features.module.modules.movement.speeds.SpeedType

class WatchdogSpeed: SpeedMode("Watchdog", SpeedType.HYPIXEL) {

    override fun onUpdate() {
        if (mc.thePlayer.onGround && MovementUtils.isMoving) {
            if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MovementUtils.strafe(0.5f)
            } else {
                MovementUtils.strafe(0.44f)
            }
            mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.42F, true).toDouble()
        }
    }

    override fun onMove(event: MoveEvent) {

    }
}