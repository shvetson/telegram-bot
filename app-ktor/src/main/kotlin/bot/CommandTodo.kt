package ru.shvets.telegram.bot.app.ktor.bot

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  16.07.2023 10:38
 */

enum class CommandTodo(val command: String) {
    LIST("/todos/list"),
    CREATE("/todos/create"),
    READ("/todos/read"),
    UPDATE("/todos/update"),
    DELETE("/todos/delete"),
}