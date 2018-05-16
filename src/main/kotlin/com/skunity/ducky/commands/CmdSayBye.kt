package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message

/**
 * @author NanoDankster
 */
object CmdSayBye : DuckyCommand() {
    init {
        name = "Say Goodbye"
        description = "Make the duck say goodbye"
        syntax = listOf("say goodbye", "say bye", "say bai")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping(arrayOf(
                "Goodbye! :wave:",
                "Bye everybody \\<3",
                "Goodbye everyone! See you all later \\:)"
        ).random())
    }
}