package com.example.lfg_source.main.edit;

import android.os.AsyncTask;

import com.example.lfg_source.entity.Group;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class RestClientEditGroupPatch extends AsyncTask<String, Void, Void> {
  private Group message;
  private String url;
  private String token;

  public RestClientEditGroupPatch(Group message, String token) {
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
      restTemplate.getRequestFactory().createRequest(new URI(url), HttpMethod.PATCH);
      final HttpEntity<Group> requestEntity = new HttpEntity<>(message, headers);
      ResponseEntity<Group> response =
          restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, Group.class);
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
