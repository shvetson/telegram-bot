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

fun getCommandResponse(chatId: String, text: String, firstName: String,): SendMessage {
    return when (text) {
        Command.START.command -> {
            handleStartCommand(chatId = chatId, firstName = firstName)
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
    return sendMessage(chatId = chatId, text = text, keyboard = getInlineKeyboardMenu())
}

private fun handleHelpCommand(chatId: String): SendMessage {
    return sendMessage(chatId = chatId, text = HELP_TEXT)
}

private fun handleSettingsCommand(chatId: String): SendMessage {
    return sendMessage(chatId = chatId, text = SORRY_TEXT)
}

private fun handleNotFoundCommand(chatId: String): SendMessage {
    return sendMessage(chatId = chatId, text = "*${SORRY_TEXT}*")
}

private fun getInlineKeyboardMenu(): InlineKeyboardMarkup {
    val menuButton = getButton(text = "Go to Todos", callbackData = CommandCallback.MENU.command)
    val requireButton = getButton(text = "Go to require", callbackData = "require")
    val rowButtons1 = getRow(menuButton, requireButton)

    val testButton = getButton(text = "Go to test", callbackData = "test")
    val rowButtons2 = getRow(testButton)

    val collection = getCollection(rowButtons1, rowButtons2)

    return getKeyboard(collection)
}