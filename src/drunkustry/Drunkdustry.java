package drunkustry;

import arc.*;
import arc.util.*;
import drunkustry.audio.*;
import drunkustry.graphics.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class Drunkdustry extends Mod{

    public Drunkdustry(){
        Events.on(ClientLoadEvent.class, e -> {
            loadSettings();
        });
    }

    @Override
    public void init(){
        DrunkSound.init();
        DrunkColors.init();
    }

    private void loadSettings(){
        ui.settings.sound.sliderPref("du-drunk-mag", 10, 1, 20, 1, s -> (s * 10) + "%");
        ui.settings.sound.sliderPref("du-drunk-scl", 10, 1, 50, 1, s -> Strings.autoFixed(s / 10f, 2) + "x");
    }
}
