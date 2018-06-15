package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message

/**
 * @author NanoDankster
 */
object CmdShrug : DuckyCommand() {
    init {
        name = "Shrug"
        description = "¯\\_(ツ)_/¯"
        syntax = listOf("shrug", "%bot% shrug")
        minRank = Rank.Everyone
    }

    // I guess we could make it *require* %bot% in the syntax,
    // but I like it so much without this right now lol
    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping("¯\\_(ツ)_/¯")
        // there is a `shrug %user%` syntax too in Ducky v4 but I have realized it enables people to mention-spam
        // anyone. we could re-add it but with `.tag` instead, I dunno if it has a point though
    }
}