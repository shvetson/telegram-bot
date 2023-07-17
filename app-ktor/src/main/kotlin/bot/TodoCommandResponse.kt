package ru.shvets.telegram.bot.app.ktor.bot

import io.kotest.common.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import ru.shvets.telegram.bot.common.helper.SORRY_TEXT
import ru.shvets.telegram.bot.common.model.Context
import ru.shvets.telegram.bot.common.model.Todo
import ru.shvets.telegram.bot.common.model.TodoId
import ru.shvets.telegram.bot.common.model.TodoStatusType
import ru.shvets.telegram.bot.common.repo.TodoRepository
import java.lang.StringBuilder
import java.time.format.DateTimeFormatter

fun getTodoCommandResponse(
    callbackCommand: String,
    message: Message,
    todoItemStep: MutableMap<Long, Todo>,
    todoService: TodoRepository,
): Any {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId
    val log: Logger = LoggerFactory.getLogger("TODO")

    return when (callbackCommand) {
        CommandTodo.LIST.command -> {
            log.info("Request to get all items")
            handleListCommand(chatId, messageId, todoService)
        }

        CommandTodo.CREATE.command -> {
            log.info("Request to create a new item")
            handleCreateCommand(chatId, messageId, todoItemStep)
        }

//        CommandTodo.READ.command -> {
//
//        }
//
//        CommandTodo.UPDATE.command -> {
//
//        }

        CommandTodo.DELETE.command -> {
            log.info("Request to delete the item")
            handleDeleteCommand(chatId, messageId, todoService)
        }

        else -> {
            handleNotFoundCommand(chatId)
        }
    }
}

private fun handleNotFoundCommand(chatId: String): SendMessage {
    return sendMessage(chatId = chatId, text = "*$SORRY_TEXT*")
}

private fun handleListCommand(chatId: String, messageId: Int, todoService: TodoRepository): EditMessageText {
    val sb = StringBuilder()
    var counter = 0

    val list = runBlocking { todoService.search(chatId.toInt()) }
    for (item in list) {
        counter += 1
        sb.append("$counter \n")
            .append("${item.title}\n")
            .append("${item.content}\n")
            .append(
                item.createdAt
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            )
            .append(" /todos${item.id.asString()}\n")
            .append("\n")
    }
    return editMessage(chatId = chatId, text = sb.toString(), messageId = messageId)
}

private fun handleCreateCommand(chatId: String, messageId: Int, todoItemStep: MutableMap<Long, Todo>): EditMessageText {
    val editMessageText = editMessage(chatId = chatId, text = "Send *Title* ->", messageId = messageId)
    Context.status = TodoStatusType.TITLE

    val todo = Todo(
        id = TodoId(messageId.toString()),
        userId = chatId.toLong(),
    )

    todoItemStep[chatId.toLong()] = todo
    return editMessageText
}

private fun handleDeleteCommand(chatId: String, messageId: Int, todoService: TodoRepository): EditMessageText {
    val todo = Context.current
    runBlocking { todo?.let { todoService.delete(it.id) } }
    return editMessage(chatId = chatId, text = "Todo deleted", messageId = messageId)
}