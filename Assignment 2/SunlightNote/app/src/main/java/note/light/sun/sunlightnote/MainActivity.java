package note.light.sun.sunlightnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB; // Database object
    Cursor res = null; // Cursor pointing towards database rows

    private ListView listView;
    private List<Map<String, String>> items = new ArrayList<Map<String, String>>();		// converted storage for "records"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Create instance of DatabaseHelper
         */
        myDB = DatabaseHelper.getInstance(this);
        listView = (ListView)findViewById(R.id.mainList);

        onButtonClick();

        /*
         * Retrieve all rows in database
         */
        res = myDB.retrieveAll();
        if(res.getCount()==0) {
            showMessage("Nothing Found", "No Records Found");
            return;
        }

        final ListAdapter listAdapter = createListAdapter();
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                res.moveToFirst();	// return cursor to the beginning
                int iter = 0;	// position of record in list
                do {
					/* if position where record resides is reached */
                    if(iter == position) {
                        /*
                         * Create intent with extra data - the ID that corresponds to the selected row
                         */
                        Intent intent = new Intent(getBaseContext(), View.class);
                        intent.putExtra("EXTRA_ROW_ID", res.getString(0));
                        startActivity(intent);
                        break;
                    }
                    iter++;
                } while(res.moveToNext());
            }
        });
    }

    public ListAdapter createListAdapter() {
        final String[] fromMapKey = new String[] {"TEXT1", "TEXT2"};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};

        while(res.moveToNext()) {
            Map<String, String> listItem = new HashMap<>();
            listItem.put(fromMapKey[0], "Date: " + res.getString(2) + "    Time: " + res.getString(3));
            listItem.put(fromMapKey[1], "Note: " + res.getString(4));
            items.add(listItem);
        }

        return new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
    }

    /*
     * Begins Record Activity when + new note is selected
     */
    public void onButtonClick() {
        Button bt = (Button)findViewById(R.id.btn_add);

        bt.setOnClickListener(
                new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        startActivity(new Intent(MainActivity.this, Record.class));
                    }
                }
        );
    }

    /*
     * Display pop up message if database retrieved 0 results for listView
     */
    public void showMessage(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });
        builder.setMessage(message);
        builder.show();
    }

}
