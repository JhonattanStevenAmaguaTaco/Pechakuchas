package com.jhonattanApechakuchas.unirestbundle;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.osgi.service.component.annotations.Component;

@Component(service = MyHttpClientService.class, immediate = true)
public class MyHttpClientService {

    public String fetchDataFromUrl(String url) {
        try {
            HttpResponse<String> response = Unirest.get(url)
                .header("accept", "application/json")
                .asString();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching data from: " + url;
        }
    }
}
