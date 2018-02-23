package edu.self.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prepare contents of listview
        ArrayList data;
        data = new ArrayList<String>();
        data.add("東京");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        ListView AreaList = (ListView)findViewById(R.id.listView);
        AreaList.setAdapter(adapter);

        AreaList.setOnItemClickListener(new ListItemClickListner());

    }
    private class ListItemClickListner implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){

            String item = (String)parent.getItemAtPosition(position);
            //String msg = position + "がクリックされました";
            //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MainActivity.this, WeatherInfo.class);
            intent.putExtra("Area",item);
            startActivity(intent);
        }

    }
}
