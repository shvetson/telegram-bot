package ru.shvets.telegram.bot.common.model

import kotlinx.datetime.Instant
import ru.shvets.telegram.bot.common.helper.NONE

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  23.06.2023 21:02
 */

data class Todo(
    val id: TodoId = TodoId.NONE,
    var title: String = "",
    var content: String = "",
    val createdAt: Instant = Instant.NONE,
    val userId: Long = 0L
)