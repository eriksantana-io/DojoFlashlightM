package com.lastdojodev.dojoflashlightm;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private CameraCaptureSession mSession;
    private CameraDevice mCameraDevice;
    private CameraManager mCameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize camera - check for flash availability
        try
        {
            initializeCamera();
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }

        //Turn on flashlight for the first time
        Switch flashSwitch = (Switch) findViewById(R.id.switch1);
        flashSwitch.setChecked(true);
        flashSwitch.setOnCheckedChangeListener(new onSwitchClick());
        try
        {
            mCameraManager.setTorchMode("0", true);
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    private void initializeCamera() throws CameraAccessException
    {
        mCameraManager = (CameraManager) MainActivity.this.getSystemService(Context.CAMERA_SERVICE);

        CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics("0");

        boolean flashAvailable = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

        // Check flash availability
        if (!flashAvailable)
        {
            Toast.makeText(MainActivity.this, "Flash not available", Toast.LENGTH_SHORT).show();
            //todo: throw Exception
        }
    }

    class onSwitchClick implements CompoundButton.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            try
            {
                if (isChecked)
                {
                    //Turn on flashlight
                    mCameraManager.setTorchMode("0", true);
                }
                else
                {
                    //Turn off flashlight
                    mCameraManager.setTorchMode("0", false);
                }
            }
            catch (CameraAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void close()
    {
        if (mCameraDevice == null || mSession == null)
        {
            return;
        }
        mSession.close();
        mCameraDevice.close();
        mCameraDevice = null;
        mSession = null;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        close();
    }
}
