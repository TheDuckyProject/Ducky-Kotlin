package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message

/**
 * The difference between this command and [CmdSay] is that [CmdSay] makes Ducky reply to *you*,
 * while [CmdSayHello] makes ducky say hello to a larger group of people
 *
 * @author Nicofisi
 */
object CmdSayHello : DuckyCommand() {
    init {
        name = "Say Hello"
        description = "Make the duck say hello"
        syntax = listOf(
                "say hello %bot%", "say hi %bot%", "say heya %bot%", "say hey %bot%", "say hiya %bot%", "say yo %bot%")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping(listOf(
                "Hello! :wave::skin-tone-3:",
                "Hi everybody <3",
                "Hello everyone! It's nice to see all of you again :)"
        ).random())
    }
}