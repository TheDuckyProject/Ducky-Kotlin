import com.skunity.ducky.MainKt;
import com.skunity.ducky.cmdapi.DuckyCommand;
import com.skunity.ducky.cmdapi.Rank;
import kotlin.collections.CollectionsKt;
import net.dv8tion.jda.core.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestCmd extends DuckyCommand {

    TestCmd() {
        name = "Say Hi";
        description = "Send a message that matches this syntax if you want me to reply \"hi\" to you";
        syntax = CollectionsKt.listOf("hello %bot%", "howdy %bot%", "hi there %bot%", "hi %bot%", "hey %bot%");
        minRank = Rank.Everyone.INSTANCE;
    }

    @Override
    public void execute(@NotNull Message msg, @NotNull List<?> args) {
        MainKt.sendWithTyping(msg.getChannel(), "Hi " + msg.getAuthor().getAsMention() + "!");
    }
}
