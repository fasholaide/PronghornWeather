package com.example.bofashola.pronghornweather;

import java.util.ArrayList;
import java.util.List;

public class WeatherValues {
	private String placeName;
	private String currentConditionTempC;
	private String currentConditionTempF;
	private String todayHiTemp;
	private String todayLoTemp;
	private String currentConditionHumidity;
	private String weatherDescription;
	private List<String> dateListed = new ArrayList();
	private List<String> tempMaxCCList = new ArrayList();
	private List<String> tempMaxFFList = new ArrayList();
	private List<String> tempMinCCList = new ArrayList();
	private List<String> tempMinFFList = new ArrayList();
	private List<String> humidityLList = new ArrayList();
	private List<String> weatherDescList = new ArrayList();

	public List<String> getWeatherDescList() {
		return weatherDescList;
	}

	public void setWeatherDescList(List<String> weatherDescList) {
		this.weatherDescList = weatherDescList;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getCurrentConditionTempC() {
		return currentConditionTempC;
	}

	public void setCurrentConditionTempC(String currentConditionTempC) {
		this.currentConditionTempC = currentConditionTempC;
	}

	public String getCurrentConditionTempF() {
		return currentConditionTempF;
	}

	public void setCurrentConditionTempF(String currentConditionTempF) {
		this.currentConditionTempF = currentConditionTempF;
	}

	public String getTodayHiTemp() {
		return todayHiTemp;
	}

	public void setTodayHiTemp(String todayHiTemp) {
		this.todayHiTemp = todayHiTemp;
	}

	public String getTodayLoTemp() {
		return todayLoTemp;
	}

	public void setTodayLoTemp(String todayLoTemp) {
		this.todayLoTemp = todayLoTemp;
	}

	public String getCurrentConditionHumidity() {
		return currentConditionHumidity;
	}

	public void setCurrentConditionHumidity(String currentConditionHumidity) {
		this.currentConditionHumidity = currentConditionHumidity;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

	public List<String> getDateListed() {
		return dateListed;
	}

	public void setDateListed(List<String> dateListed) {
		this.dateListed = dateListed;
	}

	public List<String> getTempMaxCCList() {
		return tempMaxCCList;
	}

	public void setTempMaxCCList(List<String> tempMaxCCList) {
		this.tempMaxCCList = tempMaxCCList;
	}

	public List<String> getTempMaxFFList() {
		return tempMaxFFList;
	}

	public void setTempMaxFFList(List<String> tempMaxFFList) {
		this.tempMaxFFList = tempMaxFFList;
	}

	public List<String> getTempMinCCList() {
		return tempMinCCList;
	}

	public void setTempMinCCList(List<String> tempMinCCList) {
		this.tempMinCCList = tempMinCCList;
	}

	public List<String> getTempMinFFList() {
		return tempMinFFList;
	}

	public void setTempMinFFList(List<String> tempMinFFList) {
		this.tempMinFFList = tempMinFFList;
	}

	public List<String> getHumidityLList() {
		return humidityLList;
	}

	public void setHumidityLList(List<String> humidityLList) {
		this.humidityLList = humidityLList;
	}

}
