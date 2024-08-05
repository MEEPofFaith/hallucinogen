#define HIGHP

uniform sampler2D u_texture;

uniform vec2 u_resolution;
uniform float u_radius;

varying vec2 v_texCoords;

void main() {
    vec2 c = v_texCoords.xy;

    vec4 col = vec4(0.0);
    int total = 0;

    int iRad = int(u_radius); //TODO find some way to use pixels instead of world units
    float radSqr = u_radius * u_radius;
    for(int dx = -iRad; dx <= iRad; dx++){
        for(int dy = -iRad; dy <= iRad; dy++){
            if(dx * dx + dy * dy <= radSqr){
                col += texture2D(u_texture, c + (vec2(dx, dy) / u_resolution));
                total++;
            }
        }
    }

    gl_FragColor = col / total;
}
