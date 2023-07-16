package ru.shvets.telegram.bot.app.ktor.bot

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.shvets.telegram.bot.common.helper.SORRY_TEXT
import ru.shvets.telegram.bot.common.model.Todo
import ru.shvets.telegram.bot.common.repo.TodoRepository
import java.util.regex.Matcher
import java.util.regex.Pattern

fun getCallBackCommandResponse(data: String, message: Message, todoItemStep: MutableMap<Long, Todo>, todoService: TodoRepository): Any {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId

    val patternTodoCommand: Pattern = Pattern.compile("^/todos/")
    val matcherTodoCommand: Matcher = patternTodoCommand.matcher(data)
    val cmd  = if (matcherTodoCommand.find()) CallbackCommand.TODO.command else data

    return when (cmd) {
        CallbackCommand.MENU_TODO.command -> {
            handleMenuTodoCommand(chatId, messageId)
        }

        CallbackCommand.TODO.command -> {
            getTodoCommandResponse(data, message, todoItemStep, todoService)
        }

        else -> {
            handleNotFoundCommand(chatId)
        }
    }
}

private fun handleNotFoundCommand(chatId: String): SendMessage {
    return sendMessage(chatId = chatId, text = "*$SORRY_TEXT*")
}

private fun handleMenuTodoCommand(chatId: String, messageId: Int): EditMessageText {
    return editMessageText(chatId = chatId, text = "*Todo service*", messageId = messageId, keyboard = getInlineKeyboardTodoCommand())
}

private fun getInlineKeyboardTodoCommand(): InlineKeyboardMarkup {
    val todoListButton = getButtonWithEmoji("ToDo List", CommandTodo.LIST.command, ":ledger:")
    val todoCreateItemButton = getButtonWithEmoji("Create New", CommandTodo.CREATE.command, ":new:")

    val firstRow = getRow(todoListButton)
    val secondRow = getRow(todoCreateItemButton)

    val collection = getCollection(firstRow, secondRow)
    return getKeyboard(collection)
}