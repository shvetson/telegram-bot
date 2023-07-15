package ru.shvets.subscriber.bot.app.ktor.bot

import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

fun getButton(text: String, callbackData: String): InlineKeyboardButton {
    val button = InlineKeyboardButton()
    button.apply {
        this.text = text
        this.callbackData = callbackData
    }
    return button
}

fun getRow(vararg inlineKeyboardButtons: InlineKeyboardButton): List<InlineKeyboardButton> {
    val row: MutableList<InlineKeyboardButton> = ArrayList()
    row.addAll(inlineKeyboardButtons.toList())
    return row
}

fun getCollection(vararg inlineKeyboardButtons: List<InlineKeyboardButton>): List<List<InlineKeyboardButton>> {
    val collection: MutableList<List<InlineKeyboardButton>> = ArrayList()
    collection.addAll(inlineKeyboardButtons.toList())
    return collection
}

fun getKeyboard(collection: List<List<InlineKeyboardButton>>): InlineKeyboardMarkup {
    val inlineKeyboardMarkup = InlineKeyboardMarkup()
    inlineKeyboardMarkup.keyboard = collection
    return inlineKeyboardMarkup
}

fun sendMessage(chatId: String, message: String, keyboard: Any? = null): SendMessage {
    val sendMessage = SendMessage(chatId, message)
    sendMessage.parseMode = ParseMode.MARKDOWN
    sendMessage.disableWebPagePreview
    sendMessage.replyMarkup =
        when (keyboard) {
            is InlineKeyboardMarkup -> keyboard as InlineKeyboardMarkup
            is ReplyKeyboardMarkup -> keyboard as ReplyKeyboardMarkup
            else -> null
        }
    return sendMessage
}