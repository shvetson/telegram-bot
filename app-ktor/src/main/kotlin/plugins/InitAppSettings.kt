package ru.shvets.telegram.bot.app.ktor.plugins

import io.ktor.server.application.*
import ru.shvets.telegram.bot.app.ktor.config.AppSettings
import ru.shvets.telegram.bot.app.ktor.config.PostgresConfig
import ru.shvets.telegram.bot.app.ktor.config.TelegramConfig
import ru.shvets.telegram.bot.repo.postgresql.service.TodoService

fun Application.initAppSettings(): AppSettings {
    return AppSettings(
        db = initAppRepo(),
        bot = initAppTelegram(),
        timerMinutes = environment.config.property("scheduler.minutes").getString().toLong(),
        repo = listOf(TodoService())
    )
}

private fun Application.initAppRepo(): PostgresConfig = PostgresConfig(
    url = environment.config.property("psql.url").getString(),
    driver = environment.config.property("psql.driver").getString(),
    user = environment.config.property("psql.user").getString(),
    password = environment.config.property("psql.password").getString(),
)

private fun Application.initAppTelegram(): TelegramConfig = TelegramConfig(
    botName = environment.config.property("telegram.name").getString(),
    botToken = environment.config.property("telegram.token").getString(),
//    botOwner = environment.config.property("telegram.owner").getString().toLong(),
)