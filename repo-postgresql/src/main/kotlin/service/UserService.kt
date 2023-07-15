package ru.shvets.subscriber.bot.repo.postgresql.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shvets.subscriber.bot.common.model.ChatId
import ru.shvets.subscriber.bot.common.model.User
import ru.shvets.subscriber.bot.common.repo.UserRepository
import ru.shvets.subscriber.bot.log.Logger
import ru.shvets.subscriber.bot.repo.postgresql.SqlProperties
import ru.shvets.subscriber.bot.repo.postgresql.entity.UserTable
import ru.shvets.subscriber.bot.repo.postgresql.entity.UserTable.fromRow

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  23.06.2023 21:32
 */

class UserService(
//    properties: SqlProperties,
) : UserRepository, Logger {
    override suspend fun create(user: User): Unit = dbQuery {
        UserTable.insert { toRow(it, user) }
            .resultedValues?.singleOrNull()?.let(::fromRow)
        log.info("User saved")
    }

    override suspend fun read(chatId: ChatId): User? = dbQuery {
        UserTable.select { UserTable.chatId eq chatId.asString() }
            .map(::fromRow)
            .singleOrNull()
    }

    override suspend fun update(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(chatId: ChatId): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun search(chatId: ChatId?): List<User> = dbQuery {
        val result = UserTable.selectAll()
        chatId?.let { result.andWhere { UserTable.chatId eq chatId.asString() } }
        result.orderBy(UserTable.chatId).map(::fromRow)
    }

    private suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction {
//                addLogger(StdOutSqlLogger)
                block()
            }
        }
}