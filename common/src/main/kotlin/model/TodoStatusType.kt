package ru.shvets.telegram.bot.common.model

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  16.07.2023 16:46
 */

enum class TodoStatusType {
    NONE,
    TITLE,
    CONTENT,
    FINISHED,
    UPDATE_TITLE,
    UPDATE_CONTENT,
}