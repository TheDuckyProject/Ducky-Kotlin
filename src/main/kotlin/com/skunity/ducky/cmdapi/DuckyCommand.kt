package com.skunity.ducky.cmdapi

import net.dv8tion.jda.api.entities.Message

// TODO docs
abstract class DuckyCommand {

    lateinit var name: String

    lateinit var description: String

    /**
     * The pattern needs to be fully lowercase
     */
    lateinit var syntax: List<String>

    lateinit var minRank: Rank

    abstract fun execute(message: Message, arguments: List<Any>)
}
