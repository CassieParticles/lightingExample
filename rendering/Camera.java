package rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import utils.Input;
import utils.Timer;

public class Camera {
    private Vector3f position;
    private Vector3f rotation;

    private Matrix4f projectionMatrtix;

    public Camera(Vector3f position, Vector3f rotation){
        this.position=position;
        this.rotation=rotation;
    }

    public Camera(Vector3f position){
        this(position,new Vector3f());
    }

    public Camera(){
        this(new Vector3f());
    }

    public void control(Input input, Timer timer, float moveSpeed, float rotateSpeed){
        float deltaUpdate=(float)timer.getDeltaUpdate();
        if(input.isKeyDown(GLFW.GLFW_KEY_W)){
            move(new Vector3f(0,0,-deltaUpdate*moveSpeed));
        }else if(input.isKeyDown(GLFW.GLFW_KEY_S)){
            move(new Vector3f(0,0,deltaUpdate*moveSpeed));
        }if(input.isKeyDown(GLFW.GLFW_KEY_A)){
            move(new Vector3f(-deltaUpdate*moveSpeed,0,0));
        }else if(input.isKeyDown(GLFW.GLFW_KEY_D)){
            move(new Vector3f(deltaUpdate*moveSpeed,0,0));
        }if(input.isKeyDown(GLFW.GLFW_KEY_Q)){
            translate(new Vector3f(0,deltaUpdate*moveSpeed,0));
        }else if(input.isKeyDown(GLFW.GLFW_KEY_E)){
            translate(new Vector3f(0,-deltaUpdate*moveSpeed,0));
        }
        if(input.isKeyDown(GLFW.GLFW_KEY_LEFT)){
            rotate(new Vector3f(0,-deltaUpdate*rotateSpeed,0));
        }else if(input.isKeyDown(GLFW.GLFW_KEY_RIGHT)){
            rotate(new Vector3f(0,deltaUpdate*rotateSpeed,0));
        }
        if(input.isKeyDown(GLFW.GLFW_KEY_UP)){
            rotate(new Vector3f(-deltaUpdate*rotateSpeed,0,0));
        }else if(input.isKeyDown(GLFW.GLFW_KEY_DOWN)){
            rotate(new Vector3f(deltaUpdate*rotateSpeed,0,0));
        }
    }

    public void setPos(Vector3f position){
        this.position.set(position);
    }

    public void translate(Vector3f translation){
        this.position.add(translation);
    }

    public void move(Vector3f translation){
        if ( translation.z != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * translation.z;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * translation.z;
        }
        if ( translation.x != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y-90)) * -1.0f * translation.x;
            position.z += (float)Math.cos(Math.toRadians(rotation.y-90)) * translation.x;
        }
        position.y += translation.y;
    }

    public void rotate(Vector3f rotation){
        this.rotation.add(rotation);
    }

    public Vector3f getPosition(){
        return new Vector3f(position);
    }

    public Vector3f getFacing(){
        double r=Math.cos(Math.toRadians(-Math.abs(rotation.y)));

        return new Vector3f((float)(Math.sin(Math.toRadians(rotation.x))*r),(float)Math.sin(Math.toRadians(rotation.y)),(float)(-Math.cos(Math.toRadians(rotation.x))*r));
    }

    public void calculateProjectionMatrix(float FOV, float zNear, float zFar, float aspectRatio){
        this.projectionMatrtix=new Matrix4f().perspective(FOV,aspectRatio, zNear, zFar);
    }

    public Matrix4f getViewMatrix(){
        return new Matrix4f().identity().
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).translate(new Vector3f(position).mul(-1));
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrtix;
    }


}
