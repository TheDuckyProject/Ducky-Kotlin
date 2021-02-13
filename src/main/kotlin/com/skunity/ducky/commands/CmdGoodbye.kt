package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.api.entities.Message

/**
 * @author NanoDankster
 */
object CmdGoodbye : DuckyCommand() {
    init {
        name = "Say Bye"
        description = "Don't go! :("
        syntax = listOf("bye %bot%", "%bot% bye", "%bot% see you")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping(listOf(
                ":wave:",
                "Goodbye!",
                "Baiii \\<3",
                "See ya!",
                "You're going?! Cya \\:(",
                "Bye :wave:"
        ).random())
    }
}
