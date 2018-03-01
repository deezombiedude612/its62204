package howblankisit.example.administrator.mainpage;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import how.blank.isit.R;

public class tall extends AppCompatActivity implements SensorEventListener {
    /*Call UI elements*/
    private TextView result;
    private Button button;

    /*Create Sensor*/
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    /*Sensor Event variables*/
    private long lastIn = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private long startTime;
    private long endTime;
    private double elaTime;
    private double lAccel;
    private double distance;
    String flag = "notyet";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tall);
        onButtonClick();
        result = (TextView) findViewById(R.id.res);
        button = (Button) findViewById(R.id.btns);

        /*Register accelerometer listener*/
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        /*Invoke calcHeight to calculate height*/
        calcHeight();
    }
    /*On Activity Creation - continue accelerometer listerner*/
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /*On Activity Pause - remove listener*/
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    /*Calculate height*/
    public void calcHeight() {
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Changes button text to 'CANCEL' when START is pushed*/
                        if (button.getText() == "START") {
                            result.setText("0.0");
                            button.setText("CANCEL");
                        }
                        /*Changes button text to 'START' when CANCEL or RESET is pushed*/
                        else {
                            result.setText("0.0");
                            button.setText("START");
                            startTime = endTime = 0;
                            elaTime = 0;
                            flag = "notyet";
                        }
                    }
                }
        );
    }

    /*OnSensorChanged invoked when listener detects changes in sensor readings*/
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        /*Runs only when START is pushed*/
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER && button.getText() == "CANCEL") {
            /*Accept accelerometer readings*/
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            /*Make sure readings are not recorded when small motions or sensor drift is affecting it initially*/
            long curTime = System.currentTimeMillis();
            if((curTime - lastIn) > 100) {
                long diffTime = (curTime - lastIn);
                lastIn = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/diffTime * 10000;

                /*Start recording when movement registered is higher that allowed*/
                if(speed > SHAKE_THRESHOLD && flag == "notyet") {
                    startTime = System.nanoTime();
                    flag = "started";
                }
                /*Continue reading only when it has started recording*/
                else if(flag == "started") {
                    double[] grav = {0, 0, 0};
                    double[] accel = {0, 0, 0};
                    final float alpha = 0.8f;

                    /*Low filter pass for eliminating gravitational acceleration from accelerometer readings to get linear acceleration*/
                    grav[0] = alpha * grav[0] + (1 - alpha) * x;
                    grav[1] = alpha * grav[1] + (1 - alpha) * y;
                    grav[2] = alpha * grav[2] + (1 - alpha) * z;
                    accel[0] = x - grav[0];
                    accel[1] = y - grav[1];
                    accel[2] = z - grav[2];
                    double tAccel = ((x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH))-1;

                    /*Calculates and display time when device decelerates as it hits a surface*/
                    if (tAccel < lAccel) {
                        endTime = System.nanoTime();
                        elaTime = ((double) endTime - (double) startTime) / 1000000000;

                        distance = 0.5 * 9.81 * Math.pow(elaTime, 2);

                        DecimalFormat df = new DecimalFormat("#.##");
                        String ans = df.format(distance);

                        result.setText(ans);
                        button.setText("RESET");
                    }
                    /*Register previous value*/
                    else {
                        lAccel = tAccel;
                    }
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onButtonClick(){
        Button bt = (Button)findViewById(R.id.bt3);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tall.this, MainActivity.class));
            }
        });
    }
}
