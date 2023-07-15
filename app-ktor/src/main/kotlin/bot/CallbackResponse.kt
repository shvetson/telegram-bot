package ru.shvets.subscriber.bot.app.ktor.bot

import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.shvets.subscriber.bot.common.helper.SORRY_TEXT

fun getCallBackCommandResponse(cmd: String, message: Message): Any {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId

    return when (cmd) {
        CallbackCommand.MENU.command -> {
            handleMenuCommand(chatId, messageId)
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
    val todoListButton = getButton("ToDo List", "/todo/list")
    val todoCreateItemButton = getButton("Create New", "/todo/create")

    val firstRow = getRow(todoListButton)
    val secondRow = getRow(todoCreateItemButton)

    val collection = getCollection(firstRow, secondRow)
    return getKeyboard(collection)
}