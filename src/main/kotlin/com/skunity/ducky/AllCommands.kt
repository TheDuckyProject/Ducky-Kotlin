package com.skunity.ducky

import com.skunity.ducky.commands.CmdHi
import com.skunity.ducky.commands.CmdJavaGuys
import com.skunity.ducky.commands.CmdSay

// TODO adding commands from the `commands` package to this list with reflection perhaps?
val allCommands = listOf(
        // written in Kotlin
        CmdHi,
        CmdSay,

        // written in Java
        CmdJavaGuys()
)