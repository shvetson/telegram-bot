package ru.shvets.telegram.bot.app.ktor.bot

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  15.07.2023 16:28
 */

enum class CommandCallback(val command: String) {
    MENU("menu"),
    TODO("/todo"),
    CANCEL("cancel")
}