package howblankisit.example.administrator.mainpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import how.blank.isit.R;

/**
 * This is the main activity of the MAD Assignment 1
 * @ author: King, Henry, and Li Jie
 * @ date: 20 Oct 2017
 * **/
public class MainActivity extends AppCompatActivity {

    /*
    * Initialize the variables that will be used in in program
    * */
    private static Button bt;
    private static Spinner dropdown;
    private static ArrayAdapter<String> adapter;
    private static String item;


    /*OnCreate Method*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Declare the string array for creating dropdown list for use to navigate*/
       dropdown = (Spinner)findViewById(R.id.sp1);
        String[] items = new String[]{"", "RANDOM", "HIGH", "BRIGHT"};
       adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        /*Action taken place while clicking on the button*/
        onButtonClick();
    }

    /*onButtonClick method details*/
    public void onButtonClick() {
        /*instantiating button bt called from button bt from xml file*/
        bt = (Button) findViewById(R.id.bt);

        /*using switch/case statement for user to choose the item from the dropdown*/
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*using avariable position to get the index number of array items*/
                item = (String)parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_LONG).show();

                switch (position){
                    case 1:
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, far.class));
                            }
                        });
                        break;
                    case 2:
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, tall.class));
                            }
                        });
                        break;
                    case 3:
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, bright.class));
                            }
                        });
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        }


}
