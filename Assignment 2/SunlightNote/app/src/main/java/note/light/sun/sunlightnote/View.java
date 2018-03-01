package note.light.sun.sunlightnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class View extends AppCompatActivity {
    Button bt, bt2;
    TextView tv1, tv2, tv3;
    EditText et;
    DatabaseHelper myDB;
    String id = null; // variable for holding ID retrieved from database
    int selectedRow = 0; // variable for holding selected row ID from MainActivity listView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        tv1 = (TextView) findViewById(R.id.etlj1);
        tv2 = (TextView) findViewById(R.id.etlj2);
        tv3 = (TextView) findViewById(R.id.etlj3);
        et = (EditText) findViewById(R.id.etlj4);
        et.setEnabled(false);

        myDB = DatabaseHelper.getInstance(this);

        /*
         * Retrieve the extra data passed from MainActivity listView
         */
        selectedRow = Integer.parseInt(getIntent().getStringExtra("EXTRA_ROW_ID"));
        showRecord();
        onButtonClick();
    }

    /*
     * Retrieve all column data in the selected row
     */
    public void showRecord(){
        try {
            String res[] = myDB.retrieveData(selectedRow);

            id = res[0];
            tv1.setText(res[1]);
            tv2.setText(res[2]);
            tv3.setText(res[3]);
            et.setText(res[4]);
        }
        catch (Exception e) {
            Toast.makeText(View.this, "Error: Note not found or no retrievable", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Button actions
     */
    public void onButtonClick(){
        bt = (Button)findViewById(R.id.btlj1);
        bt2 = (Button)findViewById(R.id.btlj2);

        bt.setOnClickListener(
                new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        if(bt.getText().toString().equals("Edit")) {
                            Toast.makeText(View.this, "Opened for editing", Toast.LENGTH_LONG).show();
                            bt.setText("Update");
                            bt2.setText("Cancel");
                            et.setEnabled(true);
                        }
                        else {
                            Boolean aBoolean = myDB.updateData(
                                    id,
                                    tv1.getText().toString(),
                                    tv2.getText().toString(),
                                    tv3.getText().toString(),
                                    et.getText().toString());

                            if(aBoolean==true) {
                                Toast.makeText(View.this, "Note Updated Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(View.this, MainActivity.class));
                            }
                            else {
                                Toast.makeText(View.this, "Error: Note Not Updated", Toast.LENGTH_SHORT).show();
                            }

                            bt.setText("Edit");
                            bt2.setText("Delete");
                            et.setEnabled(false);
                        }
                    }
                }
        );

        bt2.setOnClickListener(
                new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        if(bt2.getText().toString().equals("Delete")) {
                            int deletedRows = myDB.deleteData(selectedRow);

                            if(deletedRows > 0) {
                                Toast.makeText(View.this, "Deleted the Note", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(View.this, MainActivity.class));
                            }
                            else {
                                Toast.makeText(View.this, "Error: Failed to Delete Note", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            showRecord();
                            Toast.makeText(View.this, "Changes Not Saved", Toast.LENGTH_LONG).show();
                            bt.setText("Edit");
                            bt2.setText("Delete");
                            et.setEnabled(false);
                        }
                    }
                }
        );
    }
}
