package com.example.bofashola.pronghornweather;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ZipCodeActivity extends ActionBarActivity {
    Button fetchReport;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zipcode_main);
        addListenerOnButton();
	}

    public void addListenerOnButton() {
        fetchReport = (Button) findViewById(R.id.buttonZipCode);

        fetchReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get value from the EditText
                EditText text = (EditText) findViewById(R.id.editTextZipCode);
                String textValue = text.getText().toString().trim();
                if (textValue == null || textValue.equals(""))
                    Toast.makeText(ZipCodeActivity.this, "Input A Value",
                            Toast.LENGTH_SHORT).show();
                else {
                    Intent i = new Intent(
                            "com.example.bofashola.pronghornweather.DisplayWeatherActivity");
                    i.putExtra("location", textValue);
                    startActivity(i);
                }
            }
        });

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.zip_code, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	    if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
