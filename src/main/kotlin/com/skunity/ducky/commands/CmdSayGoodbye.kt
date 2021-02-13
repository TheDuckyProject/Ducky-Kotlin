package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.api.entities.Message

/**
 * The difference between this command and [CmdGoodbye] is that [CmdSay] makes Ducky reply to *you*,
 * while [CmdSayHello] makes ducky say goodbye to a larger group of people
 *
 * @author NanoDankster
 */
object CmdSayGoodbye : DuckyCommand() {
    init {
        name = "Say Goodbye"
        description = "Make the duck say goodbye"
        syntax = listOf("say goodbye %bot%", "say bye %bot%", "say bai %bot%")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping(listOf(
                "Goodbye! :wave::skin-tone-3:",
                "Bye everybody <3",
                "Goodbye everyone! See you all later :)"
        ).random())
    }
}
