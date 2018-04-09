package com.skunity.ducky

import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

object DuckyListener : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val msg = event.message
        val author = event.author
        val channel = event.channel
        val raw = msg.contentRaw
        val lower = raw.toLowerCase()

        if (lower.startsWith("ducky spam ")) {
            channel.sendWithTyping("x".repeat(lower.substring("ducky spam ".length).toInt()))
        }
    }

    override fun onReady(event: ReadyEvent) {
        println("The app has been enabled - running using the ${event.jda.selfUser.tag()} account")
    }
}








