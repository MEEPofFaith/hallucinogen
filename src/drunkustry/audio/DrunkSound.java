package drunkustry.audio;

import arc.*;
import arc.math.*;
import mindustry.game.EventType.*;

public class DrunkSound{
    public static void init(){
        Events.run(Trigger.update, () -> {
            Core.audio.globalPitch = 1.001f + Mathf.sin(150 * drunkScl() / Mathf.PI2, 0.5f * drunkMag());
        });
    }

    public static float drunkMag(){
        return Core.settings.getInt("du-drunk-mag") / 10f;
    }
    public static float drunkScl(){
        return Core.settings.getInt("du-drunk-scl") / 10f;
    }
}
