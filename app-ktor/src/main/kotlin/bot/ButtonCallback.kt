package ru.shvets.telegram.bot.app.ktor.bot

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  15.07.2023 16:28
 */

enum class ButtonCallback(val button: String) {
    MENU("menu"),
    TODO("/todo"),
    CANCEL("cancel")
}