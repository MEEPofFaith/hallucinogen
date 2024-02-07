package drunkustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;

import static arc.Core.*;

public class DrunkColors{
    public static FrameBuffer drunkBuffer;

    public static void init(){
        drunkBuffer = new FrameBuffer();

        Events.run(Trigger.drawOver, () -> {
            drunkBuffer.resize(graphics.getWidth(), graphics.getHeight());
            Draw.draw(Layer.endPixeled + 2, () -> {
                drunkBuffer.begin(Color.clear);
                Draw.rect();
                drunkBuffer.end();
                drunkBuffer.blit(DrunkShaders.drunkShader);
            });
        });
    }
}
