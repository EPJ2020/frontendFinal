package ch.lfg.lfg_source.service;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientPost<T> extends AsyncTask<Object, Void, Void> {
  private T message;
  private String url;
  private String token;
  private Class<T> generic;

  public RestClientPost(T message, String token, String url, Class<T> genericClass) {
    this.message = message;
    this.token = token;
    this.url = url;
    this.generic = genericClass;
  }

  @Override
  protected Void doInBackground(Object... objects) {
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<T> request = new HttpEntity<>(message, headers);
      restTemplate.postForEntity(url, request, generic);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }
}
