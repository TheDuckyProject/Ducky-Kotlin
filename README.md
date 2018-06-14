[![Build Status](https://travis-ci.com/TheDuckyProject/Ducky-Kotlin.svg?branch=master)](https://travis-ci.com/TheDuckyProject/Ducky-Kotlin)

Ducky v5, written in Kotlin

Licensed under [The GNU General Public License v3.0](LICENSE.md)

## The history of Ducks
First of all, remember, Ducky is not a bot. Don't let the `BOT` tag mistake you.

Anyways, here are all the official ducks that ever existed:

| Name | Language | Authors | Source code | Status | Official invite | 
| --- | --- | --- | --- | --- | --- |
| Ducky v1 | JavaScript | [@nfell2009](https://github.com/nfell2009) | closed | abandoned | - |
| Ducky v2 | JavaScript | [@nfell2009](https://github.com/nfell2009) and [@TheLimeGlass](https://github.com/TheLimeGlass) | [open](https://github.com/TheDuckyProject/DuckyJS) | abandoned | - |
| Ducky v3 | JavaScript | [@nfell2009](https://github.com/nfell2009) | closed | never finished, abandoned | - |
| Ducky v4 | JavaScript | [@Nicofisi](https://github.com/Nicofisi) | closed | **currently running as official** | - |
| **Ducky v5 (this version)** | Kotlin | [click to check](https://github.com/TheDuckyProject/Ducky-Kotlin/graphs/contributors) | [open](.) | **being actively worked on** | *Soon™* |

There were/are also a few unofficial versions that we are aware of:

| Name | Language | Authors | Source code | Status| Official invite |
| --- | --- | --- | --- | --- | --- |
| DuckySk | Skript | [@minemidnight](https://github.com/minemidnight) | [open](https://github.com/TheDuckyProject/DuckySk) | incomplete, abandoned | - |
| Ducky.sk | Skript | [@DerpyTheCoder](https://github.com/DerpyTheCoder) | [open](https://github.com/DerpyTheCoder/ducky.sk) | **being actively worked on** | - |

## Running

First, copy `config.json.example` to `config.json`, fill it in, then:

* `./gradlew run` to run
* `./gradlew shadowJar` to build a runnable jar with all the required dependencies, 
it will be in `build/libs/ducky-VERSION-all.jar`

### The config, explained
* `token` - a string, get it from 
[here](https://discordapp.com/developers/applications/me/)
* `accountType` - a string, either `BOT` or `CLIENT`
* `botName` - a string - a single word used by the command parser as one of the
 valid options for %bot%. Only alphanumeric characters are allowed
* `botAdminIds` - an array of IDs of people with the BotAdmin rank
* `botModIds` - an array of IDs of people with the BotMod rank

## How to add a command
First, open the project in your favorite IDE (in IntelliJ IDEA).
 
Once you do that, the simplest way to add a command is the following:
* choose a random command from the `commands` package, and copy the whole file
to another file, called `CmdCommandName` (obviously use an actual name):
  * if you're making the command in Kotlin, copy any Kotlin command, 
  to a file in `src/main/kotlin/com/skunity/ducky/commands` 
  * if you're making the command in Java, you can copy the 
  [`CmdJavaGuys`](src/main/java/com/skunity/ducky/commands/CmdJavaGuys.java) file,
  and put it in the directory `src/main/java/com/skunity/ducky/commands`
* delete everything inside of the `execute` method
* write the new command logic inside of the `execute` method
* properly fill in the command info - name, description, syntax and minimal required 
permission rank (look at other commands if you need examples)
* register the command in the 
[`AllCommands.kt`](src/main/kotlin/com/skunity/ducky/AllCommands.kt) file
(just look at how other commands are "registered" inside)

**Note:** if you add a pull request with a command written in Java, there's a high chance 
that we'll port it to Kotlin after merging

## Types in commands
`%bot%` - the @mention of the bot, or the name from the `config.json` - it ignores
the case when comparing, also letters can be repeated - if you name your bot
`Ducky` in the config, *HI DUUUCKYYY* will be a match for `hi %bot%` without any problems

**Note:** `%bot%` is the only type that is **not** added to the `arguments` array when detected, 
so with a syntax of `%bot% say %string%`, the String will be the **first** argument
(that you can get with `arguments[0]` or `arguments.first()`)

`%user%` - either a @mention of some user or their ID. They can be from any server where
the bot is in

`%member%` - like a `%user%`, but works only in guilds and only searches for
people in the same guild

`%biginteger%` - a `java.math.BigInteger` (a number like `1239054905409540954095409094`)

`%bigdecimal%` - a `java.math.BigDecimal` (a number like `348994834` or `989.39393`)

`%string%` - a string. Tries to match only one word when not on the end of a syntax,
otherwise tries to match the whole rest of the message.
