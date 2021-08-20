package main;

import org.joml.Vector3f;
import rendering.*;
import utils.FileHandling;
import utils.Input;
import utils.Timer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

public class Main {

    private Window window;
    private Timer timer;
    private Input input;
    private Camera camera;

    private Program renderProgram;
    private GameObject3D cube;
    private GameObject3D[] objects;

    public static void main(String[] args){
        new Main().gameLoop();
    }

    public void gameLoop(){
        try{
            init();
            loop();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            cleanup();
        }
    }

    public void init() throws Exception{
        window=new Window(600,600,"Title");
        timer=new Timer(60,60);
        input=new Input();
        camera=new Camera();

        window.init();
        input.init(window);
        camera.calculateProjectionMatrix((float)Math.toRadians(60),0.1f,1000f, window.getAspectRatio());

        renderProgram=new Program();
        renderProgram.attachShaders(new Shader[]{
                new Shader(FileHandling.loadResource("src/shaders/rendering/vertex.glsl"),GL46.GL_VERTEX_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/rendering/geometry.glsl"),GL46.GL_GEOMETRY_SHADER),
                new Shader(FileHandling.loadResource("src/shaders/rendering/fragment.glsl"),GL46.GL_FRAGMENT_SHADER)
        });

        renderProgram.link();
        renderProgram.createUniform("projectionMatrix");
        renderProgram.createUniform("viewMatrix");
        renderProgram.createUniform("transformationMatrix");
        renderProgram.createUniform("Colour");
        renderProgram.createUniform("cameraPos");
        renderProgram.createUniform("bgColour");
        renderProgram.createUniform("cameraFacing");

        float[] vertices=new float[]{
                -1,-1,-1,
                1,-1,-1,
                1,-1,1,
                -1,-1,1,
                -1,1,-1,
                1,1,-1,
                1,1,1,
                -1,1,1
        };

        int[] indices=new int[]{
                0,2,1, 0,3,2,
                0,4,7, 0,7,3,
                0,5,4, 0,1,5,
                1,6,5, 1,2,6,
                3,7,6, 3,6,2,
                4,5,6, 4,6,7
        };

        cube=new GameObject3D(vertices,indices,new Vector3f(1,0.2f,0.2f));
        cube.setPosition(new Vector3f(3,2,-5));

        GL46.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        window.loop();
    }

    private void render(){
        window.loop();
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

        cube.render(renderProgram,camera,timer);
    }

    private void update(){
        if(input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)){
            window.close();
        }
//        cube.rotate(new Vector3f(1,1,1));
        camera.control(input,timer,10.0f,180);
    }

    public void loop(){
        while(!window.shouldClose()){
            timer.update();
            if(timer.getFrame()){
                render();
            }if(timer.getUpdate()){
                update();
            }
        }
    }

    public void cleanup(){
        System.out.println("Cleaning up");

        window.cleanup();
        renderProgram.cleanup();
        cube.cleanup();
    }

}
