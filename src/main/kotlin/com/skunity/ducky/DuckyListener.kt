package com.skunity.ducky

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

object DuckyListener : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val msg = event.message
        val author = event.author
        val channel = event.channel
        val raw = msg.contentRaw
        val lower = raw.toLowerCase()

        if (lower == "hail ducky") {
            channel.sendMessage("Kotlin is bae").queue()
        }
    }
}