package ru.shvets.subscriber.bot.app.ktor.bot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import ru.shvets.subscriber.bot.app.ktor.config.AppSettings
import ru.shvets.subscriber.bot.repo.postgresql.service.UserService
import ru.shvets.subscriber.bot.common.helper.*
import ru.shvets.subscriber.bot.common.repo.UserRepository
import ru.shvets.subscriber.bot.log.Logger

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 11:55
 */

class BotService(appSettings: AppSettings) : TelegramLongPollingBot(), Logger {
    private val telegramConfig = appSettings.bot
//    private val userService = appSettings.repo?.find { it is UserRepository } as UserService

    override fun getBotToken() = telegramConfig.botToken
    override fun getBotUsername() = telegramConfig.botName

    override fun onUpdateReceived(update: Update?) {
        val chatId = update?.message?.chat?.id.toString()

        if (update?.hasMessage() == true && update.message?.hasText() == true) {

            val cmd = update.message.text.lowercase()
            val firstName = update.message.chat.firstName

            val sendMessage = getCommandResponse(cmd, firstName, chatId)
            execute(sendMessage)

        } else if (update?.hasCallbackQuery() == true) {

            val callbackQuery = update.callbackQuery
            val cmd = callbackQuery.data
            val message = callbackQuery.message

            when (val msg = getCallBackCommandResponse(cmd, message)) {
                is SendMessage -> execute(msg as SendMessage)
                is EditMessageText -> execute(msg as EditMessageText)
            }
        } else if (update?.hasMessage() == true && update.message?.hasDocument() == true) {

            val file = update.message.document
            val name = file.fileName
            val size = file.fileSize

            execute(sendMessage(chatId, "File - $name\nSize - $size"))
        }
    }

    fun configureMenu() {
        val listOfCommands: MutableList<BotCommand> = mutableListOf()
        listOfCommands.add(BotCommand(Command.START.command, START_LABEL))
        listOfCommands.add(BotCommand(Command.HELP.command, HELP_LABEL))
        listOfCommands.add(BotCommand(Command.SET.command, SET_LABEL))
        execute(SetMyCommands(listOfCommands, BotCommandScopeDefault(), null))
    }
}