package note.light.sun.sunlightnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Record extends AppCompatActivity implements SensorEventListener{
    TextView tv1, tv2, tv3;
    EditText et;
    SensorManager sensorManager;
    Sensor sensor;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tv1 = (TextView) findViewById(R.id.etlj1);
        tv2 = (TextView) findViewById(R.id.etlj2);
        tv3 = (TextView) findViewById(R.id.etlj3);
        et = (EditText) findViewById(R.id.etlj4);

        /*give the values to the sensor and sensor manager */
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        String cdt = DateFormat.getDateInstance().format(new Date());
        Calendar c = Calendar.getInstance();
        String time = Integer.toString(c.get(Calendar.HOUR_OF_DAY))+":"
                +Integer.toString(c.get(Calendar.MINUTE))+":"+Integer.toString(c.get(Calendar.SECOND));

        tv2.setText(cdt);
        tv3.setText (time);

        myDB = DatabaseHelper.getInstance(this);

        onButtonClick();
    }

    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /*onResume method*/
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onButtonClick(){
        Button bt = (Button)findViewById(R.id.btlj1);
        Button bt2 = (Button)findViewById(R.id.btlj2);

        /*
         * Adds a new row into the database
         */
        bt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean aBoolean = myDB.insertData(
                                tv1.getText().toString(),
                                tv2.getText().toString(),
                                tv3.getText().toString(),
                                et.getText().toString());
                        if(aBoolean==true) {
                            Toast.makeText(Record.this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Record.this, MainActivity.class));
                        }else{
                            Toast.makeText(Record.this, "Alert: Note Not Inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        /*
         * Returns to MainActivity if user selects cancel
         */
        bt2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Record.this, "Note Save Cancelled", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Record.this, MainActivity.class));
                    }
                }
        );
    }

    public final void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
           /*Set the text into the TextView tv*/
            if (event.values[0] <= 100) {
                tv1.setText("Dark");
            } else if (event.values[0] <= 1200 && event.values[0] > 100) {
                tv1.setText("Shady");
            } else if (event.values[0] <= 2000 && event.values[0] > 1200) {
                tv1.setText("Low Light");
            } else if (event.values[0] <= 3000 && event.values[0] > 2000) {
                tv1.setText("Indirect Light");
            } else if (event.values[0]<=4000 && event.values[0]>2000){
                tv1.setText("Bright");
            } else if (event.values[0]<120000 && event.values[0]>4000){
                tv1.setText("Direct Light");
            }


        }
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
