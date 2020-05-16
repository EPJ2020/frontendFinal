package com.example.lfg_source.login;

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
import androidx.lifecycle.ViewModelProviders;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.LoginFormState;

public class Login extends AppCompatActivity {

  private LoginViewModel loginViewModel;

  private EditText usernameEditText;
  private EditText passwordEditText;
  private Button loginButton;
  private Button goToRegistrationButton;
  private Button registrationButton;
  private Button cancelButton;
  private ProgressBar loadingProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    usernameEditText = findViewById(R.id.username);
    passwordEditText = findViewById(R.id.password);
    loginButton = findViewById(R.id.login);
    goToRegistrationButton = findViewById(R.id.register);
    registrationButton = findViewById(R.id.register2);
    cancelButton = findViewById(R.id.cancelRegistration);
    loadingProgressBar = findViewById(R.id.loading);
    loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

    loginViewModel
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

    loginViewModel
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
                  returnIntent.putExtra("usertoken", loginResult);
                  setResult(Activity.RESULT_OK, returnIntent);
                  finish();
                }
                loadingProgressBar.setVisibility(View.GONE);
              }
            });

    final Activity login = this;
    loginViewModel
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
            loginViewModel.loginDataChanged(
                usernameEditText.getText().toString(), passwordEditText.getText().toString());
          }
        };

    usernameEditText.addTextChangedListener(afterTextChangedListener);
    passwordEditText.addTextChangedListener(afterTextChangedListener);
    passwordEditText.setOnEditorActionListener(
        new TextView.OnEditorActionListener() {

          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
              loginViewModel.login(
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
            loginViewModel.login(
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
            loginViewModel.register(
                usernameEditText.getText().toString(), passwordEditText.getText().toString());
          }
        });
  }

  private void showLoginFailed() {
    Toast.makeText(getApplicationContext(), "Versuchen Sie es erneut", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onBackPressed() {
    moveTaskToBack(false);
  }
}
