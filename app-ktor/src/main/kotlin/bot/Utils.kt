package ru.shvets.telegram.bot.app.ktor.bot

import com.vdurmont.emoji.EmojiParser
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.time.format.DateTimeFormatter

fun getButton(text: String, callbackData: String): InlineKeyboardButton {
    val button = InlineKeyboardButton()
    button.apply {
        this.text = text
        this.callbackData = callbackData
    }
    return button
}

fun getButtonWithEmoji(text: String, callbackData: String, emoji: String): InlineKeyboardButton {
    val button = InlineKeyboardButton()
    val str = EmojiParser.parseToUnicode("$emoji $text ")
    button.apply {
        this.text = str
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

fun editMessage(chatId: String, text: String, messageId: Int, keyboard: InlineKeyboardMarkup? = null): EditMessageText {
    return EditMessageText.builder()
        .chatId(chatId)
        .text(text)
        .messageId(messageId)
        .parseMode(ParseMode.MARKDOWN)
        .replyMarkup(keyboard)
        .build()
}

fun sendMessage(chatId: String, text: String, keyboard: Any? = null): SendMessage {
    val replyKeyboard = when (keyboard) {
        is InlineKeyboardMarkup -> keyboard as InlineKeyboardMarkup
        is ReplyKeyboardMarkup -> keyboard as ReplyKeyboardMarkup
        else -> null
    }

    return SendMessage.builder()
        .chatId(chatId)
        .text(text)
        .parseMode(ParseMode.MARKDOWN)
        .disableWebPagePreview(true)
        .replyMarkup(replyKeyboard)
        .build()
}

fun Instant.kotlinInstantToDateString(): String {
    return (this.toLocalDateTime(TimeZone.currentSystemDefault())
        .toJavaLocalDateTime()
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
}