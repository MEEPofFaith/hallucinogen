#define HIGHP

#define NSCALE 400.0 / 2.0

uniform sampler2D u_texture;
uniform sampler2D u_noise;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.x + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main(){
    vec2 c = v_texCoords.xy;
    vec2 coords = (c * u_resolution) + u_campos;

    float btime = u_time / 3000.0;
    float noise = (texture2D(u_noise, (coords) / NSCALE + vec2(btime) * vec2(-0.9, 0.8)).r +
                    texture2D(u_noise, (coords) / NSCALE + vec2(btime * 1.1) * vec2(0.5, 0.2)).r +
                    texture2D(u_noise, (coords) / NSCALE + vec2(btime * 0.9) * vec2(-0.1, -0.6)).r +
                    texture2D(u_noise, (coords) / NSCALE + vec2(btime * 1.2) * vec2(0.3, -1.0)).r);
    vec3 hue = hsv2rgb(vec3(noise, 1.0, 1.0));

    gl_FragColor = vec4(hue, 0.25);
}
