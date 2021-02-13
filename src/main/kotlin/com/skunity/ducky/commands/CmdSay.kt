package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.noEveryoneHere
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.api.entities.Message

/**
 * @author Nicofisi
 */
object CmdSay : DuckyCommand() {
    init {
        name = "Say"
        description = "Make the duck say something"
        syntax = listOf("%bot% say %string%", "%bot% reply with %string%", "%bot% repeat after me %string%")
        minRank = Rank.BotMod
    }

    override fun execute(message: Message, arguments: List<Any>) {
        message.channel.sendWithTyping((arguments.first() as String).noEveryoneHere)
        message.delete().queue()
    }
}
