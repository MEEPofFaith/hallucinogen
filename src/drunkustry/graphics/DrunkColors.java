package drunkustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;
import mindustry.graphics.Shaders.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class DrunkColors{
    public static FrameBuffer drunkBuffer;
    public static DrunkShader drunkShader;

    public static void init(){
        drunkBuffer = new FrameBuffer();
        drunkShader = new DrunkShader("drunk");

        Events.run(Trigger.drawOver, () -> {
            drunkBuffer.resize(graphics.getWidth(), graphics.getHeight());
            Draw.draw(Layer.max, () -> {
                drunkBuffer.begin(Color.clear);
                Draw.rect();
                drunkBuffer.end();
                drunkBuffer.blit(drunkShader);
            });
        });
    }

    /** Copy of {@link SurfaceShader} that's able to get my shader. */
    public static class DrunkShader extends Shader{
        Texture noiseTex;

        public DrunkShader(String frag){
            super(files.internal("shaders/screenspace.vert"), tree.get("shaders/" + frag + ".frag"));
            loadNoise();
        }

        public DrunkShader(String vertRaw, String fragRaw){
            super(vertRaw, fragRaw);
            loadNoise();
        }

        public String textureName(){
            return "noise";
        }

        public void loadNoise(){
            Core.assets.load("sprites/" + textureName() + ".png", Texture.class).loaded = t -> {
                t.setFilter(TextureFilter.linear);
                t.setWrap(TextureWrap.repeat);
            };
        }

        @Override
        public void apply(){
            setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_resolution", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);

            if(hasUniform("u_noise")){
                if(noiseTex == null){
                    noiseTex = Core.assets.get("sprites/" + textureName() + ".png", Texture.class);
                }

                noiseTex.bind(1);
                renderer.effectBuffer.getTexture().bind(0);

                setUniformi("u_noise", 1);
            }
        }
    }
}
