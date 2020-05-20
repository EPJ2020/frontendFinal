package ch.lfg.lfg_source.service;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class RestClientPatch<T> extends AsyncTask<String, Void, Void> {
  private T message;
  private String url;
  private String token;
  private Class<T> generic;

  public RestClientPatch(T message, String token, Class<T> genericClass) {
    this.message = message;
    this.token = token;
    this.generic = genericClass;
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
      final HttpEntity<T> requestEntity = new HttpEntity<>(message, headers);
      restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, generic);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }
}
