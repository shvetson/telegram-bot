package ru.shvets.telegram.bot.repo.postgresql.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shvets.telegram.bot.common.model.Todo
import ru.shvets.telegram.bot.common.model.TodoId
import ru.shvets.telegram.bot.common.repo.TodoRepository
import ru.shvets.telegram.bot.log.Logger
import ru.shvets.telegram.bot.repo.postgresql.entity.TodoTable
import ru.shvets.telegram.bot.repo.postgresql.entity.TodoTable.fromRow

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  23.06.2023 21:32
 */

class TodoService : TodoRepository, Logger {
    override suspend fun create(todo: Todo): Unit = dbQuery {
        log.info("Todo added")
        TodoTable.insert { toRow(it, todo) }
            .resultedValues?.singleOrNull()?.let(::fromRow)
    }

    override suspend fun read(todoId: TodoId): Todo? = dbQuery {
        log.info("Todo got")
        TodoTable.select { TodoTable.id eq todoId.asString().toLong() }
            .map(::fromRow)
            .singleOrNull()
    }

    override suspend fun update(todo: Todo): Unit = dbQuery {
        log.info("Todo updated")
        TodoTable.update({ TodoTable.id eq todo.id.asString().toLong() }) { toRow(it, todo) }
    }

    override suspend fun delete(todoId: TodoId): Boolean = dbQuery {
        log.info("Todo deleted")
        TodoTable.deleteWhere { TodoTable.id eq todoId.asString().toLong() } > 0
    }

    override suspend fun search(userId: Int?, search: String): List<Todo> = dbQuery {
        val result = TodoTable.selectAll()
        userId?.let { result.andWhere { TodoTable.userId eq it.toString() } }
        search.let { result.andWhere { TodoTable.title like "%${it}%" } }
        search.let { result.orWhere { TodoTable.content like "%${it}%" } }
        result.orderBy(TodoTable.id).map(::fromRow)
    }

    private suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction {
                block()
            }
        }
}