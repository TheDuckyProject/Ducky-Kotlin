package com.skunity.ducky

import com.beust.klaxon.Klaxon
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.*
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.timerTask

object Ducky {
    val startTime = LocalDateTime.now()

    val config = Klaxon().parse<Config>(File("config.json"))!!
    val jda = JDABuilder(AccountType.valueOf(Ducky.config.accountType))
            .setToken(Ducky.config.token)
            .addEventListener(DuckyListener)
            .buildBlocking()
    var typingChannelToEndTime = emptyMap<MessageChannel, Long>()

    init {
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
    }
}

/**
 * The main function which does nothing on it's own, just initializes [Ducky]
 */
fun main(args: Array<String>) {
    Ducky
}
