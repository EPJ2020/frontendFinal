package com.example.lfg_source.main.swipe;

import android.os.AsyncTask;

import com.example.lfg_source.entity.AnswerEntity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientAnswerPost extends AsyncTask<String, Void, Void> {
  private AnswerEntity message;
  private String url;
  private String token;

  public RestClientAnswerPost(AnswerEntity message, String token) {
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
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      HttpEntity<AnswerEntity> request = new HttpEntity<>(message, headers);
      ResponseEntity<AnswerEntity> response =
          restTemplate.postForEntity(url, request, AnswerEntity.class);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }

  protected void onPostExecute(ResponseEntity<AnswerEntity> result) {
    HttpStatus statusCode = result.getStatusCode();
    if (statusCode != HttpStatus.OK) {
      this.execute(url);
    }
  }
}
