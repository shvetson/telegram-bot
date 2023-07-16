package ru.shvets.telegram.bot.app.ktor.bot

import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.shvets.telegram.bot.common.helper.SORRY_TEXT
import java.util.regex.Matcher
import java.util.regex.Pattern

fun getCallBackCommandResponse(data: String, message: Message): Any {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId

    val patternTodoCommand: Pattern = Pattern.compile("^/todos/")
    val matcherTimeCommand: Matcher = patternTodoCommand.matcher(data)
    val cmd  = if (matcherTimeCommand.find()) CallbackCommand.TODO.command else data

    return when (cmd) {
        CallbackCommand.MENU.command -> {
            handleMenuCommand(chatId, messageId)
        }

        CallbackCommand.TODO.command -> {
            getTodoCommandResponse(data, message)
        }

        else -> {
            handleNotFoundCommand(chatId)
        }
    }
}

private fun handleNotFoundCommand(chatId: String): SendMessage {
    return sendMessage(chatId, "*$SORRY_TEXT*")
}

private fun handleMenuCommand(chatId: String, messageId: Int): EditMessageText {
    val editMessageText = EditMessageText()
    editMessageText.apply {
        text = "*Todo service*"
        this.chatId = chatId
        this.messageId = messageId
        parseMode = ParseMode.MARKDOWN
        replyMarkup = getInlineKeyboardMenu()
    }
    return editMessageText
}

private fun getInlineKeyboardMenu(): InlineKeyboardMarkup {
    val todoListButton = getButtonWithEmoji("ToDo List", CommandTodo.LIST.command, ":ledger:")
    val todoCreateItemButton = getButtonWithEmoji("Create New", CommandTodo.CREATE.command, ":new:")

    val firstRow = getRow(todoListButton)
    val secondRow = getRow(todoCreateItemButton)

    val collection = getCollection(firstRow, secondRow)
    return getKeyboard(collection)
}