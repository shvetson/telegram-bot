package ru.shvets.telegram.bot.app.ktor.bot

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  15.07.2023 10:17
 */

enum class Command(val command: String) {
    START("/start"),
    HELP("/help"),
    SET("/settings"),
}