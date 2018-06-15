package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message

/**
 * @author Nicofisi
 */
object CmdOmgHi : DuckyCommand() {
    init {
        name = "Omg Hi"
        description = "Ducky is surprised that you're still alive, and decides to say hi"
        syntax = listOf("omg hi %bot%")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping(listOf(
                "omgggg! Hiya ${message.author.asMention}" // TODO more responses maybe?
        ).random())
    }
}