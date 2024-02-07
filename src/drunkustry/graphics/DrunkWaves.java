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

        Events.run(Trigger.drawOver, () -> {
            wavesBuffer.resize(graphics.getWidth(), graphics.getHeight());
            Draw.draw(Layer.background - 0.01f, () -> wavesBuffer.begin(Color.clear));
            Draw.draw(Layer.endPixeled + 4, () -> {
                wavesBuffer.end();
                wavesBuffer.blit(DrunkShaders.drunkWaves);
            });
        });
    }
}
