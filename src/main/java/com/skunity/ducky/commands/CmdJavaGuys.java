package com.skunity.ducky.commands;

import com.skunity.ducky.cmdapi.DuckyCommand;
import com.skunity.ducky.cmdapi.Rank;
import kotlin.collections.CollectionsKt;
import net.dv8tion.jda.core.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

/**
 * @author NanoDankster
 */
public class CmdJavaGuys extends DuckyCommand {

    public CmdJavaGuys() {
        name = "Java command";
        description = "For *java guys*.";
        syntax = CollectionsKt.listOf("java guys", "*java guys*");
        minRank = Rank.EveryoneOnGuilds.INSTANCE;
    }

    @Override
    public void execute(@NotNull Message message, @NotNull List<?> arguments) {
        String[] possibleMessages = {
                "java *succs* btw",
                "kys *java guys*",
                "duccs hate coffe",
                "Remember, no *java guy*."
        };

        message.getChannel().sendMessage(possibleMessages[new Random().nextInt(possibleMessages.length)]).queue();
    }
}
