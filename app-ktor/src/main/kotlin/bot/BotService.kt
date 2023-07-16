package ru.shvets.telegram.bot.app.ktor.bot

import kotlinx.coroutines.runBlocking
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import ru.shvets.telegram.bot.app.ktor.config.AppSettings
import ru.shvets.telegram.bot.common.helper.*
import ru.shvets.telegram.bot.common.model.Context
import ru.shvets.telegram.bot.common.model.Todo
import ru.shvets.telegram.bot.common.model.TodoStatusType
import ru.shvets.telegram.bot.common.repo.TodoRepository
import ru.shvets.telegram.bot.log.Logger
import ru.shvets.telegram.bot.repo.postgresql.service.TodoService

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 11:55
 */

class BotService(appSettings: AppSettings) : TelegramLongPollingBot(), Logger {
    private val telegramConfig = appSettings.bot
    private val todoService = appSettings.repo?.find { it is TodoRepository } as TodoService
    private val todoItemStep: MutableMap<Long, Todo> = mutableMapOf()

    override fun getBotToken() = telegramConfig.botToken
    override fun getBotUsername() = telegramConfig.botName

    override fun onUpdateReceived(update: Update?) {
        val chatId = update?.message?.chat?.id.toString()

        if (update?.hasMessage() == true && update.message?.hasText() == true) {

            val text = update.message.text.lowercase()
            val firstName = update.message.chat.firstName

            if (todoItemStep.containsKey(chatId.toLong())) {
                if (Context.status == TodoStatusType.TITLE) {
                    val todoItem = todoItemStep[chatId.toLong()]
                    todoItem?.title = text

                    Context.status = TodoStatusType.CONTENT
                    execute(sendMessage(chatId, "Send *Content*"))
                } else if (Context.status == TodoStatusType.CONTENT) {
                    val todoItem = todoItemStep[chatId.toLong()]
                    todoItem?.content = text

                    Context.status = TodoStatusType.FINISHED
                    todoItemStep.clear()

                    runBlocking { todoItem?.let { todoService.create(it) } }
                    execute(sendMessage(chatId, "Item added"))
                }
            } else {
                val sendMessage = getCommandResponse(text, firstName, chatId)
                execute(sendMessage)
            }

        } else if (update?.hasCallbackQuery() == true) {

            val callbackQuery = update.callbackQuery
            val data = callbackQuery.data
            val message = callbackQuery.message

            when (val msg = getCallBackCommandResponse(data, message, todoService, todoItemStep)) {
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