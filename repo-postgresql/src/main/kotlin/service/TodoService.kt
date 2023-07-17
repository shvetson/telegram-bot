package ru.shvets.telegram.bot.repo.postgresql.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shvets.telegram.bot.common.model.TodoId
import ru.shvets.telegram.bot.common.model.Todo
import ru.shvets.telegram.bot.common.repo.TodoRepository
import ru.shvets.telegram.bot.log.Logger
import ru.shvets.telegram.bot.repo.postgresql.entity.TodoTable
import ru.shvets.telegram.bot.repo.postgresql.entity.TodoTable.fromRow

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  23.06.2023 21:32
 */

class TodoService(
//    properties: SqlProperties,
) : TodoRepository, Logger {
    override suspend fun create(todo: Todo): Unit = dbQuery {
        TodoTable.insert { toRow(it, todo) }
            .resultedValues?.singleOrNull()?.let(::fromRow)
        log.info("Todo saved")
    }

    override suspend fun read(todoId: TodoId): Todo? = dbQuery {
        TodoTable.select { TodoTable.id eq todoId.asString().toLong() }
            .map(::fromRow)
            .singleOrNull()
    }

    override suspend fun update(todo: Todo) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(todoId: TodoId): Boolean = dbQuery{
        TodoTable.deleteWhere { TodoTable.id eq todoId.asString().toLong() } > 0
    }

    override suspend fun search(userId: Int?): List<Todo> = dbQuery {
        val result = TodoTable.selectAll()
        userId?.let { result.andWhere { TodoTable.userId eq userId.toString() } }
        result.orderBy(TodoTable.id).map(::fromRow)
    }

    private suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction {
//                addLogger(StdOutSqlLogger)
                block()
            }
        }
}