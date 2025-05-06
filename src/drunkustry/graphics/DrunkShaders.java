package drunkustry.graphics;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.util.*;
import drunkustry.graphics.DrunkShaders.AberrationShader.*;
import mindustry.graphics.Shaders.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class DrunkShaders{
    public static PassThroughShader passThrough;
    public static HallucinationShader colorHallucination;
    public static AberrationShader chromaticAberration;
    public static DistortionShader distortion;
    public static InversionShader inversion;
    public static BlurShader blur;

    public static void init(){
        passThrough = new PassThroughShader();
        colorHallucination = new HallucinationShader();
        chromaticAberration = new AberrationShader();
        distortion = new DistortionShader();
        inversion = new InversionShader();
        blur = new BlurShader();
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
        private float aberDir = 0f;

        public AberrationShader(){
            super("chromaticAberration");
        }

        @Override
        public void applyOther(){
            setUniformf("u_scl", settings.getFloat("du-aberration-amount"));
            setUniformi("u_mode", settings.getInt("du-aberration-mode"));

            float sTime = Time.time / 60f;
            if(settings.getBool("du-aberration-rotation")){
                if(!state.isPaused()){
                    float amount = Mathf.sin(sTime * 1.7f)
                        - Mathf.sin(sTime * 2.3f)
                        + Mathf.sin(sTime * 0.2f)
                        + Mathf.cos(sTime * 3f)
                        - Mathf.sin(sTime * 1.2f);
                    amount *= settings.getFloat("du-aberration-rotation-speed") / 2f;
                    aberDir += amount * Time.delta;
                }

                setUniformf("u_dir", aberDir * Mathf.degRad);
            }else{
                setUniformf("u_dir", 0f);
            }

            float amount = Mathf.sin(sTime * 2.1f);
                amount += Mathf.sin(sTime * 3.5f);
                amount += Mathf.cos(sTime * 4.67f);
                amount += Mathf.sin(sTime * 1.2f);
                amount *= 2f;

            setUniformf("u_offset", amount);
        }

        @Override
        public float timeScale(){
            return settings.getFloat("du-aberration-speed");
        }

        public enum AberrationType{
            RGB, CMY, RYGCB;

            public final static AberrationType[] all = values();
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

    public static class BlurShader extends DrunkShader{
        public BlurShader(){
            super("blur");
        }

        @Override
        public void apply(){
            float speed = settings.getFloat("du-blur-speed");
            float t = Time.time / 60f * Mathf.PI / 8f * speed;
            float s = Mathf.sin(t, 1.2f, 1f) +
                Mathf.sin(t, 1.5f, 1f) +
                -Mathf.sin(t, 0.2f, 1f) +
                Mathf.sin(t, 0.7f, 1f) +
                -Mathf.cos(t, 2f, 1f);
            s /= 5f;
            s *= 4f * settings.getFloat("du-blur-mag");

            setUniformf("u_resolution", w, h);
            setUniformf("u_radius", Math.abs(s));
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

            w = camera.width;
            h = camera.height;
            x = camera.position.x - w / 2;
            y = camera.position.y - h / 2f;

            buffer.blit(this);
        }

        @Override
        public void apply(){
            texture.bind(0);
        }
    }

    public static class PassThroughShader extends Shader{
        public PassThroughShader(){
            super(getShaderFi("screenspace.vert"), getShaderFi("passThrough.frag"));
        }
    }

    public static Fi getShaderFi(String file){
        return tree.get("shaders/" + file);
    }
}
