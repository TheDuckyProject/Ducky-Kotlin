package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.timer
import net.dv8tion.jda.core.entities.Message
import java.util.*

/**
 * @author Nicofisi
 */
object CmdShutdown : DuckyCommand() {
    init {
        name = "Shutdown"
        description = "TODO write me"
        syntax = listOf("die %bot%", "kys %bot%", "rip %bot%", "%bot% shutdown")
        minRank = Rank.BotAdmin
    }

    override fun execute(message: Message, arguments: List<Any>) {
        // TODO wait before all queued messages in sendWithTyping are sent and stop listening for commands
        // during the wait?
        message.channel.sendMessage("rip ducky 2016-${Calendar.getInstance().get(Calendar.YEAR)}").queue {
            message.jda.shutdown()
            timer.cancel()
        }
    }
}