package com.skunity.ducky

import com.beust.klaxon.Klaxon
import net.dv8tion.jda.api.entities.User
import java.io.File

fun jsonFile(dirName: String?, fileName: String): File {
    return File(if (dirName == null) "data/$fileName.json" else "data/$dirName/$fileName.json")
}


// General data reading & saving

inline fun <reified T> readData(dirName: String?, fileName: String) = {
    val jsonFile = jsonFile(dirName, fileName)
    if (jsonFile.exists() && jsonFile.isFile) {
        Klaxon().parse<T>(jsonFile)
    } else null
}

inline fun <reified T> readDataArray(dirName: String?, fileName: String) = {
    val jsonFile = jsonFile(dirName, fileName)
    if (jsonFile.exists() && jsonFile.isFile) {
        Klaxon().parseArray<T>(jsonFile.readText())
    } else null
}

// TODO an execution service to save only from one thread to prevent data corruption
fun saveData(dirName: String?, fileName: String, data: Any) {
    val jsonFile = jsonFile(dirName, fileName)

    if (!jsonFile.exists()) {
        jsonFile.parentFile.mkdirs()
        jsonFile.createNewFile()
    }

    jsonFile.writeText(Klaxon().toJsonString(data)) // TODO how the heck does one pretty-print this
}


// UserData reading & saving

fun readUserData(userId: String): UserData {
    require(userId.matches(Regex("\\d+")))

    return readData<UserData>("users", userId)() ?: UserData(userId)
}

fun readUserData(user: User) = readUserData(user.id)

fun saveUserData(userId: String, data: UserData) {
    require(userId.matches(Regex("\\d+")))

    saveData("users", userId, data)
}

fun saveUserData(user: User, data: UserData) = saveUserData(user.id, data)


// BotData reading & saving

fun saveBotData(data: BotData) {
    saveData(null, "ducky", data)
}

fun readBotData(): BotData {
    return readData<BotData>(null, "ducky")() ?: BotData()
}

// Json object classes come below

/**
 * User data
 */
data class UserData(val id: String, var skuCounts: Int = 0, var penisLength: Int = -1, var isPotato: Boolean = false)

/**
 * Ducky configuration, the program should never try to change it
 * The config file is meant to be modified only by the owner of the bot,
 * if you need to save something use `saveData`
 */
data class BotConfig(
    val token: String, val botName: String,
    val botAdminIds: List<String>, val botModIds: List<String>
)

/**
 * @property startCount how many times the bot was successfully enabled
 * @property totalUptime how many time, in milliseconds, the bot has been running in total. Updated every minute
 */
data class BotData(var startCount: Int = 0, var totalUptime: Long = 0)

//data class DownloadedData(var items: List<DownloadedItem> = emptyList())

data class DownloadedItem(val fileName: String, val originalName: String, val addedById: String, val addedByTag: String)
