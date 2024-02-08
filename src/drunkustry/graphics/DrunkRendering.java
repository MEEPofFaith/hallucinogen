package drunkustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;

import static arc.Core.*;
import static drunkustry.graphics.DrunkShaders.*;

public class DrunkRendering{
    public static final float begin = Layer.min;
    public static final float end = Layer.max;

    public static void init(){
        initAberration();
        initColor();
        initDistortion();
    }

    public static void initAberration(){
        if(!settings.getBool("du-aberration", true)) return;

        Events.run(Trigger.drawOver, () -> {
            Draw.draw(begin + 0.01f, () -> {
                chromaticAberration.begin();
            });
            Draw.draw(end, () -> {
                chromaticAberration.end();
            });
        });
    }

    public static void initColor(){
        if(!settings.getBool("du-color", true)) return;

        Events.run(Trigger.drawOver, () -> {
            Draw.draw(end, () -> {
                colorHallucination.begin();
                Draw.rect();
                colorHallucination.end();
            });
        });
    }

    public static void initDistortion(){
        if(!settings.getBool("du-distortion", true)) return;

        Events.run(Trigger.drawOver, () -> {
            Draw.draw(begin, () -> {
                distortion.begin();
            });
            Draw.draw(end, () -> {
                distortion.end();
            });
        });
    }
}
