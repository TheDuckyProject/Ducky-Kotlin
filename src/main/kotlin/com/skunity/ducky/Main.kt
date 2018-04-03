package com.skunity.ducky

import com.beust.klaxon.Klaxon
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import java.io.File

fun main(args: Array<String>) {
    val config = Klaxon().parse<Config>(File("config.json"))
    Ducky.jda = JDABuilder(AccountType.valueOf(config!!.accountType))
            .setToken(config.token)
            .addEventListener(DuckyListener)
            .buildBlocking()
}

object Ducky {
    lateinit var jda: JDA
}

class Config(val token: String, val accountType: String)