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

fun getCallBackButtonResponse(
    data: String,
    message: Message,
    todoService: TodoRepository,
): Any {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId

    val patternTodoCommand: Pattern = Pattern.compile("^/todos/")
    val matcherTodoCommand: Matcher = patternTodoCommand.matcher(data)
    val cmd = if (matcherTodoCommand.find()) ButtonCallback.TODO.button else data

    return when (cmd) {
        ButtonCallback.MENU.button -> {
            handleMenuTodoButton(chatId, messageId)
        }

        ButtonCallback.TODO.button -> {
            getTodoButtonResponse(data, message, todoService)
        }

        ButtonCallback.CANCEL.button -> {
            handleMenuTodoButton(chatId, messageId)
        }

        else -> {
            handleNotFoundButton(chatId)
        }
    }
}

private fun handleNotFoundButton(chatId: String): SendMessage {
    return sendMessage(
        chatId = chatId,
        text = "*$SORRY_TEXT*"
    )
}

private fun handleMenuTodoButton(chatId: String, messageId: Int): EditMessageText {
    return editMessage(
        chatId = chatId,
        text = "*Menu*",
        messageId = messageId,
        keyboard = getInlineKeyboardTodoButtonsListAndNew()
    )
}