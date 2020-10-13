package com.slmnhrrn.coronavirustracker.services;

import com.slmnhrrn.coronavirustracker.models.LocationStats;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sun.net.www.http.HttpClient;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronavirusDataService {

    private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();
    public List<LocationStats> getAllStats() {
        return allStats;
    }


    //todo DO NOT SAVE STATE/MEMBER VARIABLES IN A SERVICE
    @PostConstruct
    @Scheduled(cron="* * 1 * * *")
    public void fetchVirusData() {
        List<LocationStats> newStats = new ArrayList<>();
        try{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(VIRUS_DATA_URL).build();
        Response response = client.newCall(request).execute();
        String content = response.body().string();
        StringReader csvBodyReader = new StringReader(content);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(locationStat);
        }
        this.allStats = newStats;

        } catch (Exception e){
            System.out.println("error establishing connection to github data repo");
        }

    }
}
