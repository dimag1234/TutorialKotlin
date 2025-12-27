package org.tutorial.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory
import kotlin.math.sin


class TutorialClient : ClientModInitializer {

    private var isPitchAnimating = false
    private var animationStartTime = 0L
    private val animationDurationMs = 3000L  // 3 секунды — ультраплавно даже на 1000 тиках
    private val startPitch = -90f
    private val endPitch = 90f
    private val logger = LoggerFactory.getLogger("TutorialClient")
    private lateinit var openMenuKeyBinding: KeyBinding
    var open = false

    override fun onInitializeClient() {
        logger.info("TutorialClient загружен!") // Проверка в логах
        // Команда активации
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal("activate")
                    .executes { ctx ->
                        startPitchAnimation()
                        ctx.source.sendFeedback(Text.literal("§aУльтраплавный поворот pitch запущен (3 секунды)"))
                        1
                    }
            )
        }

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal("menu")
                    .executes { ctx ->
                        open = true
                        ctx.source.sendFeedback(Text.literal("§aМеню открыто командой!"))
                        1
                    }
            )
        }



        openMenuKeyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.modid.open_menu", // Локализованное название
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O, // Клавиша O
                "category.modid.main" // Категория в настройках управления
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (openMenuKeyBinding.wasPressed() || open) {
                // Открываем наш кастомный экран
                client.setScreen(SimpleMenuScreen())
                open = false
            }
        }

        // Используем WorldRenderEvents.AFTER_ENTITIES — обновление каждый рендер-фрейм (60+ FPS)
        // Это даёт настоящую плавность, а не 20 тиков/сек
        WorldRenderEvents.AFTER_ENTITIES.register { context ->
            val player = MinecraftClient.getInstance().player ?: return@register

            if (!isPitchAnimating) return@register

            val currentTime = System.nanoTime() / 1_000_000  // миллисекунды
            val elapsed = currentTime - animationStartTime

            if (elapsed >= animationDurationMs) {
                isPitchAnimating = false
                player.pitch = endPitch
                player.sendMessage(Text.literal("§aПоворот завершён!"), false)
                return@register
            }

            // Прогресс 0.0 → 1.0 на основе реального времени
            val progress = elapsed.toFloat() / animationDurationMs.toFloat()

            // Cubic ease-in-out + дополнительный smoothstep — ультраплавно, без рывков даже на 1000 тиках
            val smoothProgress = progress * progress * progress * (progress * (6f * progress - 15f) + 10f)  // Smoothstep + cubic
            val finalProgress = sin(smoothProgress * Math.PI.toFloat() / 2f)  // Sine для идеального ease

            player.pitch = MathHelper.lerp(finalProgress, startPitch, endPitch)
        }
    }

    fun startPitchAnimation() {
        isPitchAnimating = true
        animationStartTime = System.nanoTime() / 1_000_000
        MinecraftClient.getInstance().player?.pitch = startPitch
    }
}


