package com.example.bofashola.pronghornweather;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PronghornWeatherActivity extends ActionBarActivity {
	RadioGroup radioChoiceGroup;
	Button locationChoiceButton;
	RadioButton radioChoiceButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pronghorn_activity_main);
		addListenerOnButton();

	}

	public void addListenerOnButton() {

		radioChoiceGroup = (RadioGroup) findViewById(R.id.radioGrouped);
		locationChoiceButton = (Button) findViewById(R.id.locationChoiceButton);

		locationChoiceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// get selected radio button from radioGroup
				int selectedId = radioChoiceGroup.getCheckedRadioButtonId();
				if (selectedId != -1) {
					// find the radiobutton by returned id
					radioChoiceButton = (RadioButton) findViewById(selectedId);
					if (radioChoiceButton.getText().equals("Via Place Name"))
						startActivity(new Intent(
								"com.example.bofashola.pronghornweather.PlaceNameActivity"));
					else if (radioChoiceButton.getText().toString().trim()
							.equals("Via Zip Code"))
						startActivity(new Intent(
								"com.example.bofashola.pronghornweather.ZipCodeActivity"));
					else if (radioChoiceButton.getText().toString().trim()
							.equals("Via Google Maps"))
						startActivity(new Intent(
								"com.example.bofashola.pronghornweather.MapActivity"));
				} else
					Toast.makeText(PronghornWeatherActivity.this,
							"Choose An Option", Toast.LENGTH_SHORT).show();

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pronghorn_weather, menu);
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
