package drunkustry.ui;

import arc.util.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class DrunkSettings{
    public static void init(){
        ui.settings.addCategory(bundle.get("setting.drunk-title"), "hallucinogen-settings-icon", t -> {
            t.pref(new Separator("du-sounds"));
            t.checkPref("du-pitch", true);
            t.sliderPref("du-drunk-mag", 10, 1, 20, 1, s -> (s * 10) + "%");
            t.sliderPref("du-drunk-scl", 10, 1, 50, 1, s -> Strings.autoFixed(s / 10f, 2) + "x");
            t.checkPref("du-flanger", true);
            t.pref(new Separator("du-graphics"));
            t.checkPref("du-aberration", true);
            t.checkPref("du-color", true);
            t.checkPref("du-distortion", true);
        });
    }

    public static class Separator extends Setting{
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
}
