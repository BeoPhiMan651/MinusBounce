/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.render

import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.Render2DEvent
import net.minusmc.minusbounce.event.Render3DEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.block.BlockUtils.canBeClicked
import net.minusmc.minusbounce.utils.block.BlockUtils.getBlock
import net.minusmc.minusbounce.utils.render.ColorUtils.rainbow
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.IntegerValue
import net.minecraft.block.Block
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11
import java.awt.Color

@ModuleInfo(name = "BlockOverlay", spacedName = "Block Overlay", description = "Allows you to change the design of the block overlay.", category = ModuleCategory.RENDER)
class BlockOverlay : Module() {
    private val colorRedValue = IntegerValue("R", 68, 0, 255)
    private val colorGreenValue = IntegerValue("G", 117, 0, 255)
    private val colorBlueValue = IntegerValue("B", 255, 0, 255)
    private val colorRainbow = BoolValue("Rainbow", false)
    val infoValue = BoolValue("Info", false)

    val currentBlock: BlockPos?
        get() {
            val blockPos = mc.objectMouseOver?.blockPos ?: return null

            if (canBeClicked(blockPos) && mc.theWorld.worldBorder.contains(blockPos))
                return blockPos

            return null
        }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val blockPos = currentBlock ?: return
        val block = mc.theWorld.getBlockState(blockPos).block ?: return
        val partialTicks = event.partialTicks
        val color = if (colorRainbow.get()) rainbow(0.4F) else Color(colorRedValue.get(),
            colorGreenValue.get(), colorBlueValue.get(), (0.4F * 255).toInt())

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        RenderUtils.glColor(color)
        GL11.glLineWidth(2F)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)

        block.setBlockBoundsBasedOnState(mc.theWorld, blockPos)

        val x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks
        val y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks
        val z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks

        val axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, blockPos)
            .expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026)
            .offset(-x, -y, -z)

        RenderUtils.drawSelectionBoundingBox(axisAlignedBB)
        RenderUtils.drawFilledBox(axisAlignedBB)
        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.resetColor()
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (infoValue.get()) {
            val blockPos = currentBlock ?: return
            val block = getBlock(blockPos) ?: return

            val info = "${block.localizedName} §7ID: ${Block.getIdFromBlock(block)}"
            val scaledResolution = ScaledResolution(mc)

            /*RenderUtils.drawBorderedRect(
                    scaledResolution.scaledWidth / 2 - 2F,
                    scaledResolution.scaledHeight / 2 + 5F,
                    scaledResolution.scaledWidth / 2 + Fonts.font40.getStringWidth(info) + 2F,
                    scaledResolution.scaledHeight / 2 + 16F,
                    3F, Color.BLACK.rgb, Color.BLACK.rgb
            )*/
            GlStateManager.resetColor()
            Fonts.fontSFUI40.drawCenteredString(info, scaledResolution.scaledWidth / 2F, scaledResolution.scaledHeight / 2F + 6F, Color.WHITE.rgb)
        }
    }
}