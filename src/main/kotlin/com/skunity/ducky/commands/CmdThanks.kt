package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.api.entities.Message

/**
 * @author NanoDankster
 */
object CmdThanks : DuckyCommand() {
    init {
        name = "Thanks"
        description = "All thanks to the duck"
        syntax = listOf("thanks %bot%", "thank you %bot%", "thanks very much %bot%", "%bot% thanks", "%bot% thank you")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        val author = message.author.asMention
        message.channel.sendWithTyping(listOf(
                "No problem, $author.",
                "Thanks to you $author.",
                "$author no worries!",
                "$author <3",
                "Lol np $author <3"
        ).random() + (if (message.contentDisplay.endsWith(":3")) " :3" else ""))
    }
}
