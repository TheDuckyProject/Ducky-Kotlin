package com.skunity.ducky

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timerTask

/**
 * A [java.util.Timer] that we use for scheduling different stuff
 */
val timer = Timer()

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

/**
 * @return a random element from the array
 */
fun <E> Array<E>.random(): E = get(random.nextInt(size))

/**
 * Replaces every @everyone and @here with something that looks exactly the same, except
 * the `e` letter is replaced with a russian one, what causes Discord not to mention everyone/online people
 */
val String.noEveryoneHere
    get() = replace("@everyone", "@\u0435veryone").replace("@here", "@h\u0435re")

/**
 * Calls [noEveryoneHere] on the string, and then replaces every `>` with a `\>` - what discards all mentions,
 * and every tilde with a \tilde, what makes code blocks impossible to escape (and even if someone manages to do it,
 * in a way that I didn't think of, they still won't be able to do any harm)
 */
val String.escape
    get() = replace(">", "\\>").replace("`", "\\`").noEveryoneHere

/**
 * @param str the format to use, ISO 8601 by default
 * @return the current LocalDateTime converted to a String
 */
fun formattedLocalTime(str: String = "yyyy-MM-dd HH:mm:ss"): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern(str)
    val time = LocalDateTime.now()
    return time.format(dateTimeFormatter)
}