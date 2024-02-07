package drunkustry.audio;

import arc.*;
import arc.audio.*;
import arc.audio.Filters.*;
import arc.math.*;
import mindustry.game.EventType.*;

public class DrunkSound{
    public static final FlangerFilter filter = new FlangerFilter(){{
        set(0.2f, 0.4f);
    }};

    public static void init(){
        Events.run(Trigger.update, () -> {
            Core.audio.globalPitch = 1.001f + Mathf.sin(150 * drunkScl() / Mathf.PI2, 0.5f * drunkMag());
        });

        Core.audio.setFilter(0, filter);
        Core.audio.setFilterParam(0, 0, Filters.paramWet, 1f);
    }

    public static float drunkMag(){
        return Core.settings.getInt("du-drunk-mag") / 10f;
    }
    public static float drunkScl(){
        return Core.settings.getInt("du-drunk-scl") / 10f;
    }
}
