package org.tutorial

import org.tutorial.Item.CustomRubyItem
import net.fabricmc.api.ModInitializer
//import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Blocks
import net.minecraft.block.MapColor
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import org.slf4j.LoggerFactory
import org.tutorial.Item.RubyBlock


class Tutorial : ModInitializer {


    companion object {
        const val MOD_ID = "tutorial"
        private val logger = LoggerFactory.getLogger(MOD_ID)
    }

    override fun onInitialize() {
        logger.info("[$MOD_ID] Инициализация мода...")


        items()

        logger.info("[$MOD_ID] Инициализация завершена.")

    }


    fun items() {

        // Ключ для блока
        val RUBY_BLOCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "rubyblock"))

        // Регистрация блока с фабрикой (фиксит NPE в 1.21.8)
        val RUBY_BLOCK = Blocks.register(
            RUBY_BLOCK_KEY,
            { settings: AbstractBlock.Settings -> RubyBlock(settings) },
            AbstractBlock.Settings.create()
                .mapColor(MapColor.BRIGHT_RED)
                .strength(5.0f, 6.0f)
                .requiresTool()
                .luminance { 8 }
                .sounds(BlockSoundGroup.AMETHYST_BLOCK)
        )

        // Ключ для BlockItem
        val RUBY_BLOCK_ITEM_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "ruby_block"))

        // Регистрация BlockItem с настройками
        val RUBY_BLOCK_ITEM = Items.register(
            RUBY_BLOCK_ITEM_KEY,
            { settings: net.minecraft.item.Item.Settings -> net.minecraft.item.BlockItem(RUBY_BLOCK, settings) },
            net.minecraft.item.Item.Settings().fireproof()
        )

        // Регистрация с правильным способом для 1.21.8 (избегает NPE)
        val rubyKey: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "ruby"))
        val rubyItem = Items.register(
            rubyKey,
            { settings: Item.Settings -> CustomRubyItem(settings) },  // Фабрика
            Item.Settings().maxCount(64).fireproof()
        ) as CustomRubyItem

        // Добавляем в творческую вкладку
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register { entries ->
            entries.add(rubyItem)
        }

    }
}
