#define HIGHP

#define STEP 0.2

uniform sampler2D u_texture;

uniform vec2 u_resolution;
uniform float u_radius;

varying vec2 v_texCoords;

void main() {
    vec2 c = v_texCoords.xy;

    if(u_radius < 0.01f){
        gl_Fragcolor = texture2D(u_texture, v_texCoords);
        return;
    }

    vec4 col = vec4(0.0);
    int total = 0;

    float radSqr = u_radius * u_radius;
    for(float dx = -u_radius; dx <= u_radius; dx += STEP){
        for(float dy = -u_radius; dy <= u_radius; dy += STEP){
            if(dx * dx + dy * dy <= radSqr){
                col += texture2D(u_texture, c + (vec2(dx, dy) / u_resolution));
                total++;
            }
        }
    }

    gl_FragColor = col / total;
}
