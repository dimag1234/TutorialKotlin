package org.tutorial.Item



import net.minecraft.block.AbstractBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class RubyBlock(settings: AbstractBlock.Settings) : net.minecraft.block.Block(settings) {

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) return ActionResult.PASS

        player.sendMessage(Text.literal("§cРубиновый блок активирован! §e+10 уровней"), true)
        player.addExperienceLevels(10)

        // Правильный звук для 1.21.8 (громкий, слышно всем)
        world.playSound(
            null,
            pos,
            SoundEvents.ENTITY_PLAYER_LEVELUP,
            net.minecraft.sound.SoundCategory.PLAYERS,
            3.0f,
            1.5f
        )
        world.setBlockState(pos, Blocks.EMERALD_BLOCK.defaultState)

        return ActionResult.SUCCESS
    }
}