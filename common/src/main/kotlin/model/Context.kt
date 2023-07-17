package ru.shvets.telegram.bot.common.model

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  16.07.2023 16:44
 */

object Context {
    var status: TodoStatusType = TodoStatusType.NONE
    var current: Todo? = null
}