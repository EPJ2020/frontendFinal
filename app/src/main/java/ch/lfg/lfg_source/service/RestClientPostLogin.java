package ch.lfg.lfg_source.service;

import android.os.AsyncTask;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ch.lfg.lfg_source.entity.LoginEntity;

public class RestClientPostLogin extends AsyncTask<String, Void, ResponseEntity<String>> {
  private static final String LOGIN_FAILED_TEXT = "Login fehlgeschlagen";

  private MyService myService;
  private LoginEntity loginEntity;
  private String url;

  public RestClientPostLogin(MyService myService, LoginEntity loginEntity, String url) {
    this.myService = myService;
    this.loginEntity = loginEntity;
    this.url = url;
  }

  @Override
  protected ResponseEntity<String> doInBackground(String... uri) {
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      return restTemplate.postForEntity(url, loginEntity, String.class);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }

  protected void onPostExecute(ResponseEntity<String> result) {
    if (result == null) {
      myService.setLoginFailMessage(LOGIN_FAILED_TEXT);
    } else {
      HttpStatus statusCode = result.getStatusCode();
      if (statusCode != HttpStatus.OK) {
        myService.setLoginFailMessage(LOGIN_FAILED_TEXT);
      } else {
        myService.setLoginData(result.getBody());
      }
    }
  }
}
