package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message

object CmdHi : DuckyCommand() {
    init {
        name = "Say Hi"
        description = "Send a message that matches this syntax if you want me to reply \"hi\" to you"
        syntax = listOf("hello %bot%", "howdy %bot%", "hi there %bot%", "hi %bot%", "hey %bot%")
        minRank = Rank.Everyone
    }

    override fun execute(msg: Message, args: List<Any>) {
        msg.channel.sendWithTyping("${listOf("Hi there", "Howdy", "Hiya", "Hey", "Hi").random()} ${msg.author.asMention}!")
    }
}