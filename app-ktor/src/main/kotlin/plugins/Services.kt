package ru.shvets.subscriber.bot.app.ktor.plugins

import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.shvets.subscriber.bot.app.ktor.bot.BotService
import ru.shvets.subscriber.bot.app.ktor.config.AppSettings
import java.util.*

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.06.2023 20:48
 */

fun Application.initServices(appSettings: AppSettings) {
    val bot = TelegramBotsApi(DefaultBotSession::class.java)
    val botService = BotService(appSettings)

    bot.registerBot(botService)
    log.info("Bot started")

    botService.configureMenu()
    log.info("Menu configured")

//    val timer: Timer = Timer()
//    timer.schedule(object : TimerTask() {
//        override fun run() {
//            Runnable { runBlocking { botService.sendAds() } }.run()
//        }
//    }, 60 * 1000, appSettings.timerMinutes * 60 * 1000)
}