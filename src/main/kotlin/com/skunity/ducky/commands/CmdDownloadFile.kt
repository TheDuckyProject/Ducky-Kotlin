package com.skunity.ducky.commands

import com.github.kittinunf.fuel.Fuel
import com.skunity.ducky.DownloadedItem
import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.readDataArray
import com.skunity.ducky.saveData
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.api.entities.Message
import java.io.File
import java.time.Instant

/**
 * @author Nicofisi
 */
object CmdDownloadFile : DuckyCommand() {
    init {
        name = "Download File"
        description = "Make Ducky download a file so he can send it later"
        syntax = listOf(
            "%bot% save %string% as %string%",
            "%bot% download %string% and save it as %string%"
        )
        minRank = Rank.BotMod // TODO add the potato rank or something

        File("./data/downloaded/").mkdirs()
    }

    private const val USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3694.0 Safari/537.36"

    override fun execute(message: Message, arguments: List<Any>) {
        val url = arguments[0] as String
        val name = arguments[1] as String

        if (name.length > 40) {
            message.channel.sendWithTyping("A forty character long name is the most I can handle!")
            return
        } else if (name.length < 4) {
            message.channel.sendWithTyping("The name needs to be at least 4 characters long")
            return
        }

        val fileId = name
            .replace(Regex("\bthe\b"), "") // FIXME the `\b`s work only on regex101.com?
            .replace(Regex("\ba\b"), "")
            .replace(Regex("\ban\b"), "")
            .replace(Regex("[^A-Za-z0-9]"), "")
            .toLowerCase() // TODO prettify

        var fileName = fileId + "-" + Instant.now().epochSecond

        val lastDot = url.lastIndexOf(".")
        if (lastDot > url.lastIndexOf("/") && lastDot != -1) {
            fileName += url.substring(lastDot)
        }

        Fuel.download(url)
            .fileDestination { _, _ -> File("./data/downloaded/$fileName") }
            .header("User-Agent", USER_AGENT)
            .response { _, _, result ->
                // FIXME handle errors
                val items = readDataArray<DownloadedItem>(null, "downloads")().orEmpty().toMutableList()
                items += DownloadedItem(fileName, name, message.author.id, message.author.asTag)
                saveData(null, "downloads", items)

                message.channel.sendWithTyping(
                    "Yay! I have a new file stored in my directory! I saved it as `$fileName`. " +
                            "Send a message matching the `%bot% share $name` pattern if you want me to share it!"
                )
            }
    }
}
