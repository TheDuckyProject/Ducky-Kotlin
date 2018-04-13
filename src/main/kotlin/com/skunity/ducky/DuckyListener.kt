package com.skunity.ducky

import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.math.BigDecimal
import java.math.BigInteger

object DuckyListener : ListenerAdapter() {
    private val whitespacePattern = Regex("\\s")
    private val ignoredWordEndingsPattern = Regex("[!?.;,:<>()\\[\\]{}]+$")
    private val botPattern = Regex(Ducky.config.botName.toLowerCase().map { "$it+" }.joinToString(""))

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val msg = event.message
        val author = event.author
        val channel = event.channel
        val raw = msg.contentRaw
        val guild: Guild? = event.guild

        // TODO if the user is ignored, don't run the code below

        // we filter out the empty strings so when someone for example says
        // `hello<space><space>bot-name`, it will still be matched for `hello<space>%bot%`
        val msgSplit = raw.split(whitespacePattern).filter { it.trim().isNotEmpty() }

        // any word can end with an unlimited amount of !?.;,:<>()[]{} and it will be ignored by the matching system
        // ^ brackets mainly because they can be used as emoticons
        val splitNoPunctuation = msgSplit.map { it.replace(ignoredWordEndingsPattern, "") }

        allCommands.forEach {
            var parsedArgs = emptyList<Any>()

            val matchedPatternIndex = it.syntax.indexOfFirst {
                parsedArgs = emptyList() // resetting the variable after a possible previous iteration

                val syntaxSplit = it.split(" ")

                // if a message has less words than a pattern it clearly can't match that pattern
                if (syntaxSplit.size > msgSplit.size) {
                    return@indexOfFirst false
                }

                syntaxSplit.withIndex().all {
                    val syntaxWord = it.value
                    val index = it.index
                    val wordNoPunctuation = splitNoPunctuation[index]

                    if (syntaxWord.startsWith("%")) { // this word is a type
                        val wordToParse = msgSplit[index]

                        return@all when (syntaxWord) {
                            "%bot%" -> {
                                val lowerNoPunctuation = wordNoPunctuation.toLowerCase()

                                // if the bot name is 'ducky', 'duuuckkyyyyy' will work too
                                lowerNoPunctuation.matches(botPattern) ||
                                        //|| syntaxWord === Ducky.jda.selfUser.name.toString() // TODO option in the config or maybe not dunno
                                        wordToParse === "🦆" // :duck: emoji // TODO emoji in the config
                            }
                            "%user%" -> {
                                var user = event.jda.getUserById(
                                        wordToParse.replace("<@", "").replace(">", "").replace("!", ""))
                                if (user == null) {
                                    val users = event.jda.getUsersByName(wordToParse, true)
                                    if (users.size == 1) { // 0 means no one and >1 means we don't know which one
                                        user = users.first()
                                    }
                                }
                                if (user != null) {
                                    parsedArgs += user
                                    true
                                } else false
                            }
                            "%member%" -> {
                                guild != null && {
                                    var member = guild.getMemberById(
                                            wordToParse.replace("<@", "").replace(">", "").replace("!", ""))
                                    if (member == null) {
                                        val members = guild.getMembersByName(wordToParse, true)
                                        if (members.size == 1) { // 0 means no one and >1 means we don't know which one
                                            member = members.first()
                                        }
                                    }
                                    if (member != null) {
                                        parsedArgs += member
                                        true
                                    } else false
                                }.invoke()
                            }
                            "%string%" -> {
                                parsedArgs += if (index == syntaxSplit.size - 1) { // if this %string% is the last word in the pattern
                                    msgSplit.drop(index).joinToString(" ")
                                } else {
                                    wordToParse
                                }
                                true // any string matches %string%
                            }
                            "%biginteger%" -> {
                                try {
                                    parsedArgs += BigInteger(wordToParse)
                                    true
                                } catch (ex: NumberFormatException) {
                                    false
                                }
                            }
                            "%bigdecimal%" -> {
                                try {
                                    parsedArgs += BigDecimal(wordToParse)
                                    true
                                } catch (ex: NumberFormatException) {
                                    false
                                }
                            }
                            else -> {
                                System.err.println("In the syntax of the command ${it.javaClass.name}"
                                        + " there is an unknown type used - $syntaxWord")
                                false
                            }
                        }
                    }

                    // if it's a normal word (not a %type%), just return whether it's equal
                    syntaxWord.equals(wordNoPunctuation, ignoreCase = true)
                }
            }

            if (matchedPatternIndex == -1) return@forEach // else the syntax was matched, and we want to execute it

            val matchedPattern = it.syntax[matchedPatternIndex]

            val consoleLogLine = "Matched pattern '$matchedPattern' for message '$raw' by ${author.tag}"
            println("-".repeat(Math.min(100, consoleLogLine.length))) // TODO explain
            println(consoleLogLine)

            if (!it.minRank.check(author, channel)) { // if the author has no permissions
                println("Failed to execute the command because of insufficient permissions")
                return // TODO react somehow perhaps? config option? what by default?
            }

            it.execute(msg, parsedArgs)
            println("Command executed successfully")
        }
    }

    override fun onReady(event: ReadyEvent) {
        println("The app has been enabled - running using the ${event.jda.selfUser.tag} account")
    }
}








