#version 450

in vec3 fragmentPosition;
in vec3 normal;

uniform vec3 Colour;
uniform vec3 cameraPos;
uniform vec3 bgColour;
uniform vec3 cameraFacing;

out vec4 FragColour;

float calcLighting(){
    vec3 dirToCam=normalize(cameraPos-fragmentPosition);
    float diffuseLighting=dot(dirToCam,normal);
    return clamp(diffuseLighting,0,1);
}

void main() {
    FragColour=vec4(calcLighting()*Colour,1);
}
