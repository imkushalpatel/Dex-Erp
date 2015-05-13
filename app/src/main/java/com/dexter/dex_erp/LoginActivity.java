package com.dexter.dex_erp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String USER_ID = "UserId";
    private static final String ENTITY_ID = "EntityId";
    private static final String USER_STATUS = "Login";
    EditText txtUsername, txtPassword;
    TextView txtForgotPassword;
    ProgressDialog dialog = null;
    // Session Manager Class
    SessionManager session;
    // JSON Node names
    // url to make request
    String url;
    // Search EditText
    JSONArray user_data_check = null;
    String Username = null;
    String encodepass = null;
    String pass1;
    String mail = "";
    private Button loginBtn;

    public void onClick(View paramView) {
        if (paramView == this.loginBtn) {

            if ((ValidationMethod.checkEmpty(txtUsername) != true)) {
                check();
            } else {
                dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        Login();
                    }
                }).start();
            }
        }

    }

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.login);

        session = new SessionManager(getApplicationContext());
        // Toast.makeText(getApplicationContext(),
        // "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG)
        // .show();
        this.loginBtn = ((Button) findViewById(R.id.btnLogin));
        this.loginBtn.setOnClickListener(this);

        // this.registerBtn = ((Button) findViewById(R.id.btn_register));
        // this.registerBtn.setOnClickListener(this);

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.etUsername);
        txtPassword = (EditText) findViewById(R.id.etPassword);

    }

    public void check() {

        mail = txtUsername.getText().toString().trim();

        if ((ValidationMethod.checkEmpty(txtUsername))
                && (ValidationMethod.checkEmpty(txtPassword)) != true) {

            Toast.makeText(getBaseContext(), "Please fill up all details",
                    Toast.LENGTH_LONG).show();
        } else {
            if (ValidationMethod.checkEmail(mail) != true) {
                Toast.makeText(getBaseContext(), "Invalid Email id",
                        Toast.LENGTH_LONG).show();
            } else {
                dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        Login();
                    }
                }).start();
            }

        }

    }

    public void Login() {

        try {

            Username = txtUsername.getText().toString().trim();
            String pass = txtPassword.getText().toString().trim();

            System.out.println(txtUsername.getText().toString().trim());
            System.out.println(pass);

            url = getResources().getString(R.string.Login_User_Data) + Username
                    + "/" + pass + "/1";
            System.out.println(url);

            JSONParser jParser = new JSONParser();
            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            Log.i("JSON REPLY", json.toString());

            dialog.dismiss();

            if (json.getString("Login").equals("0")) {

                showAlert();

            } else {

                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(LoginActivity.this, "Login Successfull",
                                Toast.LENGTH_SHORT).show();

                    }
                });

                String id = json.getString(USER_ID);
                String enityId = json.getString(ENTITY_ID);
                String status = json.getString(USER_STATUS);

                session.createLoginSession(id, enityId, status);
                // System.out.println("Seesion :" + session);

                // Start New Activity
                Intent interntDashboard = new Intent(getApplicationContext(),
                        Home_Screen.class);
                interntDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                interntDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(interntDashboard);
                finish();
                // End
            }

        } catch (Exception e) {
            dialog.dismiss();
            // System.out.println("Exception : " + e.getMessage());
        }

    }

    public void showAlert() {
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are You Sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
