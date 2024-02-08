package drunkustry;

import arc.*;
import arc.util.*;
import drunkustry.audio.*;
import drunkustry.graphics.*;
import drunkustry.ui.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;

import static mindustry.Vars.*;

public class Drunkdustry extends Mod{

    public Drunkdustry(){}

    @Override
    public void init(){
        DrunkSettings.init();
        DrunkSound.init();
        DrunkShaders.init();
        DrunkRendering.init();
    }
}
