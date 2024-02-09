package drunkustry.audio;

import arc.*;
import arc.audio.*;
import arc.audio.Filters.*;
import arc.math.*;
import mindustry.game.EventType.*;

import static arc.Core.settings;

public class DrunkSound{
    public static FlangerFilter filter;

    public static void init(){
        initPitch();
        initFlanger();
    }

    public static void initPitch(){
        if(!settings.getBool("du-pitch", true)) return;

        Events.run(Trigger.update, () -> {
            Core.audio.globalPitch = 1.001f + Mathf.sin(
                150 / settings.getFloat("du-drunk-scl", 1f) / Mathf.PI2,
                0.5f * settings.getFloat("du-drunk-mag", 1f)
            );
        });
    }

    public static void initFlanger(){
        if(!settings.getBool("du-flanger", true)) return;

        filter = new FlangerFilter(){{
            set(0.1f, 0.3f);
        }};
        Core.audio.setFilter(0, filter);
        Core.audio.setFilterParam(0, 0, Filters.paramWet, 1f);
    }
}
