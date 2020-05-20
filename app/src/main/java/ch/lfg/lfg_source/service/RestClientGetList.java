package ch.lfg.lfg_source.service;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import ch.lfg.lfg_source.entity.Group;
import ch.lfg.lfg_source.entity.GroupSuggestion;
import ch.lfg.lfg_source.entity.User;
import ch.lfg.lfg_source.entity.UserSuggestion;

public class RestClientGetList<T> extends AsyncTask<Object, Void, ResponseEntity<T>> {
  private static final String URL_FRAGMENT_MY = "My";
  private static final String URL_FRAGMENT_USER_MATCHES = "User/Matches";
  private static final String URL_FRAGMENT_GROUP_MATCHES = "/Group/Matches/";
  private static final String URL_FRAGMENT_GROUP_SUGGESTIONS = "Group/Suggestions/";
  private static final String URL_FRAGMENT_USER_SUGGESTIONS = "User/Suggestions";

  private MyService myService;
  private String url;
  private String token;
  private Class<T> genericClass;

  public RestClientGetList(MyService myService, String token, String url, Class<T> genericClass) {
    this.myService = myService;
    this.token = token;
    this.url = url;
    this.genericClass = genericClass;
  }

  @Override
  protected ResponseEntity<T> doInBackground(Object... objects) {
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, genericClass);
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
      if (url.contains(URL_FRAGMENT_MY)) {
        myService.setDataMyGroup(new ArrayList<>(Arrays.asList((Group[]) result.getBody())));
        return;
      }
      if (url.contains(URL_FRAGMENT_USER_MATCHES)) {
        myService.setDataMatchGroup(new ArrayList<>(Arrays.asList((Group[]) result.getBody())));
        return;
      }
      if (url.contains(URL_FRAGMENT_GROUP_SUGGESTIONS)) {
        myService.setUserSuggestions(
            new ArrayList<>(Arrays.asList((UserSuggestion[]) result.getBody())));
      }
      if (url.contains(URL_FRAGMENT_USER_SUGGESTIONS)) {
        myService.setGroupSuggestions(
            new ArrayList<>(Arrays.asList((GroupSuggestion[]) result.getBody())));
      }
      if (url.contains(URL_FRAGMENT_GROUP_MATCHES)) {
        myService.setDataMatchUsers(new ArrayList<>(Arrays.asList((User[]) result.getBody())));
      }
    }
  }
}
