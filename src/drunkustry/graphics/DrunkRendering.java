package drunkustry.graphics;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;

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
            Draw.draw(end, () -> {
                float t = Time.time / 60f * Mathf.PI / 4f * settings.getFloat("du-inversion", 1f);
                float s = Mathf.sin(t, 1f, 1f) +
                    Mathf.sin(t, 1.3f, 1f) +
                    Mathf.sin(t, 1.7f, 1f) +
                    Mathf.sin(t, 0.5f, 1f) +
                    Mathf.sin(t, 0.8f, 1f);
                if(!state.isPaused()) inversion.lerp = Mathf.clamp(inversion.lerp + s / 5f / 10f * Time.delta, 0f, 1f);
                inversion.end();
            });
        });
    }
}
