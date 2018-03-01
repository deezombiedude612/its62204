package howblankisit.example.administrator.mainpage;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import how.blank.isit.R;

/**
 * The Bright activity page
 * **/

public class bright extends AppCompatActivity implements SensorEventListener {
    /*Initialize the variables that will be used in the program*/
    TextView tv;
    SensorManager sensorManager;
    Sensor sensor;

    /*Oncreate method*/
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bright);

        /*find the text view from the xml page*/
        tv = (TextView) findViewById(R.id.tvb);

        /*give the values to the sensor and sensor manager */
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        onButtonClick();
    }

    /*onPause method*/
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /*onResume method*/
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    /*onSensorChanged method*/
    public final void onSensorChanged(SensorEvent event) {

       if (event.sensor.getType()== Sensor.TYPE_LIGHT){
           /*Set the value into the TextView tv*/
               tv.setText(""+event.values[0]);

       }
       /*change the background color when the range of the brightness changed*/
        View view = this.getWindow().getDecorView();
        if(event.values[0]<=100){
            view.setBackgroundColor(Color.LTGRAY);
        }else if (event.values[0]<=800 && event.values[0]>100){
            view.setBackgroundColor(Color.rgb(151,156,66));
        } else if (event.values[0]<=1200 && event.values[0]>800){
            view.setBackgroundColor(Color.rgb(190, 230, 66));
        }else if (event.values[0]<=2000 && event.values[0]>1200){
            view.setBackgroundColor(Color.YELLOW);
        }else if (event.values[0]<=3000 && event.values[0]>2000){
            view.setBackgroundColor(Color.rgb(255, 128, 0));
        }else if (event.values[0]<=4000 && event.values[0]>2000){
            view.setBackgroundColor(Color.rgb(255,71,18));
        }else if (event.values[0]<120000 && event.values[0]>4000){
            view.setBackgroundColor(Color.rgb(255, 0, 0));
        }


    }

    /*onButtonClick method*/
    public void onButtonClick(){
        Button bt = (Button)findViewById(R.id.bt5);
        /*leading user to the main page while they click on the Button bt*/
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(bright.this, MainActivity.class));
            }
        });
    }
}
