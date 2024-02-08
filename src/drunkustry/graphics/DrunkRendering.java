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
        Events.run(Trigger.drawOver, () -> {
            Draw.draw(begin, () -> {
                distortion.begin();
            });
            Draw.draw(begin + 0.01f, () -> {
                chromaticAberration.begin();
            });

            Draw.draw(end, () -> {
                chromaticAberration.end();

                colorHallucination.begin();
                Draw.rect();
                colorHallucination.end();

                distortion.end();
            });
        });
    }
}
