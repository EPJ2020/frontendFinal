package com.example.lfg_source.service;

import android.os.AsyncTask;

import com.example.lfg_source.entity.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientGetUser<T> extends AsyncTask<Object, Void, ResponseEntity<T>> {
  private MyService myService;
  private String token;
  private Class<T> generic;
  private String url;

  public RestClientGetUser(MyService myService, String token, String url, Class<T> generic) {
    this.myService = myService;
    this.token = token;
    this.generic = generic;
    this.url = url;
  }

  @Override
  protected ResponseEntity<T> doInBackground(Object... objects) {
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, generic);
      return response;
    } catch (Exception e) {
      String message = e.getMessage();
      return null;
    }
  }

  @Override
  protected void onPostExecute(ResponseEntity<T> result) {
    if (result != null) {
      HttpStatus statusCode = result.getStatusCode();
      myService.setLoginUser((User) result.getBody());
    }
  }
}
