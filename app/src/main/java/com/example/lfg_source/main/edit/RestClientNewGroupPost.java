package com.example.lfg_source.main.edit;

import android.os.AsyncTask;

import com.example.lfg_source.entity.Group;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientNewGroupPost extends AsyncTask<String, Void, Void> {
  private Group message;
  private String url;
  private String token;

  public RestClientNewGroupPost(Group message, String token) {
    this.message = message;
    this.token = token;
  }

  @Override
  protected Void doInBackground(String... uri) {
    url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<Group> request = new HttpEntity<>(message, headers);
      ResponseEntity<Group> response = restTemplate.postForEntity(url, request, Group.class);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }

  protected void onPostExecute(ResponseEntity<Group> result) {
    HttpStatus statusCode = result.getStatusCode();
    if (statusCode != HttpStatus.OK) {
      this.execute(url);
    }
  }
}
