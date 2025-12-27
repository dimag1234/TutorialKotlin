package org.tutorial.Item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


class CustomRubyItem(settings: Settings) : Item(settings) {

    override fun hasGlint(stack: ItemStack): Boolean = true

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val player = context.player ?: return ActionResult.PASS
        if (context.world.isClient) return ActionResult.SUCCESS
        if (player.itemCooldownManager.isCoolingDown(player.activeItem)) {
            return ActionResult.PASS
        }

        player.sendMessage(Text.literal("§cРубин на блоке — +5 уровней!"), true)
        player.addExperienceLevels(5)
        player.playSoundToPlayer(SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 3.0f, 1.5f)
        return ActionResult.SUCCESS
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        if (world.isClient) return ActionResult.PASS
        if (user.itemCooldownManager.isCoolingDown(user.activeItem)) {
            return ActionResult.PASS
        }
        user.sendMessage(Text.literal("§6Магический рубин активирован! §c+20 HP"), true)
        user.heal(20.0f)
        user.abilities.allowFlying = true
        world.playSound(
            null,                               // игрок-источник (null = всем)
            user.x, user.y, user.z,             // позиция
            SoundEvents.BLOCK_BEACON_ACTIVATE,
            SoundCategory.PLAYERS,
            5.0f,                               // громкость
            1.5f                                // тон
        )
        return ActionResult.SUCCESS
    }
}