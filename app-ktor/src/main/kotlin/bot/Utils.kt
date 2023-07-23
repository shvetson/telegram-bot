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

fun getInlineKeyboardMenu(): InlineKeyboardMarkup {
    val menuButton = getButton(
        text = "Go to Todos",
        callbackData = ButtonCallback.MENU.button
    )
    val requireButton = getButton(
        text = "Go to require",
        callbackData = "require"
    )
    val rowButtons1 = getRow(menuButton, requireButton)

    val testButton = getButton(
        text = "Go to test",
        callbackData = "test"
    )
    val rowButtons2 = getRow(testButton)
    val collection = getCollection(rowButtons1, rowButtons2)
    return getKeyboard(collection)
}

// меню 2-го уровня - возврат на уровень выше, вывод всех записей, поиск записей
fun getInlineKeyboardMenuAndTodo(): InlineKeyboardMarkup {
    val menuButton = getButton(
        text = "Menu",
        callbackData = ButtonCallback.MENU.button
    )
    val todoListButton = getButtonWithEmoji(
        text = "List",
        callbackData = ButtonTodo.LIST.button,
        emoji = ":ledger:"
    )
    val searchButton = getButtonWithEmoji(
        text = "Search",
        callbackData = ButtonTodo.SEARCH.button,
        emoji = ":mag_right:"
    )

    val rowButtons = getRow(menuButton, todoListButton, searchButton)
    val collection = getCollection(rowButtons)
    return getKeyboard(collection)
}

fun getInlineKeyboardTodoButtonsListAndNew(): InlineKeyboardMarkup {
    val todoListButton = getButtonWithEmoji(
        text = "List",
        callbackData = ButtonTodo.LIST.button,
        emoji = ":ledger:"
    )
    val newButton = getButton(
        text = "New",
        callbackData = ButtonTodo.CREATE.button
    )

    val rowButtons = getRow(todoListButton, newButton)
    val collection = getCollection(rowButtons)
    return getKeyboard(collection)
}

// меню команд для обработки записи
fun getInlineKeyboardTodoButtonsUpdateDeleteCancel(): InlineKeyboardMarkup {
    val updateButton = getButton(
        text = "Update",
        callbackData = ButtonTodo.UPDATE.button
    )
    val deleteButton = getButton(
        text = "Delete",
        callbackData = ButtonTodo.DELETE.button
    )
    val cancelButton = getButtonWithEmoji(
        text = "Cancel",
        callbackData = ButtonCallback.CANCEL.button,
        emoji = ":x:"
    )

    val rowButtons = getRow(updateButton, deleteButton, cancelButton)
    val collection = getCollection(rowButtons)
    return getKeyboard(collection)
}

// меню команд для работы с записями после их вывода списком
fun getInlineKeyboardTodoButtonsMenuAndSearch(): InlineKeyboardMarkup {
    val menuButton = getButton(
        text = "Menu",
        callbackData = ButtonCallback.MENU.button
    )
    val searchButton = getButtonWithEmoji(
        text = "Search",
        callbackData = ButtonTodo.SEARCH.button,
        emoji = ":mag_right:"
    )

    val rowButtons = getRow(menuButton, searchButton)
    val collection = getCollection(rowButtons)
    return getKeyboard(collection)
}

fun getInlineKeyboardCancel(): InlineKeyboardMarkup {
    val cancelButton = getButton(
        text = "Cancel",
        callbackData = ButtonCallback.CANCEL.button
    )
    val rowButtons = getRow(cancelButton)
    val collection = getCollection(rowButtons)
    return getKeyboard(collection)
}