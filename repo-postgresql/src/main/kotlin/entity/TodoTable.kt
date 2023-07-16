package ru.shvets.telegram.bot.repo.postgresql.entity

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.shvets.telegram.bot.common.helper.NONE
import ru.shvets.telegram.bot.common.model.TodoId
import ru.shvets.telegram.bot.common.model.Todo
import java.time.LocalDateTime

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 10:26
 */

object TodoTable : Table(name = "todos_data") {
    val id: Column<Long> = long("id")
    val title: Column<String> = varchar("title", 128)
    val content: Column<String> = varchar("content", 1024)
    val createdAt: Column<LocalDateTime> = datetime("created_at")
    val userId: Column<String> = varchar("user_id", 64)

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun toRow(it: UpdateBuilder<*>, todo: Todo) {
        it[id] = todo.id.takeIf { it != TodoId.NONE }?.asString()?.toLong() ?: 0L
        it[title] = todo.title.takeIf { it.isNotBlank() } ?: ""
        it[content] = todo.content.takeIf { it.isNotBlank() } ?: ""
        it[createdAt] =
            (todo.createdAt.takeIf { it != Instant.NONE }
                ?: Clock.System.now()).toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
        it[userId] = todo.userId.takeIf { it != 0L }.toString() ?: ""
    }

    fun fromRow(result: ResultRow): Todo =
        Todo(
            id = TodoId(result[id].toString()),
            title = result[title],
            content = result[content],
            createdAt = result[createdAt].toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()),
            userId = result[userId].toLong()
        )
}