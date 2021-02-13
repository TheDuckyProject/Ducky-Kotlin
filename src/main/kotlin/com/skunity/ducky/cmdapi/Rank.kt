package com.skunity.ducky.cmdapi

import com.skunity.ducky.Ducky
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*

abstract class Rank private constructor() {
    /**
     * Will be called every time someone tries to execute a command
     * that requires this Rank in a guild, to check whether they should be allowed
     */
    abstract fun checkInGuild(member: Member, channel: TextChannel): Boolean

    /**
     * Will be called every time someone tries to execute a command
     * that requires this Rank in a direct message, to check whether they should be allowed
     */
    abstract fun checkInDirectMessage(user: User): Boolean

    fun check(user: User, channel: MessageChannel): Boolean = when (channel.type) {
        ChannelType.TEXT -> (channel as TextChannel).guild.getMember(user)?.let { checkInGuild(it, channel) } ?: false
        ChannelType.PRIVATE -> checkInDirectMessage(user)
        ChannelType.GROUP -> checkInDirectMessage(user) // it's fine, isn't it?
        else -> throw IllegalArgumentException("The channel ${channel.type} isn't supported, kys")
    }


    // ***** The only supported ranks are below (the Rank constructor is private) *****

    /**
     * Only people defined as bot admins in the config.json file
     * will be able to execute commands that require this rank
     */
    object BotAdmin : Rank() {
        override fun checkInGuild(member: Member, channel: TextChannel): Boolean = checkInDirectMessage(member.user)
        override fun checkInDirectMessage(user: User): Boolean = Ducky.config.botAdminIds.contains(user.id)
    }

    /**
     * Only people defined as bot moderators in the config.json file
     * will be able to execute commands that require this rank
     */
    object BotMod : Rank() {
        override fun checkInGuild(member: Member, channel: TextChannel): Boolean = checkInDirectMessage(member.user)
        override fun checkInDirectMessage(user: User): Boolean =
                Ducky.config.botAdminIds.contains(user.id) || Ducky.config.botModIds.contains(user.id)
    }

    // could be enhanced to support varargs of Permission if anyone sees a need for that,
    // or maybe a support for requiring multiple Ranks should be added to commands
    class WithPermission(private val permission: Permission) : Rank() {
        override fun checkInGuild(member: Member, channel: TextChannel): Boolean =
                member.hasPermission(channel, permission)
                        || BotAdmin.checkInGuild(member, channel) // bot admins override permissions

        // if a guild permission is needed to perform a command, it means that the command performs
        // some guild-related task, that isn't supported in direct messages, hence why we always return false
        override fun checkInDirectMessage(user: User): Boolean = false
    }

    /**
     * Everyone will be able to execute this command everywhere
     */
    // ..TODO except for people who are ignored, probably every #checkSomewhere in this file should return false for them
    object Everyone : Rank() {
        override fun checkInGuild(member: Member, channel: TextChannel): Boolean = true
        override fun checkInDirectMessage(user: User): Boolean = true
    }

    /**
     * Everyone will be able to execute this command on a guild,
     * but no one will be able to execute it in direct messages or groups
     */
    object EveryoneOnGuilds : Rank() {
        override fun checkInGuild(member: Member, channel: TextChannel): Boolean = true
        override fun checkInDirectMessage(user: User): Boolean = false
    }

    // TODO I think I wanted to add some more roles :thinking:
}
