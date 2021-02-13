package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.readUserData
import com.skunity.ducky.saveUserData
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import java.util.*

/**
 * @author Nicofisi
 */
object CmdPenisLength : DuckyCommand() {
    init {
        name = "Penis Length"
        description = "TODO write me"
        syntax = listOf("penis length of %user%", "what's the penis length of %user%",
                "dick length of %user%", "what's the dick length of %user%",
                "how long is the dick of %user%")
        minRank = Rank.Everyone
    }

    override fun execute(message: Message, arguments: List<Any>) {
        val argUser = arguments[0] as User

        val data = readUserData(argUser)
        if (data.penisLength == -1) {
            data.penisLength = Random().nextInt(15) + 1
            saveUserData(argUser, data)
        }

        // if (argUser == message.author) TODO send a ruler
        // "not sure what your penis length is? Here you go..."

        // if (data.female) TODO
        // send "<mention or better tag because of possible abuse> is a lady! And ladies don't have penises"
        // and an image of a lady

        val penis = "8${"=".repeat(data.penisLength)}D" // B===========D

        message.channel.sendWithTyping("Penis length of ${argUser.asTag} is $penis :open_mouth:")
    }
}
