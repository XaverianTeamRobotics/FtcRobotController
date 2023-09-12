package net.xbhs.robotics.utils;

import com.noahbres.meepmeep.MeepMeep;

public class Runner {

    public static void run(Bot... bots) {
        Environment.MEEP.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f);
        for(Bot bot : bots) {
            Environment.MEEP.addEntity(bot.bot());
        }
        Environment.MEEP.start();
    }

}
