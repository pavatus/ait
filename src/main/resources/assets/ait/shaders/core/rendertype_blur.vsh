#version 150

const float borderSize = 16.0;

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

out vec4 color;
out vec2 texCoord0;
out vec2 texCoord2;

uniform mat4 ModelViewMat, ProjMat;

void main() {
    color = Color;
    texCoord0 = UV0 * borderSize;
    texCoord2 = vec2(UV2) * 0.0625;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}