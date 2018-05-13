package com.skunity.ducky

import com.skunity.ducky.commands.*

// TODO adding commands from the `commands` package to this list with reflection perhaps?
val allCommands = listOf(
        // written in Kotlin
        CmdHi,
        CmdSay,
        CmdCoinFlip,
        CmdThanks,

        // written in Java
        CmdJavaGuys()
)