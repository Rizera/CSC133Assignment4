package csc133;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import csc133.spot;

import static csc133.spot.WIN_HEIGHT;
import static csc133.spot.WIN_WIDTH;

public class slCamera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private float f_left ;
    private float f_right;
    private float f_bottom;
    private float f_top;
    private float f_near;
    private float f_far;
    public Vector3f defaultLookFrom = new Vector3f(0f, 0f, 0f);
    public Vector3f defaultLookAt = new Vector3f(0f,0f,-1.0f);
    public Vector3f defaultUpVector = new Vector3f(0f, 1.0f, 0f);
    private Vector3f curLookFrom;
    private Vector3f curLookAt;
    private Vector3f curUpVector;
    private void setCamera() { //this needs to set up the viewport...
        projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        viewMatrix = new Matrix4f();
        viewMatrix.identity();
    }

    public slCamera(Vector3f camera_position) {
        defaultLookFrom = camera_position;
        f_left = 0;
        f_right = WIN_WIDTH;
        f_bottom = 0;
        f_top = WIN_HEIGHT;
        f_near = 0;
        f_far = 10;
    }
    public slCamera() {
        f_left = 0;
        f_right = WIN_WIDTH;
        f_bottom = 0;
        f_top = WIN_HEIGHT;
        f_near = 0;
        f_far = 10;
    }

    public void setProjectionOrtho() {
        projectionMatrix.identity();
        projectionMatrix.setOrtho(f_left, f_right, f_bottom, f_top, f_near, f_far);
    }

    public void setProjectionOrtho( float thisLeft, float thisRight, float thisBottom,
                                    float thisTop, float thisNear, float thisFar) {
        projectionMatrix.identity();
        f_left = thisLeft;
        f_right = thisRight;
        f_bottom = thisBottom;
        f_top = thisTop;
        f_near = thisNear;
        f_far = thisFar;
        setProjectionOrtho();
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity();
        return viewMatrix.lookAt(curLookFrom, curLookAt.add(defaultLookFrom), curUpVector);
    }

    public Matrix4f getProjectionMatrix() {

        return projectionMatrix;
    }
}
