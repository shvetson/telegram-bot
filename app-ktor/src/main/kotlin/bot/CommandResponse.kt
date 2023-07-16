package ru.shvets.telegram.bot.app.ktor.bot

import com.vdurmont.emoji.EmojiParser
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.shvets.telegram.bot.common.helper.HELP_TEXT
import ru.shvets.telegram.bot.common.helper.SORRY_TEXT

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  15.07.2023 17:05
 */

fun getCommandResponse(cmd: String, firstName: String, chatId: String): SendMessage {
    return when (cmd) {
        Command.START.command -> {
            handleStartCommand(chatId, firstName)
        }

        Command.HELP.command -> {
            handleHelpCommand(chatId)
        }

        Command.SET.command -> {
            handleSettingsCommand(chatId)
        }

        else -> {
            handleNotFoundCommand(chatId)
        }
    }
}

private fun handleStartCommand(chatId: String, firstName: String): SendMessage {
    val text = EmojiParser
        .parseToUnicode("Привет, _${firstName}_! :wave: Я - Telegram bot :blush: \n*Доступные команды:*")
    return sendMessage(chatId, text, getInlineKeyboard())
}

private fun handleHelpCommand(chatId: String): SendMessage {
    return sendMessage(chatId, HELP_TEXT)
}

private fun handleSettingsCommand(chatId: String): SendMessage {
    return sendMessage(chatId, SORRY_TEXT)
}

private fun handleNotFoundCommand(chatId: String): SendMessage {
    return sendMessage(chatId, "*${SORRY_TEXT}*")
}

private fun getInlineKeyboard(): InlineKeyboardMarkup {
    val menuButton = getButton(text = "Go to menu", callbackData = "menu")
    val requireButton = getButton(text = "Go to require", callbackData = "require")
    val checkButton = getButton(text = "Go to check", callbackData = "check")
    val rowButtons1 = getRow(menuButton, requireButton, checkButton)

    val testButton = getButton(text = "Go to test", callbackData = "test")
    val rowButtons2 = getRow(testButton)

    val collection = getCollection(rowButtons1, rowButtons2)

    return getKeyboard(collection)
}