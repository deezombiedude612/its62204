package howblankisit.example.administrator.mainpage;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;

import how.blank.isit.R;

public class far extends AppCompatActivity {
    private String instructions[] = {
            "Shake the phone and stop to get your number!", "OK, you can stop anytime.", "Press START to go again!"};

    private TextView tv_whatNo, tv_instruction;
    private EditText et_from, et_to;
    private Button btn_start;

    private boolean changedColor = false;

    private SensorManager thisSensorManager;
    private Sensor accelSensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1500;

    private SensorEventListener thisSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                long curTime = System.currentTimeMillis();

                if((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                    if(speed > SHAKE_THRESHOLD) {
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                        tv_instruction.setText(instructions[1]);
                        tv_whatNo.setText("Generating number..");
                        changedColor = true;
                    }
                    else {
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                        if(changedColor)
                            tv_instruction.setText(instructions[2]);
                        int from = 0, to = 0;

                        if(et_from.getText().toString().trim().length() > 0) {
                            try {
                                from = Integer.parseInt(et_from.getText().toString());
                            } catch(Exception ex) {
                                from = 0;
                            }
                        }
                        if(et_to.getText().toString().trim().length() > 0) {
                            try {
                                to = Integer.parseInt(et_to.getText().toString());
                            } catch(Exception ex) {
                                to = 0;
                            }
                        }

                        if(from > to) {
                            int temp = from;
                            from = to;
                            to = temp;
                        }

                        int generatedNumber = from + (int)(Math.random() * ((to - from) + 1));

                        tv_whatNo.setText("" + generatedNumber);

                        btn_start.setEnabled(true);
                        if(changedColor) {
                            thisSensorManager.unregisterListener(thisSensorEventListener);
                            changedColor = false;
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_far);
        onButtonClick();
        tv_whatNo = (TextView)findViewById(R.id.whatNo);
        tv_instruction = (TextView)findViewById(R.id.tv_instruction);
        et_from = (EditText)findViewById(R.id.range_from);
        et_to = (EditText)findViewById(R.id.range_to);
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                accelSensor = thisSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                thisSensorManager.registerListener(thisSensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
                tv_instruction.setText(instructions[0]);
                tv_whatNo.setText("0");
                btn_start.setEnabled(false);
            }
        });


    }
    public void onButtonClick(){
        Button bt = (Button)findViewById(R.id.bt1);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(far.this, MainActivity.class));
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
