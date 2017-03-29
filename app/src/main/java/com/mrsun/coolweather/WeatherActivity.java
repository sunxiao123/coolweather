package com.mrsun.coolweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrsun.coolweather.gson.Weather;
import com.mrsun.coolweather.util.HttpUtil;
import com.mrsun.coolweather.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView sportText;
    private TextView carWashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.api_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString!=null){
            //有缓存直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    /**
     * 根据天气id 请求城市天气信息
     * @param weatherId
     */
    private void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=93a05d77f14a456ea0ca6884ce9cf1f9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                final Weather.HeWeatherBean heWeatherBean= (Weather.HeWeatherBean) weather.getHeWeather();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather !=null&&"ok".equals(heWeatherBean.getStatus())){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        Weather.HeWeatherBean heWeather = (Weather.HeWeatherBean) weather.getHeWeather();
        Weather.HeWeatherBean.BasicBean basic = heWeather.getBasic();
        String cityName = basic.getCity();

        Weather.HeWeatherBean.BasicBean.UpdateBean update = basic.getUpdate();
        String updateTime = update.getLoc().split(" ")[1];

        Weather.HeWeatherBean.NowBean now = heWeather.getNow();
        String degree = now.getTmp()+"℃";

        Weather.HeWeatherBean.NowBean.CondBean cond = now.getCond();
        String weatherInfo = cond.getTxt();

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        List<Weather.HeWeatherBean.DailyForecastBean> daily_forecast = heWeather.getDaily_forecast();

        for (Weather.HeWeatherBean.DailyForecastBean forecast:daily_forecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.getDate());
            Weather.HeWeatherBean.DailyForecastBean.CondBeanX cond1 = forecast.getCond();
            infoText.setText(cond1.getTxt_d());

            Weather.HeWeatherBean.DailyForecastBean.TmpBean tmp = forecast.getTmp();
            maxText.setText(tmp.getMax());
            minText.setText(tmp.getMin());
            forecastLayout.addView(view);
        }
        if (heWeather.getAqi()!=null){
            Weather.HeWeatherBean.AqiBean aqi = heWeather.getAqi();
            Weather.HeWeatherBean.AqiBean.CityBean city = aqi.getCity();
            aqiText.setText(city.getAqi());
            pm25Text.setText(city.getPm25());
        }
        Weather.HeWeatherBean.SuggestionBean suggestion = heWeather.getSuggestion();
        Weather.HeWeatherBean.SuggestionBean.ComfBean comf = suggestion.getComf();
        String comfort = "舒适度: " + comf.getTxt();
        Weather.HeWeatherBean.SuggestionBean.CwBean cw = suggestion.getCw();
        String carWash = "洗车指数: " + cw.getTxt();
        Weather.HeWeatherBean.SuggestionBean.SportBean sportData = suggestion.getSport();
        String sport = "运动建议: " + sportData.getTxt();
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
