package drunkustry;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;

import static mindustry.Vars.*;

public class Drunkustry extends Mod{

    public Drunkustry(){
        Events.on(ClientLoadEvent.class, e -> {
            loadSettings();
        });
    }

    @Override
    public void init(){
        Events.run(Trigger.update, () -> {
            Core.audio.globalPitch = Mathf.absin(300 * drunkScl() / Mathf.PI2, 2 * drunkMag()) + 0.1f;
        });
    }

    public float drunkMag(){
        return Core.settings.getInt("du-drunk-mag") / 10f;
    }
    public float drunkScl(){
        return Core.settings.getInt("du-drunk-scl") / 10f;
    }

    private void loadSettings(){
        ui.settings.sound.sliderPref("du-drunk-mag", 10, 1, 50, 1, s -> (s * 10) + "%");
        ui.settings.sound.sliderPref("du-drunk-scl", 10, 1, 50, 1, s -> Strings.autoFixed(s / 10f, 2) + "x");
    }
}
