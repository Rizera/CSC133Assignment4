package csc133;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import static csc133.spot.*;

public class Main {
    GLFWErrorCallback errorCallback;
    GLFWKeyCallback keyCallback;
    GLFWFramebufferSizeCallback fbCallback;
    static long ogl_window;
    static int WIN_WIDTH = 900, WIN_HEIGHT = 900;
    int WIN_POS_X = 30, WIN_POX_Y = 90;
    private static final int OGL_MATRIX_SIZE = 16;
    // call glCreateProgram() here - we have no gl-context here
    int shader_program;
    Matrix4f viewProjMatrix = new Matrix4f();
    FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
    int vpMatLocation = 0, renderColorLocation = 0;

    public static void main(String[] args) {
        ogl_window = slWindow.getOgl_Window(WIN_WIDTH, WIN_HEIGHT);
        System.out.println("We are inside main");
        Main run_this = new Main();
        run_this.render();
    } // public static void main(String[] args)

    void render() {
        try {
            renderLoop();
            glfwDestroyWindow(ogl_window);
            keyCallback.free();
            fbCallback.free();
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    } // void render()

    void renderLoop() {
        glfwPollEvents();
        initOpenGL();
        renderObjects();
        /* Process window messages in the main thread */
        while (!glfwWindowShouldClose(ogl_window)) {
            glfwWaitEvents();
        }
    } // void renderLoop()

    void initOpenGL() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(0, 0, WIN_WIDTH, WIN_HEIGHT);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        this.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(0.0f, 0.2f, 0.2f, 2.0f);" +
                        "}");
        glCompileShader(fs);
        glAttachShader(shader_program, fs);
        glLinkProgram(shader_program);
        glUseProgram(shader_program);
        vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");
        return;
    } // void initOpenGL()

    void renderObjects() {
        float length = 75.0f;
        float offset = 20.0f;
        float padding = 3.0f;
        float xMin = offset;
        float xMax = xMin + length;
        float yMax = WIN_HEIGHT - offset;
        float yMin = yMax - length;
        int MAX_ROWS = 7;
        int MAX_COLUMNS = 5;
        int ips = 6, vps = 4, cpe = 2;
        float[] vertices = new float[MAX_ROWS * MAX_COLUMNS * vps * cpe];
        int[] indices = new int[MAX_ROWS * MAX_COLUMNS * ips];
        int index = 0;
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLUMNS; col++) { // fill the vertices array...
                vertices[index++] = xMin;
                vertices[index++] = yMin;
                vertices[index++] = xMax;
                vertices[index++] = yMin;
                vertices[index++] = xMax;
                vertices[index++] = yMax;
                vertices[index++] = xMin;
                vertices[index++] = yMax;
                xMin = xMax + padding;
                xMax = xMin + length;
            }
            xMin = offset;
            xMax = xMin + length;
            yMax = yMin - padding;
            yMin = yMax - length;
        }

        int vIndex = 0, myI = 0;
        while (myI < indices.length) { // fill the indices array...
            indices[myI++] = vIndex;
            indices[myI++] = vIndex + 1;
            indices[myI++] = vIndex + 2;
            indices[myI++] = vIndex;
            indices[myI++] = vIndex + 2;
            indices[myI++] = vIndex + 3;
            vIndex += vps;
        }

        //The while loop below SHOULD be using the vertices and indices to draw all the squares...
        while (!glfwWindowShouldClose(ogl_window)) {
            glfwPollEvents(); //tells the window if there were any events when making the window... (will be used later)
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //Clear these buffers... (will be used later)

            int vbo = glGenBuffers();
            int ibo = glGenBuffers();

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.
                    createFloatBuffer(vertices.length).
                    put(vertices).flip(), GL_STATIC_DRAW);
            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.
                    createIntBuffer(indices.length).
                    put(indices).flip(), GL_STATIC_DRAW);
            glVertexPointer(cpe, GL_FLOAT, 0, 0L);

            //use slCamera to setOrtho here, something must be wrong in my code...
            //does this mean... progress?

            /*
            slCamera my_cam = new slCamera();
            my_cam.setProjectionOrtho();
            viewProjMatrix = my_cam.getProjectionMatrix();
            */
            viewProjMatrix.setOrtho(0f, WIN_WIDTH, 0f, WIN_HEIGHT, 0, 10);

            glUniformMatrix4fv(vpMatLocation, false,
                    viewProjMatrix.get(myFloatBuffer));

            float myRed = 0.0f, myGreen = 0.0f, myBlue = 0.0f;
            glUniform3f(renderColorLocation, myRed, myGreen, myBlue);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            int VTD = indices.length; // need to process 6 Vertices To Draw 2 triangles, change to how many verticies you use...
            glDrawElements(GL_TRIANGLES, VTD, GL_UNSIGNED_INT, 0L);
            //the line above (glDrawElements) tells the gpu to draw the triangles...
            glfwSwapBuffers(ogl_window);
        }
    }
}