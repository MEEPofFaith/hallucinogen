package drunkustry.ui;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import drunkustry.graphics.*;
import mindustry.content.*;
import mindustry.graphics.*;

import static arc.Core.*;
import static drunkustry.graphics.DrunkShaders.*;
import static mindustry.Vars.*;

public class DrunkMenuRenderer extends MenuRenderer{
    private static final float darkness = 0.3f;
    private static final Color routerGray = Color.valueOf("6e7080");
    private final int width = !mobile ? 100 : 60, height = !mobile ? 50 : 40;
    private final Rand drawRand = new Rand();
    private final int routers;
    private final float trailTicks = 8f;
    private long randSeed;

    public DrunkMenuRenderer(){
        super();

        //Use Java Math.random because Mathf seems to have the same seed every time when reaching this point.
        randSeed = (long)(Math.random() * 100000);
        drawRand.setSeed(randSeed);
        routers = drawRand.random(1f) >= 0.75f ? 35
            : drawRand.random(1f) >= 0.75f ? 90
            : drawRand.random(1f) >= 0.75f ? 150
            : 250;
        randSeed = drawRand.seed0;
    }

    @Override
    public void render(){
        DrunkRendering.drawBegin();
        renderAll();
        DrunkRendering.drawEnd();
    }

    public void renderAll(){
        Reflect.set(MenuRenderer.class, this, "time", (float)getMenuRendererField("time") + Time.delta);
        Camera camera = (Camera)getMenuRendererField("camera");
        Mat mat = (Mat)getMenuRendererField("mat");
        CacheBatch batch = (CacheBatch)getMenuRendererField("batch");
        int cacheFloor = (int)getMenuRendererField("cacheFloor");
        int cacheWall = (int)getMenuRendererField("cacheWall");
        FrameBuffer shadows = (FrameBuffer)getMenuRendererField("shadows");

        float scaling = Math.max(Scl.scl(4f), Math.max(Core.graphics.getWidth() / ((width - 1f) * tilesize), Core.graphics.getHeight() / ((height - 1f) * tilesize)));
        camera.position.set(width * tilesize / 2f, height * tilesize / 2f);
        camera.resize(Core.graphics.getWidth() / scaling,
            Core.graphics.getHeight() / scaling);

        mat.set(Draw.proj());
        Draw.flush();
        Draw.proj(camera);
        batch.setProjection(camera.mat);
        batch.beginDraw();
        batch.drawCache(cacheFloor);
        batch.endDraw();
        Draw.color();
        Draw.rect(Draw.wrap(shadows.getTexture()),
            width * tilesize / 2f - 4f, height * tilesize / 2f - 4f,
            width * tilesize, -height * tilesize);
        Draw.flush();
        batch.beginDraw();
        batch.drawCache(cacheWall);
        batch.endDraw();

        drawRouters();

        Draw.proj(mat);
        Draw.color(0f, 0f, 0f, darkness);
        Fill.crect(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
        Draw.color();
    }

    protected void drawRouters(){
        Draw.color(routerGray);

        routers((x, y, dir, speed, rot) -> {
            float sX = Angles.trnsx(dir + 90f, 3f),
                sY = Angles.trnsy(dir + 90f, 3f),
                pX = Angles.trnsx(dir + 180f, speed * trailTicks),
                pY = Angles.trnsy(dir + 180f, speed * trailTicks);

            Fill.tri(
                x + sX, y + sY,
                x + pX, y + pY,
                x - sX, y - sY
            );
        });

        Draw.color();

        TextureRegion icon = Blocks.router.fullIcon;
        routers((x, y, dir, speed, rot) -> Draw.rect(icon, x, y, rot));
    }

    protected void routers(RouterDraw cons){
        float tw = width * tilesize * 1f + tilesize;
        float th = height * tilesize * 1f + tilesize;
        float time = (float)getMenuRendererField("time");

        float minOff = 50f, maxOff = 500f;
        float minMoveSpeed = 1f, maxMoveSpeed = 8f;
        float minSpinSpeed = 6f, maxSpinSpeed = 60f;
        float range = 500f;

        drawRand.setSeed(randSeed);
        for(int i = 0; i < routers; i++){
            float dir = drawRand.random(360f);
            float speed = drawRand.random(minMoveSpeed, maxMoveSpeed);
            Tmp.v1.trns(dir, time * speed);

            float wOff = drawRand.random(minOff + Math.abs(Angles.trnsx(dir, trailTicks * speed)), maxOff);
            float hOff = drawRand.random(minOff + Math.abs(Angles.trnsy(dir, trailTicks * speed)), maxOff);

            cons.draw(
                Mathf.mod(drawRand.random(range) + Tmp.v1.x, tw + wOff) - wOff / 2f,
                Mathf.mod(drawRand.random(range) + Tmp.v1.y, th + hOff) - hOff / 2f,
                dir,
                speed,
                time * drawRand.random(minSpinSpeed, maxSpinSpeed) * Mathf.sign(drawRand.range(1f))
            );
        }
    }

    private Object getMenuRendererField(String field){
        return Reflect.get(MenuRenderer.class, this, field);
    }

    static interface RouterDraw{
        void draw(float x, float y, float dir, float speed, float rot);
    }
}
