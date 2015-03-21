package com.example.bofashola.pronghornweather;

import android.support.v7.app.ActionBarActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;



public class DisplayWeatherActivity extends ActionBarActivity {

	String API_KEY = "04a47cf327719eb89d52bf5addf2d";
	String LOCATION = "";
	URL url;
	String line = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_weather);
        LOCATION = getIntent().getStringExtra("location");
        try {
            url = new URL(
                    "http://api.worldweatheronline.com/free/v2/weather.ashx?q="
                            + LOCATION
                            + "&format=xml&num_of_days=5&includelocation=yes&key="
                            + API_KEY);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            Log.d(LocationUtils.APPTAG, e.getMessage());
        }

	}

	@Override
	protected void onStart() {
		super.onStart();
        new LoadUI().execute(url);
        Log.d("DisplayWeather", "I go to the Start Method");
		/*LOCATION = getIntent().getStringExtra("location");
		try {
			url = new URL(
					"http://api.worldweatheronline.com/free/v2/weather.ashx?q="
							+ LOCATION
							+ "&format=xml&num_of_days=5&includelocation=yes&key="
							+ API_KEY);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.d(LocationUtils.APPTAG, e.getMessage());
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_weather, menu);
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

	private class LoadUI extends
			AsyncTask<URL, Integer, WeatherValues> {
		ProgressDialog dialog = null;
		String placeName;
		String currentConditionTempC;
		String currentConditionTempF;
		String todayHiTemp;
		String todayLoTemp;
		String currentConditionHumidity;
		String weatherDescription;
		List<String> dateListed = new ArrayList();
		List<String> tempMaxCCList = new ArrayList();
		List<String> tempMaxFFList = new ArrayList();
		List<String> tempMinCCList = new ArrayList();
		List<String> tempMinFFList = new ArrayList();
		List<String> humidityLList = new ArrayList();
		List<String> weatherDescList = new ArrayList();
		
		protected void onPreExecute()
		{
			dialog = ProgressDialog.show(DisplayWeatherActivity.this, "Loading Weather Report", "Please Wait...", true);
		}
		protected WeatherValues doInBackground(URL... urls) {

			WeatherValues values = new WeatherValues();

			
			try {
                Log.d("DisplayWeather", "The URL is " + url);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();

				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory
						.newDocumentBuilder();
				Document doc = docBuilder.parse(con.getInputStream());
				doc.getDocumentElement().normalize();
				NodeList nearest_area = doc
						.getElementsByTagName("nearest_area");
				if (nearest_area.item(0).getNodeType() == Node.ELEMENT_NODE) {
					Element nearestAreaElement = (Element) nearest_area.item(0);
					String tempAreaName = ((Element) nearestAreaElement
							.getElementsByTagName("areaName").item(0))
							.getChildNodes().item(0).getNodeValue().trim();
					String tempCountry = ((Element) nearestAreaElement
							.getElementsByTagName("country").item(0))
							.getChildNodes().item(0).getNodeValue().trim();
					String tempRegion = ((Element) nearestAreaElement
							.getElementsByTagName("region").item(0))
							.getChildNodes().item(0).getNodeValue().trim();
					placeName = ((tempAreaName != "" || tempAreaName != null) ? tempAreaName
							: "")
							+ " "
							// + ((tempRegion != "" || tempRegion != null) ?
							// tempRegion : "") + " "
							+ ((tempCountry != "" || tempCountry != null) ? tempCountry
									: "");
					values.setPlaceName(placeName);
					Log.d(LocationUtils.APPTAG, placeName);
				}

				NodeList currentCondition = doc
						.getElementsByTagName("current_condition");
				if (nearest_area.item(0).getNodeType() == Node.ELEMENT_NODE) {
					Element currentConditionElement = (Element) currentCondition
							.item(0);
					currentConditionTempC = ((Element) currentConditionElement
							.getElementsByTagName("temp_C").item(0))
							.getChildNodes().item(0).getNodeValue().trim();
					values.setCurrentConditionTempC(currentConditionTempC);
					currentConditionTempF = ((Element) currentConditionElement
							.getElementsByTagName("temp_F").item(0))
							.getChildNodes().item(0).getNodeValue().trim();
					values.setCurrentConditionTempF(currentConditionTempF);
					currentConditionHumidity = ((Element) currentConditionElement
							.getElementsByTagName("humidity").item(0))
							.getChildNodes().item(0).getNodeValue().trim();
					values.setCurrentConditionHumidity(currentConditionHumidity);
					weatherDescription = ((Element) currentConditionElement
							.getElementsByTagName("weatherDesc").item(0))
							.getChildNodes().item(0).getNodeValue().trim();
					values.setWeatherDescription(weatherDescription);
				}

				NodeList weathers = doc.getElementsByTagName("weather");
				for (int i = 0; i < weathers.getLength(); i++) {
                    System.out.print("This is the number" + i);
                    Log.d("Tag", "This is the number" + i);
					Node weat = weathers.item(i);
					if (weat.getNodeType() == Node.ELEMENT_NODE) {
						// Getting Date
						Element dateElement = (Element) weat;
						NodeList dateList = dateElement
								.getElementsByTagName("date");
						Element dateEElement = (Element) dateList.item(0);
						NodeList dateFNList = dateEElement.getChildNodes();
						dateListed.add(((Node) dateFNList.item(0))
								.getNodeValue().trim());

						// Getting Hi Temp in C
						Element tempMaxCElement = (Element) weat;
						NodeList tempMaxCList = tempMaxCElement
								.getElementsByTagName("maxtempC");
						Element tempMaxCEElement = (Element) tempMaxCList
								.item(0);
						NodeList tempMaxCFNList = tempMaxCEElement
								.getChildNodes();
						tempMaxCCList.add(((Node) tempMaxCFNList.item(0))
								.getNodeValue().trim());

						// Getting Hi Temp in F
						Element tempMaxFElement = (Element) weat;
						NodeList tempMaxFList = tempMaxFElement
								.getElementsByTagName("maxtempF");
						Element tempMaxFEElement = (Element) tempMaxFList
								.item(0);
						NodeList tempMaxFFNList = tempMaxFEElement
								.getChildNodes();
						tempMaxFFList.add(((Node) tempMaxFFNList.item(0))
								.getNodeValue().trim());
						
						//Getting Weather Descriptions
						Element tempMinCElement = (Element) weat;
						NodeList tempMinCList = tempMinCElement
								.getElementsByTagName("mintempC");
						Element tempMinCEElement = (Element) tempMinCList
								.item(0);
						NodeList tempMinCFNList = tempMinCEElement
								.getChildNodes();
						tempMinCCList.add(((Node) tempMinCFNList.item(0))
								.getNodeValue().trim());
						
						// Getting Hi Temp in C
						Element weatherDesc = (Element) weat;
						NodeList weatherDescLList = weatherDesc
								.getElementsByTagName("weatherDesc");
						Element weatherDescElement = (Element) weatherDescLList
								.item(0);
						NodeList weatherDescFNList = weatherDescElement
								.getChildNodes();
						weatherDescList.add(((Node) weatherDescFNList.item(0))
								.getNodeValue().trim());

						// Getting Hi Temp in F
						Element tempMinFElement = (Element) weat;
						NodeList tempMinFList = tempMinFElement
								.getElementsByTagName("mintempF");
						Element tempMinFEElement = (Element) tempMinFList
								.item(0);
						NodeList tempMinFFNList = tempMinFEElement
								.getChildNodes();
						tempMinFFList.add(((Node) tempMinFFNList.item(0))
								.getNodeValue().trim());

						// Getting Humidity
						Element humidityElement = (Element) weat;
						NodeList humidityList = humidityElement
								.getElementsByTagName("mintempF");
						Element humidityEElement = (Element) humidityList
								.item(0);
						NodeList humidityFNList = humidityEElement
								.getChildNodes();
						humidityLList.add(((Node) humidityFNList.item(0))
								.getNodeValue().trim());
					}
					values.setHumidityLList(humidityLList);
					values.setTempMaxCCList(tempMaxCCList);
					values.setTempMaxFFList(tempMaxFFList);
					values.setTempMinCCList(tempMinCCList);
					values.setTempMinFFList(tempMinFFList);
					values.setWeatherDescList(weatherDescList);
					values.setDateListed(dateListed);
					
				}
			} catch (SAXParseException err) {
				System.out.println("** Parsing error" + ", line "
						+ err.getLineNumber() + ", uri " + err.getSystemId());
				System.out.println(" " + err.getMessage());
			} catch (SAXException e) {
				Exception x = e.getException();
				((x == null) ? e : x).printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return values;
		}

		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}

		protected void onPostExecute(WeatherValues result) {
			try{//Toast.makeText(DisplayWeatherActivity.this, result.getWeatherDescription(), Toast.LENGTH_LONG).show();
			TextView placeNameTextView = (TextView) findViewById(R.id.textViewWeatherPlaceName);
			placeNameTextView.setText(result.getPlaceName());
			
			TextView currentWeatherDesc = (TextView) findViewById(R.id.textViewWeatherDesc);
			currentWeatherDesc.setText(result.getWeatherDescription());
			
			TextView currentTempF = (TextView) findViewById(R.id.textViewCurrentTempF);
			currentTempF.setText(result.getCurrentConditionTempF()+"°F");
			
			TextView todayHiTempF= (TextView) findViewById(R.id.textViewtodayHiTempF);
			todayHiTempF.setText(result.getTempMaxFFList().get(0).trim()+"°F");
			
			TextView todayHumidity = (TextView) findViewById(R.id.textViewTodayHumidity);
			todayHumidity.setText(result.getCurrentConditionHumidity());
			
			TextView todayLoTempF = (TextView) findViewById(R.id.textViewTodayLoTempF);
			todayLoTempF.setText(result.getTempMinFFList().get(0).trim()+"°F");
			
			TextView date1 = (TextView) findViewById(R.id.textViewDate1);
			date1.setText(result.getDateListed().get(1).replace("2014-", "").trim());
			
			TextView date2 = (TextView) findViewById(R.id.textViewDate2);
			date2.setText(result.getDateListed().get(2).replace("2014-", "").trim());
			
			TextView date3 = (TextView) findViewById(R.id.textViewDate3);
			date3.setText(result.getDateListed().get(3).replace("2014-", "").trim());
			
			TextView date4 = (TextView) findViewById(R.id.textViewDate4);
			date4.setText(result.getDateListed().get(4).replace("2014-", "").trim());
			
			TextView weatherDesc1 = (TextView) findViewById(R.id.textViewWeatherDescDate1);
			weatherDesc1.setText(result.getWeatherDescList().get(1).trim());
			
			TextView weatherDesc2 = (TextView) findViewById(R.id.textViewWeatherDescDate2);
			weatherDesc2.setText(result.getWeatherDescList().get(2).trim());
			
			TextView weatherDesc3 = (TextView) findViewById(R.id.textViewWeatherDescDate3);
			weatherDesc3.setText(result.getWeatherDescList().get(3).trim());
			
			TextView weatherDesc4 = (TextView) findViewById(R.id.textViewWeatherDescDate4);
			weatherDesc4.setText(result.getWeatherDescList().get(4).trim());
			
			TextView hitemp1 = (TextView) findViewById(R.id.textViewHiTemp1);
			hitemp1.setText(result.getTempMaxFFList().get(1).trim()+"°F");
			
			TextView hitemp2 = (TextView) findViewById(R.id.textViewHiTemp2);
			hitemp2.setText(result.getTempMaxFFList().get(2).trim()+"°F");
			
			TextView hitemp3 = (TextView) findViewById(R.id.textViewHiTemp3);
			hitemp3.setText(result.getTempMaxFFList().get(3).trim()+"°F");
			
			TextView hitemp4 = (TextView) findViewById(R.id.textViewHiTemp4);
			hitemp4.setText(result.getTempMaxFFList().get(4).trim()+"°F");
			
			TextView loTemp1 = (TextView) findViewById(R.id.textViewLoTemp1);
			loTemp1.setText(result.getTempMinFFList().get(1).trim()+"°F");
			
			TextView loTemp2 = (TextView) findViewById(R.id.textViewLoTemp2);
			loTemp2.setText(result.getTempMinFFList().get(2).trim()+"°F");
			
			TextView loTemp3 = (TextView) findViewById(R.id.textViewLoTemp3);
			loTemp3.setText(result.getTempMinFFList().get(3).trim()+"°F");
			
			TextView loTemp4 = (TextView) findViewById(R.id.textViewLoTemp4);
			loTemp4.setText(result.getTempMinFFList().get(4).trim()+"°F");
			
			TextView humid1 = (TextView) findViewById(R.id.textViewHumidity1);
			humid1.setText(result.getHumidityLList().get(1).trim());
			
			TextView humid2 = (TextView) findViewById(R.id.textViewHumidity2);
			humid2.setText(result.getHumidityLList().get(2).trim());
			
			TextView humid3 = (TextView) findViewById(R.id.textViewHumidity3);
			humid3.setText(result.getHumidityLList().get(3).trim());
			
			TextView humid4 = (TextView) findViewById(R.id.textViewHumidity4);
			humid4.setText(result.getHumidityLList().get(4).trim());
			
			dialog.dismiss();
			}
			catch(IndexOutOfBoundsException ex)
			{
				Toast.makeText(DisplayWeatherActivity.this, "Cannot Fetch Weather Update at this time.", Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		}
	}

}
