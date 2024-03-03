package drunkustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import drunkustry.ui.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;

import static arc.Core.*;
import static drunkustry.graphics.DrunkShaders.*;
import static mindustry.Vars.*;

public class DrunkRendering{
    private static final FrameBuffer pingPong1 = new FrameBuffer();
    private static final FrameBuffer pingPong2 = new FrameBuffer();

    public static void init(){
        Events.run(Trigger.drawOver, () -> {
            Draw.draw(Layer.min, DrunkRendering::drawBegin);
            Draw.draw(Layer.max, DrunkRendering::drawEnd);
        });

        if(settings.getBool("du-menu-background")){
            Log.debug("Drunkifying menu renderer...");
            Reflect.set(ui.menufrag, "renderer", new DrunkMenuRenderer());
        }
    }

    public static void drawBegin(){
        pingPong1.resize(graphics.getWidth(), graphics.getHeight());
        pingPong2.resize(graphics.getWidth(), graphics.getHeight());

        pingPong1.begin(Color.clear);
    }

    public static void drawEnd(Camera camera){
        FrameBuffer from = pingPong1;

        if(settings.getBool("du-aberration")) from = pingPong(from, chromaticAberration);
        if(settings.getBool("du-distortion")) from = pingPong(from, distortion);
        if(settings.getBool("du-color")) drawScreen(from, colorHallucination);
        if(settings.getBool("du-inversion")) from = pingPong(from, inversion);

        from.end();
        Draw.rect(Draw.wrap(from.getTexture()), camera.position.x, camera.position.y, camera.width, -camera.height);
    }

    public static void drawEnd(){
        drawEnd(camera);
    }

    private static FrameBuffer pingPong(FrameBuffer from, DrunkShader shader){
        FrameBuffer to = from == pingPong1 ? pingPong2 : pingPong1;

        from.end();
        to.begin();
        shader.blit(from);

        return to;
    }

    private static void drawScreen(FrameBuffer active, DrunkShader shader){
        FrameBuffer screenBuffer = active == pingPong1 ? pingPong2 : pingPong1;

        screenBuffer.begin();
        Draw.rect();
        screenBuffer.end();
        shader.blit(screenBuffer);
    }
}
