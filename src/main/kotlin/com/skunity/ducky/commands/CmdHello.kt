package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message

/**
 * @author Nicofisi
 */
object CmdHello : DuckyCommand() {
    init {
        name = "Say Hello"
        description = "Send a message that matches this syntax if you want me to reply with \"hi\""
        syntax = listOf("hello %bot%", "howdy %bot%", "hi there %bot%", "hi %bot%", "hey %bot%", "henlo %bot%")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping("${listOf("Hi there", "Howdy", "Hiya", "Hey", "Hi", "Henlo").random()} ${message.author.asMention}!")
    }
}