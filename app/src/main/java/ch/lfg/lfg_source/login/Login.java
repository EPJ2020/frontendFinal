package ch.lfg.lfg_source.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.entity.LoginFormState;

public class Login extends AppCompatActivity {
  private LoginFacade facade;

  private EditText usernameEditText;
  private EditText passwordEditText;
  private Button loginButton;
  private Button goToRegistrationButton;
  private Button registrationButton;
  private Button cancelButton;
  private ProgressBar loadingProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    facade = new LoginFacade(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);

    initViewElements();
    setupObservers();
    setTextChangeListener();
    setButtonListeners();
  }

  private void initViewElements() {
    usernameEditText = findViewById(R.id.username);
    passwordEditText = findViewById(R.id.password);
    loginButton = findViewById(R.id.login);
    goToRegistrationButton = findViewById(R.id.register);
    registrationButton = findViewById(R.id.register2);
    cancelButton = findViewById(R.id.cancelRegistration);
    loadingProgressBar = findViewById(R.id.loading);
  }

  private void setButtonListeners() {
    passwordEditText.setOnEditorActionListener(
        new TextView.OnEditorActionListener() {

          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
              facade.login(
                  usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
            return false;
          }
        });

    loginButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            facade.login(
                usernameEditText.getText().toString(), passwordEditText.getText().toString());
          }
        });

    goToRegistrationButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loginButton.setVisibility(View.GONE);
            goToRegistrationButton.setVisibility(View.GONE);
            registrationButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
          }
        });

    cancelButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loginButton.setVisibility(View.VISIBLE);
            goToRegistrationButton.setVisibility(View.VISIBLE);
            registrationButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
          }
        });

    registrationButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            facade.register(
                usernameEditText.getText().toString(), passwordEditText.getText().toString());
          }
        });
  }

  private void setTextChangeListener() {

    TextWatcher afterTextChangedListener =
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            // ignore
          }

          @Override
          public void afterTextChanged(Editable s) {
            facade.loginDataChanged(
                usernameEditText.getText().toString(), passwordEditText.getText().toString());
          }
        };

    usernameEditText.addTextChangedListener(afterTextChangedListener);
    passwordEditText.addTextChangedListener(afterTextChangedListener);
  }

  private void setupObservers() {
    facade
        .getLoginFormState()
        .observe(
            this,
            new Observer<LoginFormState>() {
              @Override
              public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                  return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                  usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                  passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                registrationButton.setEnabled(loginFormState.isDataValid());
              }
            });

    facade
        .getService()
        .getLoginResult()
        .observe(
            this,
            new Observer<String>() {
              @Override
              public void onChanged(@Nullable String loginResult) {
                if (loginResult == null) {
                  showLoginFailed();
                  return;
                } else {
                  Intent returnIntent = new Intent();
                  returnIntent.putExtra(getResources().getString(R.string.usertoken), loginResult);
                  setResult(Activity.RESULT_OK, returnIntent);
                  finish();
                }
                loadingProgressBar.setVisibility(View.GONE);
              }
            });

    final Activity login = this;
    facade
        .getService()
        .getLoginFailMessage()
        .observe(
            this,
            new Observer<String>() {
              @Override
              public void onChanged(String text) {
                Toast.makeText(login, text, Toast.LENGTH_SHORT).show();
                loadingProgressBar.setVisibility(View.GONE);
              }
            });
  }

  private void showLoginFailed() {
    Toast.makeText(
            getApplicationContext(), getResources().getString(R.string.retry), Toast.LENGTH_SHORT)
        .show();
  }

  @Override
  public void onBackPressed() {
    moveTaskToBack(false);
  }
}
