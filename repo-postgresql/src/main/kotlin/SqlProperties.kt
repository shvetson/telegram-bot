package ru.shvets.subscriber.bot.repo.postgresql

open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/tgbots",
    val user: String = "postgres",
    val password: String = "postgres",
    val schema: String = "tgbots",
    // Удалять таблицы при старте - нужно для тестирования
    val dropDatabase: Boolean = false,
)