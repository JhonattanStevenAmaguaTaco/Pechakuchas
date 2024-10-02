package com.jhonattanApechakuchas.core.models;


import com.jhonattanApechakuchas.unirestbundle.MyHttpClientService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class UrlFetcherModel {

    @ValueMapValue
    private String url;

    @OSGiService
    private MyHttpClientService httpClientService;

    private String response;

    @PostConstruct
    protected void init() {
        if (url != null && !url.isEmpty()) {
            response = httpClientService.fetchDataFromUrl(url);
        } else {
            response = "URL not provided";
        }
    }

    public String getResponse() {
        return response;
    }
}
