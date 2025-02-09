#version 150
in vec4 color;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

vec2 trapezoid(vec2 i, vec2 c) {
    return min((0.5 - abs(i - 0.5)) * c, 1.0);
}

void main() {
    vec2 edgedValue = 1.0 - trapezoid(texCoord2.xy, texCoord0.xy);
    edgedValue = edgedValue * edgedValue * 0.5;

    fragColor = vec4(vec3(edgedValue.x + edgedValue.y) * color.rgb, 1.0);
}