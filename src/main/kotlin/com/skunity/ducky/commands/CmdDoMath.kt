package com.skunity.ducky.commands

import com.skunity.ducky.cmdapi.DuckyCommand
import com.skunity.ducky.cmdapi.Rank
import com.skunity.ducky.escape
import com.skunity.ducky.sendWithTyping
import net.dv8tion.jda.core.entities.Message
import javax.script.Invocable
import javax.script.ScriptEngineManager
import javax.script.ScriptException

/**
 * @author Nicofisi
 */
object CmdDoMath : DuckyCommand() {
    private val engine = ScriptEngineManager().getEngineByName("js")
    private val invocable = engine as Invocable // Kotlin really doesn't have union types???

    init {
        name = "Do math"
        description = "Let Ducky help you with your homework"
        syntax = listOf("do math %string%", "do maths %string%") // %bot% or no?
        minRank = Rank.Everyone

        engine.eval(javaClass.classLoader.getResource("math.min.js").readText()) // load math.js
        engine.eval("var parser = math.parser();")
        engine.eval("function eval(expr) { return parser.eval(expr) }")
        engine.eval("function format(expr, precision) { return math.format(expr, precision) }")
    }

    override fun execute(message: Message, arguments: List<Any>) {
        try {
            message.channel.sendWithTyping("Answer: " + eval(arguments[0] as String).escape)
        } catch (ex: ScriptException) {
            message.channel.sendWithTyping(ex.message?.escape ?: "An unknown error has occurred")
        }
    }

    private fun eval(expr: String): String {
        val evaluated = invocable.invokeFunction("eval", expr)
        return invocable.invokeFunction("format", evaluated, 14) as String
    }
}