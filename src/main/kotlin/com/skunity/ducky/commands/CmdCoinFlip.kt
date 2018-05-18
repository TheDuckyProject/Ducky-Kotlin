package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.random
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message

/**
 * @author NanoDankster
 */
object CmdCoinFlip : DuckyCommand() {
    init {
        name = "Flip a Coin"
        description = "Heads, you win"
        syntax = listOf("%bot% flip a coin", "flip a coin %bot%")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        val result = if (random.nextBoolean()) "HEADS" else "TAILS"
        val hasLongName = (message.member?.effectiveName ?: message.author.name).length >= 10

        val author = message.author.asMention

        val messages = listOf(
                "Ooo, $author got **$result**!",
                "**$result**, $author!",
                "$author of the ${if (hasLongName) "long" else "short"} nameds flipped a coin and got **$result**!",
                "Quicks, quacks; $author flipped and got **$result**!"
        )
        if (message.contentDisplay.contains("please")) {
            message.channel.sendWithTyping("Sure!")
        }
        message.channel.sendWithTyping(messages.random())
    }
}