package ru.shvets.telegram.bot.common.repo

import ru.shvets.telegram.bot.common.model.Todo
import ru.shvets.telegram.bot.common.model.TodoId

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  23.06.2023 20:58
 */

interface TodoRepository {

    suspend fun create(todo: Todo)
    suspend fun read(todoId: TodoId): Todo?
    suspend fun update(todo: Todo)
    suspend fun delete(todoId: TodoId): Boolean
    suspend fun search(userId: Int? = null): List<Todo>
}