#version 150

uniform sampler2D CameraTexture;  // Texture containing the virtual camera's view

in vec2 texCoord;

out vec4 fragColor;

void main() {
    // Sample from the virtual camera's view texture
    vec4 cameraColor = texture(CameraTexture, texCoord);

    // You can perform additional manipulations or effects here if needed

    // Output the final color
    fragColor = cameraColor;
}