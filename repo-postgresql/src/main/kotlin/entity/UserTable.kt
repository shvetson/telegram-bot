package ru.shvets.subscriber.bot.repo.postgresql.entity

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.shvets.subscriber.bot.common.helper.NONE
import ru.shvets.subscriber.bot.common.model.ChatId
import ru.shvets.subscriber.bot.common.model.User
import java.time.LocalDateTime

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 10:26
 */

object UserTable : Table(name = "users_data") {
    val chatId: Column<String> = varchar("chat_id", 128).uniqueIndex()
    val firstName: Column<String> = varchar("first_name", 64)
    val lastName: Column<String> = varchar("last_name", 64)
    val userName: Column<String> = varchar("username", 64)
    val registeredAt: Column<LocalDateTime> = datetime("registered_at")

    override val primaryKey: PrimaryKey = PrimaryKey(chatId)

    fun toRow(it: UpdateBuilder<*>, user: User) {
        it[chatId] = user.chatId.takeIf { it != ChatId.NONE }?.asString() ?: ""
        it[firstName] = user.firstName.takeIf { it.isNotBlank() } ?: ""
        it[lastName] = user.lastName.takeIf { it.isNotBlank() } ?: ""
        it[userName] = user.userName.takeIf { it.isNotBlank() } ?: ""
        it[registeredAt] =
            (user.registeredAt.takeIf { it != Instant.NONE }
                ?: Clock.System.now()).toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    }

    fun fromRow(result: ResultRow): User =
        User(
            chatId = ChatId(result[chatId].toString()),
            firstName = result[firstName],
            lastName = result[lastName],
            userName = result[userName],
            registeredAt = result[registeredAt].toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()),
        )
}