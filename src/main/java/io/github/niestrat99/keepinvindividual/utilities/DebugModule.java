package io.github.niestrat99.keepinvindividual.utilities;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DebugModule {

    public static void info(String infoMessage) {
        if (Config.config.getBoolean("debug.enabled")) { KeepInvIndividual.get().getLogger().info("[DEBUG] " + infoMessage); }
    }

    public static void warn(String warnMessage) {
        if (Config.config.getBoolean("debug.enabled")) {
            Random randomNumber = new Random();
            String funnyLine = errorMessages.get(randomNumber.nextInt(errorMessages.size()));
            KeepInvIndividual.get().getLogger().warning("[DEBUG] " + funnyLine + " - " + warnMessage);
        }
    }

    private static final List<String> errorMessages = new ArrayList<>(Arrays.asList(
            "Ah, fiddlesticks! What now!?",
            "Oh, good heavens!",
            "*chuckles* I'm in danger!",
            "Oopsie whoopsie!",
            "AHHH!!",
            "Oh no! Cringe!",
            "Well, if it isn't the consequences of my actions!",
            "I say gentlemen, I do believe we're in quite a spot of bother!",
            "Oops, my finger slipped!",
            "Ohhh, that's gotta hurt!",
            "Womp, womp!"
    ));
}
