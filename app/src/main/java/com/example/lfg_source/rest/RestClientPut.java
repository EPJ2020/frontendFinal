package com.example.lfg_source.rest;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientPut extends AsyncTask<String, Void, Void> {
  private boolean message;

  public void setMessage(boolean message) {
    this.message = message;
  }

  @Override
  protected Void doInBackground(String... uri) {
    final String url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      restTemplate.put(url, message);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }
}
