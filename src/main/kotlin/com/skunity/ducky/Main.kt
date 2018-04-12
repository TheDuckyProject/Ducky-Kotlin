package com.skunity.ducky

import com.beust.klaxon.Klaxon
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.User
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

object Ducky {
    val config = Klaxon().parse<Config>(File("config.json"))!!
    val jda = JDABuilder(AccountType.valueOf(Ducky.config.accountType))
            .setToken(Ducky.config.token)
            .addEventListener(DuckyListener)
            .buildBlocking()
    var typingChannelToEndTime = emptyMap<MessageChannel, Long>()

    init {
        // Here we schedule resending the "typing" messages to Discord,
        // when typing in some channel is not completed within 7 seconds
        timer.scheduleAtFixedRate(timerTask {
            val now = System.currentTimeMillis()
            typingChannelToEndTime.forEach { (channel, endTime) ->
                if (endTime < now) {
                    Ducky.typingChannelToEndTime -= channel
                } else {
                    channel.sendTyping().queue()
                }
            }
        }, 100, 7000)
    }
}

class Config(val token: String, val accountType: String, val botName: String,
             val botAdminIds: List<String>, val botModIds: List<String>)


// ***** Some global stuff *****

/**
 * A [java.util.Timer] that we use for scheduling different stuff
 */
val timer = Timer()

/**
 * The main function which does nothing on it's own, just initializes [Ducky]
 */
fun main(args: Array<String>) {
    Ducky
}

/**
 * A random variable to be honest
 */
val random = Random()

/**
 * Sends a message with a delay, during which on Discord users see "Botname is typing..." in the channel
 * The algorithm for the delay is:
 *
 * max(300, content length * (random integer between 20 and 80)) in milliseconds,
 *
 * where content length is the length of the "normal" content (so not counting anything in embeds).
 * So, if a message has no text or only a few characters, it will always take 300 milliseconds to send it
 *
 * @param msg The message to send
 */
fun MessageChannel.sendWithTyping(msg: Message) {
    // msg.contentRaw is empty for messages with only an embed, we send them after 300ms..
    // .. we don't really send anything after **less** than 300 milliseconds of typing
    val millisToTake = Math.max(300, msg.contentRaw.length * (20..80).random()).toLong()
    val endMillis = System.currentTimeMillis() + millisToTake

    val previousMillisOpt = Ducky.typingChannelToEndTime[this]

    if (previousMillisOpt == null) {
        sendTyping().queue() // if Ducky wasn't typing in this channel before, it starts instantly
    }

    // if Ducky was typing here AND the time when he should finish, saved in the map,
    // is smaller than the new calculated time, we update the ending time in the map
    if (previousMillisOpt?.run { this < endMillis } != false) {
        Ducky.typingChannelToEndTime += (this@sendWithTyping to endMillis)
    }

    // here we schedule actually sending the message
    timer.schedule(timerTask {
        sendMessage(msg).queue()

        // if Ducky is typing some other message here, we re-send the typing "packet" 100ms after we send the message
        Ducky.typingChannelToEndTime[this@sendWithTyping]
                // no point in sending typing for a message
                // that will be sent in less than half a second *imo*
                // (could be easily abused) (well probably most of this is easy to abuse but anyway)
                ?.takeIf { it - 500 > System.currentTimeMillis() }?.run {
                    Thread.sleep(100) // so it's a *bit* more realistic
                    sendTyping().queue()
                }
    }, millisToTake)
}

fun MessageChannel.sendWithTyping(str: String) {
    sendWithTyping(MessageBuilder(str).build())
}

fun MessageChannel.sendWithTyping(embed: MessageEmbed) {
    sendWithTyping(MessageBuilder(embed).build())
}

// ***** Some nice global extension methods *****


/**
 * An extension method which makes is possible to do `(5..10).random()` to get a random integer between 5 and 10
 */
fun ClosedRange<Int>.random() = random.nextInt((endInclusive + 1) - start) + start

/**
 * An extension method which returns `Name#discrim`, like `Nicofisi#4467`,
 * which doesn't technically mention the user when sent in a message
 */
val User.tag
    get() = "$name#$discriminator"

/**
 * @return a random element from the list
 */
fun <E> List<E>.random(): E = get(random.nextInt(size))