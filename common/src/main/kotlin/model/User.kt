package ru.shvets.telegram.bot.common.model

import kotlinx.datetime.Instant
import ru.shvets.telegram.bot.common.helper.NONE

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  23.06.2023 21:02
 */

data class User(
    val chatId: ChatId = ChatId.NONE,
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val registeredAt: Instant = Instant.NONE
)
