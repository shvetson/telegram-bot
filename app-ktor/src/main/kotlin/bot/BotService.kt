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
import ru.shvets.telegram.bot.common.helper.HELP_LABEL
import ru.shvets.telegram.bot.common.helper.SET_LABEL
import ru.shvets.telegram.bot.common.helper.START_LABEL
import ru.shvets.telegram.bot.common.model.Commands
import ru.shvets.telegram.bot.common.model.Context
import ru.shvets.telegram.bot.common.model.TodoId
import ru.shvets.telegram.bot.common.model.TodoStatusType
import ru.shvets.telegram.bot.common.repo.TodoRepository
import ru.shvets.telegram.bot.log.Logger
import ru.shvets.telegram.bot.repo.postgresql.service.TodoService
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 11:55
 */

class BotService(appSettings: AppSettings) : TelegramLongPollingBot(), Logger {
    private val telegramConfig = appSettings.bot
    private val todoService = appSettings.repo?.find { it is TodoRepository } as TodoService

    override fun getBotToken() = telegramConfig.botToken
    override fun getBotUsername() = telegramConfig.botName

    override fun onUpdateReceived(update: Update?) {
        val chatId = update?.message?.chat?.id.toString()
        val patternTodoCommand: Pattern = Pattern.compile("^/todos")

        if (update?.hasMessage() == true && update.message?.hasText() == true) {
            val text = update.message.text.lowercase()
            val firstName = update.message.chat.firstName

            val matcherTodoCommand: Matcher = patternTodoCommand.matcher(text)
            if (matcherTodoCommand.find()) Context.command = Commands.READ

            // обработка выбранной команды из основного меню
            if (Context.command == Commands.NONE) {
                val sendMessage = getCommandResponse(
                    chatId = chatId,
                    text = text,
                    firstName = firstName
                )
                execute(sendMessage)
                return
            }

            // обработка создания нового элемента, см статус обработки поля - title или content - команда CREATE
            if (Context.command == Commands.CREATE) {
                if (Context.status == TodoStatusType.TITLE) {
                    Context.current?.title = text
                    Context.status = TodoStatusType.CONTENT
                    val sendMessage = sendMessage(
                        chatId = chatId,
                        text = "*Title*: $text\nSend *Content* ->"
                    )
                    execute(sendMessage)
                } else {
                    Context.current?.content = text
                    Context.current?.let { runBlocking { todoService.create(it) } }
                    val messageText =
                        Context.current?.let { "*Title*: ${it.title}\n*Content*: ${it.content}\n_Create Todo finished._" }
                            ?: "Nothing saved"

                    val sendMessage = sendMessage(
                        chatId = chatId,
                        text = messageText,
                        keyboard = getInlineKeyboardMenuAndTodo()
                    )
                    execute(sendMessage)

                    Context.status = TodoStatusType.FINISHED
                    Context.command = Commands.NONE
                }
                return
            }

            // обработка выбранного элемента из списка - команда READ
            if (Context.command == Commands.READ) {
                val id = text.substring(6)
                val todo = runBlocking { todoService.read(TodoId(id)) }
                Context.current = todo
                val messageText = StringBuilder().append("Title: ${todo?.title}").append("\n")
                    .append("Content: ${todo?.content}").toString()
                val sendMessage = sendMessage(
                    chatId = chatId,
                    text = messageText,
                    keyboard = getInlineKeyboardTodoButtonsUpdateDeleteCancel()
                )
                execute(sendMessage)
                Context.command = Commands.NONE
                return
            }

            // обработка обновления элемента, см статус обработки поля - title или content - команда UPDATE
            if (Context.command == Commands.UPDATE) {
                if (Context.status == TodoStatusType.UPDATE_TITLE) {
                    val todo = Context.current
                    Context.current?.title = text
                    Context.status = TodoStatusType.UPDATE_CONTENT
                    val sendMessage = sendMessage(
                        chatId = chatId,
                        text = "*New Title*: $text\nCurrent content: ${todo?.content}\nSend *New Content* ->"
                    )
                    execute(sendMessage)
                } else {
                    Context.current?.content = text
                    Context.current?.let { runBlocking { todoService.update(it) } }
                    val messageText =
                        Context.current?.let { "*Title*: ${it.title}\n*Content*: ${it.content}\n_Update Todo finished._" }
                            ?: "Nothing updated"
                    val sendMessage = sendMessage(
                        chatId = chatId,
                        text = messageText,
                        keyboard = getInlineKeyboardMenuAndTodo()
                    )
                    execute(sendMessage)

                    Context.status = TodoStatusType.FINISHED
                    Context.command = Commands.NONE
                }
                return
            }

            // обработка команды SEARCH
            if (Context.command == Commands.SEARCH) {
                val list = runBlocking { todoService.search(chatId.toInt(), text.lowercase()) }
                val messageText = printAllItems(list).takeIf { it.isNotEmpty() }
                    ?: "there is not anything to equal search text"
                val sendMessage = sendMessage(chatId, messageText, getInlineKeyboardMenuAndTodo())
                execute(sendMessage)
                Context.command = Commands.NONE
                return
            }
        } else if (update?.hasCallbackQuery() == true) {
            val callbackQuery = update.callbackQuery
            val data = callbackQuery.data
            val message = callbackQuery.message

            val msg = getCallBackButtonResponse(
                data = data,
                message = message,
                todoService = todoService
            )

            when (msg) {
                is SendMessage -> execute(msg as SendMessage)
                is EditMessageText -> execute(msg as EditMessageText)
            }
            // обработка полученного файла - документа
        } else if (update?.hasMessage() == true && update.message?.hasDocument() == true) {
            val file = update.message.document
            val name = file.fileName
            val size = file.fileSize

            execute(
                sendMessage(
                    chatId = chatId,
                    text = "File - $name\nSize - $size"
                )
            )
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