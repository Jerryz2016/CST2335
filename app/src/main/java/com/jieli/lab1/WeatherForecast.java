package com.jieli.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar =(ProgressBar) findViewById(R.id.progress_Bar);
        Log.i("WeatherForecast","onCreate");
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery thread = new ForecastQuery();
        thread.execute();
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String>{
        private String min;  //min temperature
        private String max;  // max temperature
        private String current; //current temperature
        private String weather; // store the weather for the current weather
        private String iconName; // store the picture name for the current weather
        private Bitmap icon;//  store the bitmap for the current weather
        ProgressBar progressBar =(ProgressBar) findViewById(R.id.progress_Bar);

        @Override
        protected String doInBackground(String[] args) {   //you cannot update GUI in this method
            // Given a string representation of a URL, sets up a connection and gets an input stream.
            final String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
            HttpURLConnection conn;
            InputStream in = null;
            String urlIcon = null;
            HttpURLConnection connIcon = null;
            String result="Wrong";

            try {
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();  //open the connection
                int status = conn.getResponseCode();             // if connected ,the status is 200
                Log.d("Connect ", "Status: " + status);
                if (status == 200) {
                    in = conn.getInputStream();                 //get the xml stream

                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);
                    parser.nextTag();

                    parser.require(XmlPullParser.START_TAG, null, "current");
                    Log.i("Parser START_TAG:", Integer.toString(XmlPullParser.START_TAG));
                    Log.i("END_DOCUMENT:", Integer.toString(XmlPullParser.END_DOCUMENT));
                    while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            parser.next();
                            continue;
                        }
                        String name = parser.getName();
                        Log.i("current Tag ", name);
                        // Starts by looking for the temperature tag
                        if (name.equals("temperature")) {
                            current = parser.getAttributeValue(null, "value");
                            publishProgress((int) 25); // tell android to update the GUI
                            min = parser.getAttributeValue(null, "min");
                            publishProgress((int) 50);
                            max = parser.getAttributeValue(null, "max");
                            publishProgress((int) 75);
                            Log.i("Read current temp. ", current);
                            Log.i("Read min temp. ", min);
                            Log.i("Read max temp. ", max);
                            parser.next();
                        }
                        if (name.equals("weather")) {
                            weather = parser.getAttributeValue(null,"value");
                            iconName = parser.getAttributeValue(null, "icon");
                            Log.i("The weather bitmap name", iconName);
                            break;
                        }
                        parser.next();
                    }
                }
                else{
                    return result;
                }
                }catch(XmlPullParserException | IOException e){
                    e.printStackTrace();
                }finally{
                    try {
                        if (in != null) in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            // deal with the bitmap
            if (iconName != null) {
                String iconFileName=iconName+".png";
                if (fileExistance(iconFileName)==true) {
                    Log.i(iconFileName, "found locally");
                    FileInputStream filestream = null;
                    try {
                //      filestream = new FileInputStream(iconFileName);
                        filestream = openFileInput(iconFileName);
                        icon = BitmapFactory.decodeStream(filestream);
                        Log.i(iconFileName, "read successfully");
                        publishProgress((int) 100);
                        result="Done";
                    } catch (FileNotFoundException e) {
                        Log.i(iconFileName, "FileNotFoundException");
                        result = "Wrong";
                    }
                } else {
                    urlIcon = "http://openweathermap.org/img/w/" + iconName + ".png";
                    try {
                        URL url = new URL(urlIcon);
                        connIcon = (HttpURLConnection) url.openConnection();
                      //  connIcon.connect();
                        int responseCode = connIcon.getResponseCode();
                        if (responseCode == 200) {
                            icon = BitmapFactory.decodeStream(connIcon.getInputStream());
                            Log.i(iconFileName, "is downloaded");
                            // writing data to a file
                            FileOutputStream outputStream = null;
                            outputStream = openFileOutput(iconFileName, Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            publishProgress((int) 100);
                            Log.i(iconFileName, "is stored locally");
                            result= "Done";
                        } else {
                            icon = null;
                            result= "Wrong";
                        }
                    } catch (Exception e) {
                        icon = null;
                        result= "Wrong";
                    } finally {
                        if (connIcon != null) {
                            connIcon.disconnect();
                        }
                    }
                }
             return result;
            }else {
                return "Wrong";
            }
        }
        @Override
        protected void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
      }

     @Override
        protected void onPostExecute(String  result) {  //the parameter must has the same type
            if (result.equals("Done")) {                // with the return from doInbackground()
                TextView currentTemp = (TextView) findViewById(R.id.current_temp);
                TextView minTemp = (TextView) findViewById(R.id.min_temp);
                TextView maxTemp = (TextView) findViewById(R.id.max_temp);
                ImageView weather = (ImageView) findViewById(R.id.weatherIcon);

                currentTemp.setText("Current Temperature: " + current +"°C");
                minTemp.setText("Min Temperature: " + min +"°C" );
                maxTemp.setText("Max Temperature: " + max +"°C" );
                weather.setImageBitmap(icon);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public boolean fileExistance(String fname){
        File file = new File(this.getFilesDir(), fname);//getBaseContext().getFileStreamPath(fname);
        return file.exists();

//        file.delete();
//        return false;
    }
}
