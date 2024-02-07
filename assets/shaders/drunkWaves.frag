#define HIGHP

#define PI 3.1415926535897932384626433832795
#define NSCALE 600.0 / 2.0

uniform sampler2D u_texture;
uniform sampler2D u_noise;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

void main() {
    vec2 c = v_texCoords.xy;
    vec2 coords = (c * u_resolution) + u_campos;

    float wTime = u_time / 3000.0;
    vec2 sNoise = (vec2(
        texture2D(u_noise, (coords) / NSCALE + vec2(wTime) * vec2(-0.4, 1.2)).r +
            texture2D(u_noise, (coords) / NSCALE + vec2(wTime * 1.3) * vec2(0.9, -0.7)).r,
        texture2D(u_noise, (coords) / NSCALE + vec2(wTime) * vec2(0.7, 0.3)).r +
            texture2D(u_noise, (coords) / NSCALE + vec2(wTime * 1.2) * vec2(-0.5, -0.4)).r
    ) - 1) * 2.0 * 2.0 * PI;
    float oNoise = (texture2D(u_noise, (coords) / NSCALE + vec2(wTime * 1.5) * vec2(-0.8, -0.7)).r +
        texture2D(u_noise, (coords) / NSCALE + vec2(wTime * 1.1) * vec2(1.3, 0.3)).r - 0.5) * 2.0;

    vec2 off = vec2(sin(coords.y / 5.0 + sNoise.x), sin(coords.x / 5.0 + sNoise.y)) / u_resolution * 2.0 * oNoise;
    c += off;

    gl_FragColor = texture2D(u_texture, c);
}
