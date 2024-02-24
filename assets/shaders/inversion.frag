#define HIGHP

uniform sampler2D u_texture;

uniform float u_lerp;

varying vec2 v_texCoords;

void main() {
    vec4 c = texture2D(u_texture, v_texCoords.xy);
    vec4 inv = vec4(1.0 - c.r, 1.0 - c.g, 1.0 - c.b, c.a);
    
    gl_FragColor = mix(c, inv, u_lerp);
}