#define HIGHP

uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;
uniform float u_scl;

varying vec2 v_texCoords;

void main() {
    vec2 c = v_texCoords.xy;

    float sTime = u_time / 60.0;
    float amount = sin(sTime * 2.1);
    amount += sin(sTime * 3.5);
    amount += cos(sTime * 4.67);
    amount += sin(sTime * 1.2);
    amount *= 2.0 * u_scl;
    amount /= u_resolution.x;

    vec3 col = vec3(
        texture2D(u_texture, vec2(c.x + amount, c.y)).r,
        texture2D(u_texture, c).g,
        texture2D(u_texture, vec2(c.x - amount, c.y)).b
    );

    gl_FragColor = vec4(col, 1.0);
}
