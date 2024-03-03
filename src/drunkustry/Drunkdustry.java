package drunkustry;

import drunkustry.audio.*;
import drunkustry.graphics.*;
import drunkustry.ui.*;
import mindustry.mod.*;

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
