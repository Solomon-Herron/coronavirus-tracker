package com.slmnhrrn.coronavirustracker.services;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import sun.net.www.http.HttpClient;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CoronavirusDataService {

    private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct
    public void fetchVirusData() {
        try{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(VIRUS_DATA_URL).build();
        Response response = client.newCall(request).execute();
        String content = response.body().string();
        StringReader csvBodyReader = new StringReader(content);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
//        System.out.println(csvBodyReader);
//        System.out.println("==============================================================================================================================");
//        System.out.println(content);
        for (CSVRecord record : records) {
            String state = record.get("Province/State");
            System.out.println(state);
        }
        } catch (Exception e){
            System.out.println("error establishing connection to github data repo");
        }

    }
}

//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null){
//                content.append(inputLine);
//            }
//        String x  = content.toString();
//        System.out.println(x.replaceAll("\\s", ""));