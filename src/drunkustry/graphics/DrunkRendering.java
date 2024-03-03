package drunkustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
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
    private static final FrameBuffer pingPong1 = new FrameBuffer();
    private static final FrameBuffer pingPong2 = new FrameBuffer();
    private static final FrameBuffer screenBuffer = new FrameBuffer();

    public static void init(){
        Events.run(Trigger.drawOver, () -> {
            Draw.draw(Layer.min, DrunkRendering::drawBegin);
            Draw.draw(Layer.max, DrunkRendering::drawEnd);
        });

        if(settings.getBool("du-menu-background", true)){
            Log.debug("Drunkifying menu renderer...");
            Reflect.set(ui.menufrag, "renderer", new DrunkMenuRenderer());
        }
    }

    public static void drawBegin(){
        pingPong1.resize(graphics.getWidth(), graphics.getHeight());
        pingPong2.resize(graphics.getWidth(), graphics.getHeight());
        screenBuffer.resize(graphics.getWidth(), graphics.getHeight());

        pingPong1.begin(Color.clear);
    }

    public static void drawEnd(){
        FrameBuffer from = pingPong1;

        if(settings.getBool("du-aberration", true)) from = pingPong(from, chromaticAberration);
        if(settings.getBool("du-distortion", true)) from = pingPong(from, distortion);
        if(settings.getBool("du-color", true)) drawScreen(colorHallucination);
        if(settings.getBool("du-inversion", true)) from = pingPong(from, inversion);

        from.end();
        from.blit(none);
    }

    private static FrameBuffer pingPong(FrameBuffer from, DrunkShader shader){
        FrameBuffer to = from == pingPong1 ? pingPong2 : pingPong1;

        from.end();
        to.begin();
        shader.blit(from);

        return to;
    }

    private static void drawScreen(DrunkShader shader){
        screenBuffer.begin();
        Draw.rect();
        screenBuffer.end();
        shader.blit(screenBuffer);
    }
}
