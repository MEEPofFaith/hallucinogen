package drunkustry.graphics;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.gl.*;
import arc.util.*;
import mindustry.graphics.Shaders.*;

import static arc.Core.files;
import static mindustry.Vars.renderer;
import static mindustry.Vars.tree;

public class DrunkShaders{
    public static DrunkShader drunkShader;
    public static DrunkWaves drunkWaves;

    public static void init(){
        drunkShader = new DrunkShader("drunk");
        drunkWaves = new DrunkWaves();
    }

    public static class DrunkWaves extends Shader{
        public DrunkWaves(){
            super(getShaderFi("screenspace.vert"), getShaderFi("drunkWaves.frag"));
        }

        @Override
        public void apply(){
            setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_resolution", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);
        }
    }

    /** Copy of {@link SurfaceShader} that's able to get my shader. */
    public static class DrunkShader extends Shader{
        Texture noiseTex;

        public DrunkShader(String frag){
            super(getShaderFi("screenspace.vert"), getShaderFi(frag + ".frag"));
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

    public static Fi getInternalShaderFi(String file){
        return files.internal("shaders/" + file);
    }

    public static Fi getShaderFi(String file){
        return tree.get("shaders/" + file);
    }
}
