package ru.shvets.telegram.bot.app.ktor.bot

import io.kotest.common.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import ru.shvets.telegram.bot.common.helper.SORRY_TEXT
import ru.shvets.telegram.bot.common.model.*
import ru.shvets.telegram.bot.common.repo.TodoRepository

fun getTodoButtonResponse(callbackButton: String, message: Message, todoService: TodoRepository): Any {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId
    val log: Logger = LoggerFactory.getLogger("TODO")

    return when (callbackButton) {
        ButtonTodo.LIST.button -> {
            log.info("Request to get all items")
            handleListButton(chatId, messageId, todoService)
        }

        ButtonTodo.CREATE.button -> {
            log.info("Request to create a new item")
            Context.command = Commands.CREATE
            handleCreateButton(chatId, messageId)
        }

        ButtonTodo.UPDATE.button -> {
            log.info("Request to update the item")
            Context.command = Commands.UPDATE
            handleUpdateButton(chatId, messageId)
        }

        ButtonTodo.DELETE.button -> {
            log.info("Request to delete the item")
            Context.command = Commands.DELETE
            handleDeleteButton(chatId, messageId, todoService)
        }

        ButtonTodo.SEARCH.button -> {
            log.info("Request to search")
            Context.command = Commands.SEARCH
            handleSearchButton(chatId, messageId)
        }

        else -> {
            Context.command = Commands.NONE
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

private fun handleListButton(chatId: String, messageId: Int, todoService: TodoRepository): EditMessageText {
    val list = runBlocking { todoService.search(chatId.toInt()) }

    return editMessage(
        chatId = chatId,
        text = printAllItems(list),
        messageId = messageId,
        keyboard = getInlineKeyboardTodoButtonsMenuAndSearch()
    )
}

private fun handleCreateButton(chatId: String, messageId: Int): EditMessageText {
    val todo = Todo(
        id = TodoId(messageId.toString()),
        userId = chatId.toLong(),
    )

    Context.status = TodoStatusType.TITLE
    Context.current = todo

    return editMessage(
        chatId = chatId,
        text = "Send *Title* ->",
        messageId = messageId
    )
}

private fun handleDeleteButton(chatId: String, messageId: Int, todoService: TodoRepository): EditMessageText {
    val todo = Context.current
    runBlocking { todo?.let { todoService.delete(it.id) } }
    Context.command = Commands.NONE
    return editMessage(
        chatId = chatId,
        text = "Todo deleted",
        messageId = messageId,
        keyboard = getInlineKeyboardTodoButtonsListAndNew()
    )
}

private fun handleUpdateButton(chatId: String, messageId: Int): EditMessageText {
    Context.status = TodoStatusType.UPDATE_TITLE
    val todo = Context.current

    return editMessage(
        chatId = chatId,
        text = "Current *Title*: ${todo?.title}\n Send new *Title* ->",
        messageId = messageId,
        keyboard = getInlineKeyboardCancel()
    )
}

private fun handleSearchButton(chatId: String, messageId: Int): EditMessageText {
    return editMessage(
        chatId = chatId,
        text = "Send *search* experience ->",
        messageId = messageId
    )
}

fun printAllItems(list: List<Todo>): String {
    val sb = StringBuilder()
    var counter = 0

    for (item in list) {
        counter += 1
        sb.append("$counter \n")
            .append("${item.title}\n")
            .append("${item.content}\n")
            .append(item.createdAt.kotlinInstantToDateString())
            .append(" /todos${item.id.asString()}\n")
            .append("\n")
    }
    return sb.toString()
}