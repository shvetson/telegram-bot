package ru.shvets.telegram.bot.app.ktor.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shvets.telegram.bot.repo.postgresql.entity.TodoTable

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  08.05.2023 12:09
 */

object DatabaseFactory {
    fun init(appSettings: AppSettings) {

        val config = HikariConfig()
        config.apply {
            driverClassName = appSettings.db.driver
            jdbcUrl = appSettings.db.url
            username = appSettings.db.user
            password = appSettings.db.password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        Database.connect(HikariDataSource(config))

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(TodoTable)
//            SchemaUtils.drop(TodoTable)
            SchemaUtils.createMissingTablesAndColumns(TodoTable)
        }
    }
}