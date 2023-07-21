package ru.shvets.telegram.bot.app.ktor.bot

import kotlinx.coroutines.runBlocking
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.shvets.telegram.bot.app.ktor.config.AppSettings
import ru.shvets.telegram.bot.common.helper.HELP_LABEL
import ru.shvets.telegram.bot.common.helper.SET_LABEL
import ru.shvets.telegram.bot.common.helper.START_LABEL
import ru.shvets.telegram.bot.common.model.Context
import ru.shvets.telegram.bot.common.model.Todo
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
    private val todoItemStep: MutableMap<Long, Todo> = mutableMapOf()

    override fun getBotToken() = telegramConfig.botToken
    override fun getBotUsername() = telegramConfig.botName

    override fun onUpdateReceived(update: Update?) {
        val chatId = update?.message?.chat?.id.toString()
        val patternTodoCommand: Pattern = Pattern.compile("^/todos")

        if (update?.hasMessage() == true && update.message?.hasText() == true) {
            val text = update.message.text.lowercase()
            val firstName = update.message.chat.firstName
            val matcherTodoCommand: Matcher = patternTodoCommand.matcher(text)

            if (todoItemStep.containsKey(chatId.toLong())) {
                if (Context.status == TodoStatusType.TITLE) {
                    val todoItem = todoItemStep[chatId.toLong()]
                    todoItem?.title = text
                    Context.status = TodoStatusType.CONTENT
                    execute(sendMessage(chatId, "*Title*: $text\nSend *Content* ->"))
                } else if (Context.status == TodoStatusType.CONTENT) {
                    val todoItem = todoItemStep[chatId.toLong()]
                    todoItem?.content = text
                    Context.status = TodoStatusType.FINISHED
                    todoItemStep.clear()
                    runBlocking { todoItem?.let { todoService.create(it) } }
                    val msg = "*Title*: ${todoItem?.title}\n*Content*: $text\n_Create Todo finished._"
                    val sendMessage = sendMessage(
                        chatId = chatId,
                        text = msg,
                        keyboard = getInlineKeyboardMenuAndTodo()
                    )
                    execute(sendMessage)
                }
                // обработка выбранного todo из списка
            } else if (matcherTodoCommand.find()) {
                val id = text.substring(6)
                val todo = runBlocking { todoService.read(TodoId(id)) }
                Context.current = todo
                val sb = StringBuilder()
                sb.append(todo?.title).append("\n").append(todo?.content)
                val sendMessage = sendMessage(
                    chatId = chatId,
                    text = sb.toString(),
                    keyboard = getInlineKeyboardTodoCommand()
                )
                execute(sendMessage)
            } else {
                // обработка выбранной команды из основного меню
                val sendMessage = getCommandResponse(
                    chatId = chatId,
                    text = text,
                    firstName = firstName
                )
                execute(sendMessage)
            }

        } else if (update?.hasCallbackQuery() == true) {
            val callbackQuery = update.callbackQuery
            val data = callbackQuery.data
            val message = callbackQuery.message
            val msg = getCallBackCommandResponse(
                data = data,
                message = message,
                todoItemStep = todoItemStep,
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

private fun getInlineKeyboardMenuAndTodo(): InlineKeyboardMarkup {
    val menuButton = getButton(text = "Go to Todos", callbackData = CommandCallback.MENU.command)
    val todoListButton = getButtonWithEmoji("ToDo List", CommandTodo.LIST.command, ":ledger:")


    val rowButtons = getRow(menuButton, todoListButton)
    val collection = getCollection(rowButtons)
    return getKeyboard(collection)
}

private fun getInlineKeyboardTodoCommand(): InlineKeyboardMarkup {
    val updateButton = getButton(text = "Update", callbackData = CommandTodo.UPDATE.command)
    val deleteButton = getButton(text = "Delete", callbackData = CommandTodo.DELETE.command)
    val cancelButton = getButtonWithEmoji(text = "Cancel", callbackData = CommandCallback.CANCEL.command,":x:")

    val rowButtons = getRow(updateButton, deleteButton, cancelButton)
    val collection = getCollection(rowButtons)
    return getKeyboard(collection)
}