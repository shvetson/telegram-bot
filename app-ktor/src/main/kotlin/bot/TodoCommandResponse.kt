package ru.shvets.telegram.bot.app.ktor.bot

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import ru.shvets.telegram.bot.common.helper.SORRY_TEXT


fun getTodoCommandResponse(callbackCommand: String, message: Message): SendMessage {
    val chatId = message.chat.id.toString()
    val messageId = message.messageId
    val log: Logger = LoggerFactory.getLogger("ToDo")

    return when (callbackCommand) {
        CommandTodo.LIST.command -> {
            log.info("Todo all list")
            sendMessage(chatId, "Command get all list")
        }

        CommandTodo.CREATE.command -> {
            sendMessage(chatId, "Command create a new item")
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