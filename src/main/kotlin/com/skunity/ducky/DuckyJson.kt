package com.skunity.ducky

import com.beust.klaxon.Klaxon
import net.dv8tion.jda.core.entities.User
import java.io.File

fun jsonFile(dirName: String?, fileName: String): File {
    return File(if (dirName == null) "data/$fileName.json" else "data/$dirName/$fileName.json")
}

inline fun <reified T> readData(dirName: String?, fileName: String) = {
    val jsonFile = jsonFile(dirName, fileName)
    if (jsonFile.exists() && jsonFile.isFile) {
        Klaxon().parse<T>(jsonFile)
    } else null
}

// TODO an execution service to save only from one thread to prevent data corruption
fun saveData(dirName: String?, fileName: String, data: Any) {
    val jsonFile = jsonFile(dirName, fileName)

    if (!jsonFile.exists()) {
        jsonFile.parentFile.mkdirs()
        jsonFile.createNewFile()
    }

    jsonFile.writeText(Klaxon().toJsonString(data))
}

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

// Json object classes come below

/**
 * User data
 */
data class UserData(val id: String, var skuCounts: Int = 0, var penisLength: Int = -1)

/**
 * Ducky configuration, the program should never try to change it
 * The config file is meant to be modified only by the owner of the bot,
 * if you need to save something use `saveData`
 */
data class Config(val token: String, val accountType: String, val botName: String,
             val botAdminIds: List<String>, val botModIds: List<String>)

data class BotData(var startCount: Int = 0)
