/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.movement.speeds.other

import net.minusmc.minusbounce.features.module.modules.movement.speeds.SpeedMode
import net.minusmc.minusbounce.utils.player.MovementUtils
import net.minusmc.minusbounce.value.BoolValue
import net.minecraft.client.settings.GameSettings
import net.minusmc.minusbounce.features.module.modules.movement.speeds.SpeedType

class LegitSpeed: SpeedMode("Legit", SpeedType.OTHER) {

    private val cpuSPEED = BoolValue("Legit-AlanWOOD-CPU-BYPASS-$$$$", true)

    private var wasOnGround = false

    override fun onUpdate() {
        if (cpuSPEED.get()) mc.timer.timerSpeed = 1.004f
        if (MovementUtils.isMoving) {
            if (mc.thePlayer.onGround) {
                mc.gameSettings.keyBindJump.pressed = true
                wasOnGround = true
            } else {
                if (wasOnGround) {
                    mc.gameSettings.keyBindJump.pressed = false
                    wasOnGround = false
                } else {
                    mc.gameSettings.keyBindJump.pressed = true
                }
            }
        }
    }

    override fun onDisable() {
        mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump)
        if (cpuSPEED.get()) mc.timer.timerSpeed = 1f
    }
}