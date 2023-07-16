package ru.shvets.telegram.bot.app.ktor.bot

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import ru.shvets.telegram.bot.common.helper.SORRY_TEXT
import ru.shvets.telegram.bot.common.model.Context
import ru.shvets.telegram.bot.common.model.Todo
import ru.shvets.telegram.bot.common.model.TodoId
import ru.shvets.telegram.bot.common.model.TodoStatusType
import ru.shvets.telegram.bot.common.repo.TodoRepository

fun getTodoCommandResponse(
    callbackCommand: String,
    message: Message,
    todoService: TodoRepository,
    todoItemStep: MutableMap<Long, Todo>,
): Any {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId
    val log: Logger = LoggerFactory.getLogger("TODO")

    return when (callbackCommand) {
        CommandTodo.LIST.command -> {
            log.info("Request to get all items")
//            sendMessage(chatId, "Command get all list")
        }

        CommandTodo.CREATE.command -> {
            log.info("Request to create a new item")
            handleCreateCommand(chatId, messageId, todoService, todoItemStep)
        }

//        CommandTodo.READ.command -> {
//
//        }
//
//        CommandTodo.UPDATE.command -> {
//
//        }
//
//        CommandTodo.DELETE.command -> {
//
//        }

        else -> {
            handleNotFoundCommand(chatId)
        }
    }
}

//private fun handleStartCommand(chatId: String, firstName: String): SendMessage {
//    val text = EmojiParser
//        .parseToUnicode("Привет, _${firstName}_! :wave: Я - Telegram bot :blush: \n*Доступные команды:*")
//    return sendMessage(chatId, text, getInlineKeyboard())
//}

private fun handleNotFoundCommand(chatId: String): SendMessage {
    return sendMessage(chatId, "*$SORRY_TEXT*")
}

private fun handleCreateCommand(
    chatId: String,
    messageId: Int,
    todoService: TodoRepository,
    todoItemStep: MutableMap<Long, Todo>,
): EditMessageText {
    val editMessageText = EditMessageText()
    editMessageText.apply {
        text = "Send *Title*"
        this.chatId = chatId
        this.messageId = messageId
        parseMode = ParseMode.MARKDOWN
    }

    Context.status = TodoStatusType.TITLE

    val todo = Todo(
        id = TodoId(messageId.toString()),
        userId = chatId.toLong(),
    )

    todoItemStep[chatId.toLong()] = todo
    return editMessageText
}