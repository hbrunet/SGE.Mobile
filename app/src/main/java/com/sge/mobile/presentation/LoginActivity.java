package com.sge.mobile.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sge.mobile.domain.core.DomainObjectUtils;


public class LoginActivity extends AppCompatActivity {
    private TextView txtUserName;
    private TextView txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        this.txtUserName = (TextView) findViewById(R.id.txtUserName);
        this.txtPassword = (TextView) findViewById(R.id.txtPassword);
    }

    public void Login(View button) {
        String user = this.txtUserName.getText().toString().trim();
        String password = this.txtPassword.getText().toString().trim();
        if (DomainObjectUtils.textHasContent(user)
                && DomainObjectUtils.textHasContent(password)) {

            LoginAsyncTask loginAsyncTask = new LoginAsyncTask(LoginActivity.this, user, password);
            loginAsyncTask.execute();
        } else {
            Toast.makeText(getBaseContext(), "Los campos Nombre de usuario y Contraseña son obligatorios.", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
