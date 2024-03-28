package drunkustry.graphics;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.Shaders.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class DrunkShaders{
    public static PassthroughShader passThrough;
    public static HallucinationShader colorHallucination;
    public static AberrationShader chromaticAberration;
    public static DistortionShader distortion;
    public static InversionShader inversion;

    public static void init(){
        passThrough = new PassthroughShader();
        colorHallucination = new HallucinationShader();
        chromaticAberration = new AberrationShader();
        distortion = new DistortionShader();
        inversion = new InversionShader();
    }

    public static class HallucinationShader extends DrunkScreenShader{
        public HallucinationShader(){
            super("colorHallucination");
        }

        @Override
        public void applyOther(){
            setUniformf("u_alpha", settings.getFloat("du-color-alpha"));
        }

        @Override
        public float timeScale(){
            return settings.getFloat("du-color-speed");
        }
    }

    public static class AberrationShader extends DrunkScreenShader{
        public AberrationShader(){
            super("chromaticAberration");
        }

        @Override
        public void applyOther(){
            setUniformf("u_scl", settings.getFloat("du-aberration-amount"));
        }

        @Override
        public float timeScale(){
            return settings.getFloat("du-aberration-speed");
        }
    }

    public static class DistortionShader extends DrunkScreenShader{
        public DistortionShader(){
            super("distortion");
        }

        @Override
        public void applyOther(){
            setUniformf("u_scl", settings.getFloat("du-distortion-amount"));
            setUniformf("u_insanity", settings.getFloat("du-distortion-insanity"));
        }

        @Override
        public float timeScale(){
            return settings.getFloat("du-distortion-speed");
        }
    }

    public static class InversionShader extends DrunkShader{
        public float lerp = 0f;

        public InversionShader(){
            super("inversion");
        }

        @Override
        public void apply(){
            float freq = settings.getFloat("du-inversion-freq");
            float t = Time.time / 60f * Mathf.PI / 4f * freq;
            float s = Mathf.sin(t, 1f, 1f) +
                Mathf.sin(t, 1.3f, 1f) +
                -Mathf.sin(t, 1.7f, 1f) +
                Mathf.sin(t, 0.5f, 1f) +
                -Mathf.sin(t, 0.8f, 1f);
            s /= 5f * freq * 0.5f;
            if(!state.isPaused()) inversion.lerp = Mathf.clamp(inversion.lerp + s * Time.delta, 0f, 1f);

            setUniformf("u_lerp", lerp);
            super.apply();
        }
    }

    /** Copy of {@link SurfaceShader} that's able to get my shader. */
    public static class DrunkScreenShader extends DrunkShader{
        protected Texture noiseTex;

        public DrunkScreenShader(String frag){
            super(frag);
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

        public void applyOther(){
        }

        public float timeScale(){
            return 1f;
        }

        @Override
        public void apply(){
            setUniformf("u_campos", x, y);
            setUniformf("u_resolution", w, h);
            setUniformf("u_time", Time.time * timeScale());
            applyOther();

            if(hasUniform("u_noise")){
                if(noiseTex == null){
                    noiseTex = Core.assets.get("sprites/" + textureName() + ".png", Texture.class);
                }

                noiseTex.bind(1);
                texture.bind(0);
                setUniformi("u_noise", 1);
            }
        }
    }

    public static class DrunkShader extends Shader{
        protected Texture texture;
        protected float x, y, w, h;

        public DrunkShader(String frag){
            super(getShaderFi("screenspace.vert"), getShaderFi(frag + ".frag"));
        }

        public void blit(FrameBuffer buffer, Camera camera){
            texture = buffer.getTexture();
            buffer.blit(this);

            w = camera.width;
            h = camera.height;
            x = camera.position.x - w / 2;
            y = camera.position.y - h / 2f;
        }

        @Override
        public void apply(){
            texture.bind(0);
        }
    }

    public static class PassthroughShader extends Shader{
        public PassthroughShader(){
            super(getShaderFi("screenspace.vert"), getShaderFi("passThrough.frag"));
        }
    }

    public static Fi getShaderFi(String file){
        return tree.get("shaders/" + file);
    }
}
