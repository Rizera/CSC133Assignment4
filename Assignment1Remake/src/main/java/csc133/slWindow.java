package csc133;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;


public class slWindow {
    private static GLFWErrorCallback errorCallback; //these need to be private
    private static long ogl_Window = -1;
    private static GLFWKeyCallback keyCallback;
    private static GLFWFramebufferSizeCallback fbCallback;
    private static int WIN_POS_X = 30, WIN_POX_Y = 90;
    private static int WIN_WIDTH = 900;
    private static int WIN_HEIGHT = 900;


    public slWindow(int win_width, int win_height) {
        System.out.println("Call to slWindow:: (width, height) == ("
                + win_width + ", " + win_height +") received!");
        WIN_WIDTH = win_width;
        WIN_HEIGHT = win_height;
    }

    private static long getOgl_Window() { //Delete this, and use slWindow object instead...
        if (ogl_Window != -1) {
            return ogl_Window;
        }
        glfwSetErrorCallback(errorCallback =
                GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 8);
        ogl_Window = glfwCreateWindow(WIN_WIDTH, WIN_HEIGHT, "CSC 133", NULL, NULL);
        if (ogl_Window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(ogl_Window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int
                    mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        });
        glfwSetFramebufferSizeCallback(ogl_Window, fbCallback = new
                GLFWFramebufferSizeCallback() {
                    @Override
                    public void invoke(long window, int w, int h) {
                        if (w > 0 && h > 0) {
                            WIN_WIDTH = w;
                            WIN_HEIGHT = h;
                        }
                    }
                });
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(ogl_Window, WIN_POS_X, WIN_POX_Y);
        glfwMakeContextCurrent(ogl_Window);
        int VSYNC_INTERVAL = 1;
        glfwSwapInterval(VSYNC_INTERVAL);
        glfwShowWindow(ogl_Window);
        return ogl_Window;
    }



    public static long getOgl_Window(int win_wd, int win_ht) {
        if (ogl_Window != -1)
        {
            return ogl_Window;
        }
        WIN_WIDTH = win_wd;
        WIN_HEIGHT = win_ht;
        return getOgl_Window();
    }
} // private void initGLFWindow()
