package com.af.linksaver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.af.linksaver.data.remote.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private TextView forgotPasswordTextView;
    private ProgressBar progressBar;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseHelper = new FirebaseHelper();
        if (firebaseHelper.getCurrentUser() != null) {
            startMainActivity();
            return;
        }

        initViews();

        setupListeners();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        signUpButton.setOnClickListener(v -> attemptSignUp());
        forgotPasswordTextView.setOnClickListener(v -> recoverPassword());
    }

    private void attemptLogin() {
        if (!validateFields()) {
            return;
        }

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        showProgress(true);

        firebaseHelper.signIn(email, password, task -> {
            showProgress(false);

            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                startMainActivity();
            } else {
                String errorMessage = task.getException() != null ?
                        task.getException().getMessage() : getString(R.string.login_failed);
                showError(errorMessage);
            }
        });
    }

    private void attemptSignUp() {
        if (!validateFields()) {
            return;
        }

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        showProgress(true);

        firebaseHelper.signUp(email, password, task -> {
            showProgress(false);

            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, R.string.signup_success, Toast.LENGTH_SHORT).show();
                startMainActivity();
            } else {
                String errorMessage = task.getException() != null ?
                        task.getException().getMessage() : getString(R.string.signup_failed);
                showError(errorMessage);
            }
        });
    }

    private void recoverPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.error_email_required));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.error_email_invalid));
            return;
        }

        showProgress(true);

        firebaseHelper.getAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    showProgress(false);

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                "Email de recuperação enviado para " + email,
                                Toast.LENGTH_LONG).show();
                    } else {
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "Falha ao enviar email de recuperação";
                        showError(errorMessage);
                    }
                });
    }

    private boolean validateFields() {
        emailEditText.setError(null);
        passwordEditText.setError(null);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.error_email_required));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.error_email_invalid));
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.error_password_required));
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError(getString(R.string.error_password_short));
            return false;
        }

        return true;
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
        signUpButton.setEnabled(!show);
        forgotPasswordTextView.setEnabled(!show);
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}