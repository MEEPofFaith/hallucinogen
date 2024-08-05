#define HIGHP

uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;
uniform float u_scl;
uniform float u_dir;
uniform float u_offset;
uniform int u_mode;

varying vec2 v_texCoords;

//RGB <-> CMYK from https://gist.github.com/mattdesl/e40d3189717333293813626cbdb2c1d1
vec3 CMYKtoRGB(vec4 cmyk){
    float c = cmyk.x;
    float m = cmyk.y;
    float y = cmyk.z;
    float k = cmyk.w;

    float invK = 1.0 - k;
    float r = 1.0 - min(1.0, c * invK + k);
    float g = 1.0 - min(1.0, m * invK + k);
    float b = 1.0 - min(1.0, y * invK + k);
    return clamp(vec3(r, g, b), 0.0, 1.0);
}

vec4 RGBtoCMYK(vec3 rgb){
    float r = rgb.r;
    float g = rgb.g;
    float b = rgb.b;
    float k = min(1.0 - r, min(1.0 - g, 1.0 - b));
    vec3 cmy = vec3(0.0);
    float invK = 1.0 - k;
    if (invK != 0.0) {
        cmy.x = (1.0 - r - k) / invK;
        cmy.y = (1.0 - g - k) / invK;
        cmy.z = (1.0 - b - k) / invK;
    }
    return clamp(vec4(cmy, k), 0.0, 1.0);
}

vec2 getOffset(vec2 pos, vec2 offset){
    return pos + vec2(cos(u_dir) * offset.x, sin(u_dir) * offset.y);
}

vec3 getCol(vec2 cPos, vec2 offset){
    vec2 pos1 = getOffset(cPos, offset);
    vec2 pos2 = getOffset(cPos, -offset);

    switch(u_mode){
        case 0: //rgb
            return vec3(
                texture2D(u_texture, pos1).r,
                texture2D(u_texture, cPos).g,
                texture2D(u_texture, pos2).b
            );
        case 1: //cmyk
            vec4 cmyk1 = RGBtoCMYK(texture2D(u_texture, pos1));
            vec4 cmyk2 = RGBtoCMYK(texture2D(u_texture, cPos));
            vec4 cmyk3 = RGBtoCMYK(texture2D(u_texture, pos2));

            vec4 cmykOut = vec4(cmyk1.x, cmyk2.y, cmyk3.z, cmyk2.w);

            return CMYKtoRGB(cmykOut);
        case 2: //rygcb
            vec2 pos3 = getOffset(cPos, 0.5 * offset);
            vec2 pos4 = getOffset(cPos, 0.5 * -offset);

            vec4 rgb1 = texture2D(u_texture, pos1);
            vec4 rgb2 = texture2D(u_texture, pos3);
            vec4 rgb3 = texture2D(u_texture, cPos);
            vec4 rgb4 = texture2D(u_texture, pos4);
            vec4 rgb5 = texture2D(u_texture, pos2);

            return vec3((rgb1.r + rgb2.r) / 2.0, ((rgb2.g + rgb4.g) / 2.0 + rgb3.g) / 2.0, (rgb4.b + rgb5.b) / 2.0);
    }

    return vec3(1.0);
}

void main() {
    vec2 c = v_texCoords.xy;

    vec2 offset = vec2(u_offset) * u_scl / u_resolution;

    gl_FragColor = vec4(getCol(c, offset), 1.0);
}
