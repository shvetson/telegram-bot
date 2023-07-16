package ru.shvets.telegram.bot.common.repo

import ru.shvets.telegram.bot.common.model.ChatId
import ru.shvets.telegram.bot.common.model.User

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  23.06.2023 20:58
 */

interface UserRepository {

    suspend fun create(user: User)
    suspend fun read(chatId: ChatId): User?
    suspend fun update(user: User)
    suspend fun delete(chatId: ChatId): Boolean
    suspend fun search(chatId: ChatId? = null): List<User>
}