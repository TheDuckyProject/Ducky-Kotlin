package com.skunity.ducky

import com.skunity.ducky.commands.*

// TODO adding commands from the `commands` package to this list with reflection perhaps?
val allCommands = listOf(
        // written in Kotlin
        CmdCoinFlip,
        CmdDoMath,
        CmdGoodbye,
        CmdHello,
        CmdOmgHi,
        CmdPenisLength,
        CmdSay,
        CmdSayGoodbye,
        CmdSayHello,
        CmdShrug,
        CmdShutdown,
        CmdThanks,

        // written in Java
        CmdJavaGuys()
)