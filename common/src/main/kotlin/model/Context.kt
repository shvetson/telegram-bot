package ru.shvets.telegram.bot.common.model

import kotlinx.datetime.Instant
import ru.shvets.telegram.bot.common.helper.NONE

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  16.07.2023 16:44
 */

object Context {
    var command: Commands = Commands.NONE
    var timeStart: Instant = Instant.NONE
    var status: TodoStatusType = TodoStatusType.NONE
    var current: Todo? = null
}