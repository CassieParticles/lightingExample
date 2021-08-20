#version 450

layout(location=0) in vec3 position;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

out vec3 transformedPos;

void main() {
    transformedPos=(transformationMatrix*vec4(position,1)).xyz;

    gl_Position=projectionMatrix*viewMatrix*vec4(transformedPos,1);
}
