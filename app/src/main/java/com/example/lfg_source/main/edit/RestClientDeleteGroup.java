package com.example.lfg_source.main.edit;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientDeleteGroup extends AsyncTask<String, Void, Void> {
  private String url;

  @Override
  protected Void doInBackground(String... uri) {
    url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<String> entity = new HttpEntity<String>(headers);

      restTemplate.delete(url);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }
}
