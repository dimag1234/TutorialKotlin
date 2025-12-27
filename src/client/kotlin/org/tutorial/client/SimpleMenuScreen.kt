package org.tutorial.client

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text
import net.minecraft.client.MinecraftClient

class SimpleMenuScreen : Screen(Text.literal("§b§lПростое меню")) {

    override fun init() {
        // ОБЯЗАТЕЛЬНО ПЕРВЫМ!
        super.init()

        // Кнопка "Закрыть"
        val closeButton = ButtonWidget.builder(Text.literal("§cЗакрыть")) { _ ->
            MinecraftClient.getInstance().setScreen(null) // Закрываем меню
        }
            .position(width / 2 - 50, height / 2 - 10) // Центр
            .size(100, 20)
            .build()

        addDrawableChild(closeButton)

        // Дополнительная кнопка для теста
        val testButton = ButtonWidget.builder(Text.literal("§aНажми меня")) { _ ->
            client?.player?.sendMessage(Text.literal("§eКнопка работает!"), false)
        }
            .position(width / 2 - 50, height / 2 + 20)
            .size(100, 20)
            .build()

        addDrawableChild(testButton)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Полупрозрачный фон (как в твоём коде)
        context.fill(0, 0, width, height, 0xC0101010.toInt())

        // Заголовок
        context.drawText(
            textRenderer,
            title,
            width / 2 - textRenderer.getWidth(title) / 2,
            40,
            0xFFFF55,
            true
        )

        // Текст под заголовком
        context.drawText(
            textRenderer,
            Text.literal("§7Меню работает идеально!"),
            width / 2 - textRenderer.getWidth("§7Меню работает идеально!") / 2,
            70,
            0xAAAAAA,
            true
        )

        // Рендер кнопок
        super.render(context, mouseX, mouseY, delta)
    }

    // Не паузим игру
    override fun shouldPause() = false

    // Закрытие по Esc
    override fun shouldCloseOnEsc() = true
}