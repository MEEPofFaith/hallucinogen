package drunkustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;

import static arc.Core.*;

public class DrunkRendering{
    public static FrameBuffer colorBuffer;
    public static FrameBuffer aberrationBuffer;
    private static FrameBuffer distortionBuffer;

    public static void init(){
        colorBuffer = new FrameBuffer();
        aberrationBuffer = new FrameBuffer();
        distortionBuffer = new FrameBuffer();

        Events.run(Trigger.draw, () -> {
            colorBuffer.resize(graphics.getWidth(), graphics.getHeight());
            aberrationBuffer.resize(graphics.getWidth(), graphics.getHeight());
            distortionBuffer.resize(graphics.getWidth(), graphics.getHeight());

            Draw.draw(Layer.background - 0.02f, () -> {
                distortionBuffer.begin(Color.clear);
            });
            Draw.draw(Layer.background - 0.01f, () -> {
                aberrationBuffer.begin(Color.clear);
            });

            Draw.draw(Layer.endPixeled + 1, () -> {
                aberrationBuffer.end();
                aberrationBuffer.blit(DrunkShaders.chromaticAberration);

                colorBuffer.begin(Color.clear);
                Draw.rect();
                colorBuffer.end();
                colorBuffer.blit(DrunkShaders.colorHallucination);

                distortionBuffer.end();
                distortionBuffer.blit(DrunkShaders.distortion);
            });
        });
    }
}
