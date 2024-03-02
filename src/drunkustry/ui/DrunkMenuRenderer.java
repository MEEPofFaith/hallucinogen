package drunkustry.ui;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.type.*;

import static arc.Core.settings;
import static drunkustry.graphics.DrunkShaders.*;

public class DrunkMenuRenderer extends MenuRenderer{
    private final boolean abb, col, dis, inv;

    public DrunkMenuRenderer(){
        super();
        abb = settings.getBool("du-aberration", true);
        col = settings.getBool("du-color", true);
        dis = settings.getBool("du-distortion", true);
        inv = settings.getBool("du-inversion", true);
    }

    public DrunkMenuRenderer(UnitType flyerType){
        this();
        Reflect.set(MenuRenderer.class, this, "flyerType", flyerType);
    }

    @Override
    public void render(){
        if(inv) inversion.begin();
        if(dis) distortion.begin();
        if(abb) chromaticAberration.begin();
        super.render();
        if(abb) chromaticAberration.end();
        if(col){
            colorHallucination.begin();
            Draw.rect();
            colorHallucination.end();
        }
        if(dis) distortion.end();
        if(inv) inversion.end();
    }
}
