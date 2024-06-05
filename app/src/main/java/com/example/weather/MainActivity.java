package com.example.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.jar.JarException;

public class MainActivity extends AppCompatActivity {

    EditText etCity , etCountry;
    TextView tvResult;
    Button btnGet;
    private final  String urlWeatherApi = "https://api.openweathermap.org/data/2.5/weather";
    private final  String urlGeoApi = "http://api.openweathermap.org/geo/1.0/direct";
    private final String appId = "ec15e1a581646fd1581ca64e3e42bd30";
    DecimalFormat df =  new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        etCountry=findViewById(R.id.etCountry);
        tvResult=findViewById(R.id.tvResult);
        btnGet = findViewById(R.id.btnGet);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempUrl = "";
                String city = etCity.getText().toString().trim();
                String country = etCountry.getText().toString().trim();
                if (city.isEmpty()) {
                    tvResult.setText("City field cannot be empty");
                } else {
                    if (!country.isEmpty()) {
                        tempUrl = urlWeatherApi + "?q=" + city + "," + country + "&limit=1&appid=" + appId;
                    } else {
                        tempUrl = urlWeatherApi + "?q=" + city + "&limit=1&appid=" + appId;
                    }
                    StringRequest stringrequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            String output= "";
                            try{
                                JSONObject jsonResponse = new JSONObject(s);
                                JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                String description = jsonObjectWeather.getString("description");
                                JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                                double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                                float pressure = jsonObjectMain.getInt("pressure");
                                int humidity = jsonObjectMain.getInt("humidity");
                                JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");
                                JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                                String clouds = jsonObjectClouds.getString("all");
                                JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                                String countryName = jsonObjectSys.getString("country");
                                String cityName = jsonResponse.getString("name");
                                tvResult.setTextColor(Color.rgb(0, 255, 0));
                                output += "Current weather of " + cityName + " (" + countryName + ")"
                                        + "\n Temp: " + df.format(temp) + " °C"
                                        + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                        + "\n Humidity: " + humidity + "%"
                                        + "\n Description: " + description
                                        + "\n Wind Speed: " + wind + "m/s (meters per second)"
                                        + "\n Cloudiness: " + clouds + "%"
                                        + "\n Pressure: " + pressure + " hPa";
                                tvResult.setText(output);
                            }catch (JSONException e){
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringrequest);
                }
            }
        });
    }


}