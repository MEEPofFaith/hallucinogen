#define HIGHP

#define PI 3.1415926535897932384626433832795

uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

void main() {
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_resolution) + u_campos;
    float btime = u_time / 20.0;
    T += vec2(sin(coords.y / 5.0 + btime), sin(coords.x / 5.0 + btime)) / u_resolution * 2;

    //float btime = u_time / 50.0;
    //vec2 pos = v_texCoords + u_campos / u_resolution;
    //vec2 c = vec2(pos.x + sin(btime + pos.y * 2.0 * PI) * mag, pos.y);

    gl_FragColor = texture2D(u_texture, T);
}
