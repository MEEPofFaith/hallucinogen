package drunkustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;

import static arc.Core.*;

public class DrunkWaves{
    private static FrameBuffer wavesBuffer;

    public static void init(){
        wavesBuffer = new FrameBuffer();

        Events.run(Trigger.draw, () -> wavesBuffer.resize(graphics.getWidth(), graphics.getHeight()));
        Events.run(Trigger.drawOver, () -> {
            Draw.draw(Layer.min, () -> wavesBuffer.begin(Color.clear));
            Draw.draw(Layer.max, () -> {
                wavesBuffer.end();
                wavesBuffer.blit(DrunkShaders.drunkWaves);
            });
        });
    }
}
