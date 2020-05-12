package com.example.lfg_source.main.edit;

import android.os.AsyncTask;

import com.example.lfg_source.entity.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientNewUser extends AsyncTask<String, Void, ResponseEntity<User>> {
  private User message;
  private String url;
  private String token;

  public RestClientNewUser(User message, String token) {
    this.message = message;
    this.token = token;
  }

  @Override
  protected ResponseEntity<User> doInBackground(String... uri) {
    url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<User> request = new HttpEntity<>(message, headers);
      return restTemplate.postForEntity(url, request, User.class);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }

  protected void onPostExecute(ResponseEntity<User> result) {
    HttpStatus statusCode = result.getStatusCode();
    if (statusCode != HttpStatus.OK) {
      this.execute(url);
    } else {
      User resultUser = result.getBody();
    }
  }
}
