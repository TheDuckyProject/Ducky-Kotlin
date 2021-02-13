package com.skunity.ducky

import com.beust.klaxon.Klaxon
import com.skunity.ducky.commands.CmdDoMath
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.MessageChannel
import java.io.File
import java.time.LocalDateTime
import kotlin.concurrent.timerTask

object Ducky {
    val startTime: LocalDateTime = LocalDateTime.now()

    val config = Klaxon().parse<BotConfig>(File("config.json"))!!
    val jda = JDABuilder.createDefault(Ducky.config.token)
        .addEventListeners(DuckyListener)
        .build()
        .awaitReady()

    var typingChannelToEndTime = emptyMap<MessageChannel, Long>()

    init {
        val data = readBotData()
        data.startCount++
        saveBotData(data)

        // Here we schedule resending the "typing" messages to Discord,
        // when typing in some channel is not completed within 7 seconds
        timer.scheduleAtFixedRate(timerTask {
            val now = System.currentTimeMillis()
            typingChannelToEndTime.forEach { (channel, endTime) ->
                if (endTime < now) {
                    Ducky.typingChannelToEndTime -= channel
                } else {
                    channel.sendTyping().queue()
                }
            }
        }, 100, 7000)

        Thread {
            CmdDoMath // load the math library asynchronously
        }.start()

        // Every minute adds one minute to the total uptime of Ducky and saves it
        timer.scheduleAtFixedRate(timerTask {
            val duckyData = readBotData()
            duckyData.totalUptime += 60 * 1000
            saveBotData(duckyData)
        }, 60 * 1000, 60 * 1000)
    }
}

/**
 * The main function which does nothing on its own, just initializes [Ducky]
 */
fun main() {
    Ducky
}
