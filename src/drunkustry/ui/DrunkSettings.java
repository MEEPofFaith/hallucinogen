package drunkustry.ui;

import arc.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import drunkustry.graphics.DrunkShaders.AberrationShader.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class DrunkSettings{
    private static final FloatStringProc percent = s -> Strings.autoFixed(s * 100, 2) + "%";
    private static final FloatStringProc mult = s -> Strings.autoFixed(s, 2) + "x";

    public static void init(){
        ui.settings.addCategory(bundle.get("setting.drunk-title"), "hallucinogen-settings-icon", t -> {
            t.pref(new Separator("@settings.sound"));
            t.checkPref("du-pitch", true);
            t.pref(new FloatSliderSetting("du-drunk-scl", 1f, 0.1f, 5f, 0.01f, mult));
            t.pref(new FloatSliderSetting("du-drunk-mag", 1f, 0.1f, 2f, 0.01f, percent));
            t.checkPref("du-flanger", true);
            t.pref(new Separator("@settings.graphics"));
            t.checkPref("du-menu-background", true);
            t.checkPref("du-aberration", true);
            t.pref(new FloatSliderSetting("du-aberration-speed", 1f, 0f, 5f, 0.01f, mult));
            t.pref(new FloatSliderSetting("du-aberration-amount", 1f, 0f, 5f, 0.01f, percent));
            t.checkPref("du-aberration-rotation", false);
            t.pref(new FloatSliderSetting("du-aberration-rotation-speed", 1f, 0.1f, 10f, 0.01f, percent));
            t.sliderPref("du-aberration-mode", 0, 0, AberrationType.all.length - 1, s -> AberrationType.all[s].name());
            t.checkPref("du-color", true);
            t.pref(new FloatSliderSetting("du-color-speed", 1f, 0f, 5f, 0.01f, mult));
            t.pref(new FloatSliderSetting("du-color-alpha", 0.15f, 0f, 1f, 0.01f, percent));
            t.checkPref("du-distortion", true);
            t.pref(new FloatSliderSetting("du-distortion-speed", 1f, 0f, 5f, 0.01f, mult));
            t.pref(new FloatSliderSetting("du-distortion-amount", 1f, 0f, 5f, 0.01f, percent));
            t.pref(new FloatSliderSetting("du-distortion-insanity", 1f, 0f, 5f, 0.01f, percent));
            t.checkPref("du-blur", true);
            t.pref(new FloatSliderSetting("du-blur-speed", 1f, 0f, 5f, 0.01f, mult));
            t.pref(new FloatSliderSetting("du-blur-mag", 1f, 0.1f, 5f, 0.01f, percent));
            t.checkPref("du-inversion", false);
            t.pref(new FloatSliderSetting("du-inversion-freq", 1f, 0f, 5f, 0.01f, mult));
        });
    }

    private static class Separator extends Setting{
        float height;

        public Separator(String name){
            super(name);
        }

        public Separator(float height){
            this("");
            this.height = height;
        }

        @Override
        public void add(SettingsTable table){
            if(name.isEmpty()){
                table.image(Tex.clear).height(height).padTop(3f);
            }else{
                table.table(t -> {
                    t.add(title).padTop(3f);
                }).get().background(Tex.underline);
            }
            table.row();
        }
    }

    private static class FloatSliderSetting extends Setting{
        float def, min, max, step;
        FloatStringProc sp;

        public FloatSliderSetting(String name, float def, float min, float max, float step, FloatStringProc s){
            super(name);
            this.def = def;
            this.min = min;
            this.max = max;
            this.step = step;
            this.sp = s;

            settings.defaults(name, def);
        }

        @Override
        public void add(SettingsTable table){
            Slider slider = new Slider(min, max, step, false);

            slider.setValue(settings.getFloat(name));

            Label value = new Label("", Styles.outlineLabel);
            Table content = new Table();
            content.add(title, Styles.outlineLabel).left().growX().wrap();
            content.add(value).padLeft(10f).right();
            content.margin(3f, 33f, 3f, 33f);
            content.touchable = Touchable.disabled;

            slider.changed(() -> {
                settings.put(name, slider.getValue());
                value.setText(sp.get(slider.getValue()));
            });

            slider.change();

            addDesc(table.stack(slider, content).width(Math.min(Core.graphics.getWidth() / 1.2f, 460f)).left().padTop(4f).get());
            table.row();
        }
    }

    public interface FloatStringProc{
        String get(float f);
    }
}
