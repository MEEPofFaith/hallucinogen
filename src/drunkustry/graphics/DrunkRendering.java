package drunkustry.graphics;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import drunkustry.ui.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;
import mindustry.type.*;

import static arc.Core.*;
import static drunkustry.graphics.DrunkShaders.*;
import static mindustry.Vars.*;

public class DrunkRendering{
    public static final float begin = Layer.min;
    public static final float end = Layer.max;

    public static void init(){
        initAberration();
        initColor();
        initDistortion();
        initInversion();

        Reflect.set(ui.menufrag, "renderer", new DrunkMenuRenderer());
    }

    private static void initAberration(){
        if(!settings.getBool("du-aberration", true)) return;

        Events.run(Trigger.drawOver, () -> {
            Draw.draw(begin + 0.02f, () -> chromaticAberration.begin());
            Draw.draw(end, () -> chromaticAberration.end());
        });
    }

    private static void initColor(){
        if(!settings.getBool("du-color", true)) return;

        Events.run(Trigger.drawOver, () -> Draw.draw(end, () -> {
            colorHallucination.begin();
            Draw.rect();
            colorHallucination.end();
        }));
    }

    private static void initDistortion(){
        if(!settings.getBool("du-distortion", true)) return;

        Events.run(Trigger.drawOver, () -> {
            Draw.draw(begin + 0.01f, () -> distortion.begin());
            Draw.draw(end, () -> distortion.end());
        });
    }

    private static void initInversion(){
        if(!settings.getBool("du-inversion", true)) return;

        Events.run(Trigger.drawOver, () -> {
            Draw.draw(begin, () -> inversion.begin());
            Draw.draw(end, () -> inversion.end());
        });
    }
}
