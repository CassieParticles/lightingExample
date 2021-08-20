package rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.CallbackI;
import utils.Timer;

import java.util.Vector;

public class GameObject3D extends Mesh{

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private Vector3f colour;

    public GameObject3D(float[] vertices, int[] indices, Vector3f colour) {
        super(vertices, indices);

        this.position=new Vector3f();
        this.rotation=new Vector3f();
        this.scale=new Vector3f(1,1,1);
        this.colour=colour;
    }

    public void setPosition(Vector3f position){
        this.position=new Vector3f(position);
    }

    public void setRotation(Vector3f rotation){
        this.rotation=new Vector3f(rotation);
    }

    public void setScale(Vector3f scale){
        this.scale=new Vector3f(scale);
    }

    public void translate(Vector3f translation){
        this.position.add(translation);
    }

    public void rotate(Vector3f rotation){
        this.rotation.add(rotation);
    }

    public void resize(Vector3f scale){
        this.scale.add(scale);
    }

    public Vector3f getPosition(){
        return new Vector3f(position);
    }

    public Vector3f getRotation(){
        return new Vector3f(rotation);
    }

    public Vector3f getScale(){
        return new Vector3f(scale);
    }


    private Matrix4f getTransformationMatrix(){
        return new Matrix4f().identity().translate(position).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).scale(scale);
    }

    public void render(Program program, Camera camera, Timer timer){
        program.useProgram();

        GL46.glBindVertexArray(getVaoId());
        GL46.glEnableVertexAttribArray(0);

        program.setUniform("Colour",colour);
        program.setUniform("projectionMatrix",camera.getProjectionMatrix());
        program.setUniform("viewMatrix",camera.getViewMatrix());
        program.setUniform("transformationMatrix", getTransformationMatrix());
        program.setUniform("cameraPos",camera.getPosition());
        program.setUniform("cameraFacing",new Vector3f(1,0,0));
        program.setUniform("bgColour",new Vector3f(0.1f, 0.1f, 0.2f));

        GL46.glDrawElements(GL46.GL_TRIANGLES, getVertexCount(), GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);
        GL46.glBindVertexArray(0);

        program.detachProgram();
    }
}
