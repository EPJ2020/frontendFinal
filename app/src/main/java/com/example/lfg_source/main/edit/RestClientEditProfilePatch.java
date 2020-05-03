package com.example.lfg_source.main.edit;

import android.os.AsyncTask;

import com.example.lfg_source.entity.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class RestClientEditProfilePatch extends AsyncTask<String, Void, Void> {
  private User message;
  private String url;

  public RestClientEditProfilePatch(User message) {
    this.message = message;
  }

  @Override
  protected Void doInBackground(String... uri) {
    url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      restTemplate.getRequestFactory().createRequest(new URI(url), HttpMethod.PATCH);

      final HttpEntity<User> requestEntity = new HttpEntity<>(message);
      ResponseEntity<User> response =
          restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, User.class);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }

  protected void onPostExecute(ResponseEntity<User> result) {
    HttpStatus statusCode = result.getStatusCode();
    if (statusCode != HttpStatus.OK) {
      this.execute(url);
    }
  }
}
