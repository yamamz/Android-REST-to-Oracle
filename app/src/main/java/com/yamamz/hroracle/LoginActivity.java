package com.yamamz.hroracle;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yamamz.hroracle.model.Emp;
import com.yamamz.hroracle.retrofitAPI.ServiceGenerator;
import com.yamamz.hroracle.retrofitAPI.apiServices;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Raymundo T. Melecio on 11/30/2016.
 */



public class LoginActivity extends AppCompatActivity {




    private EditText myUsername;
    private EditText myPasswordView;
    private EditText myServer;

    private Button btnLogin;

    private Boolean isAutheticated=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


initViews();
    }


   void initViews(){
      myUsername=(EditText) findViewById(R.id.user_name);
       myPasswordView=(EditText) findViewById(R.id.password);
       myServer=(EditText) findViewById(R.id.serverIP);
       btnLogin=(Button) findViewById(R.id.btnLogin);

       btnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               login();
           }
       });



    }

    public void login() {


        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);



        String username = myUsername.getText().toString();
        String password = myPasswordView.getText().toString();
        String server=myServer.getText().toString();

        // TODO: Implement your own authentication logic here.

        getAuthenticated(username,password,server);


    }




    @Override
    public void onBackPressed() {

        /**
         *para dli mo balik sa main activity
         */
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        /**
         * kung tama ang username ug password e save sa prefs pra next mo open sa app
         * dli na mo log in
         */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",myUsername.getText().toString());
        editor.putString("password",myPasswordView.getText().toString());
        editor.putString("server",myServer.getText().toString());
        editor.apply();
       btnLogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        /**
         * E check ang edittext kung naay input return ug boalean value
         *
         */
        boolean valid = true;
        String username = myUsername.getText().toString();
        String password = myPasswordView.getText().toString();
        if (username.isEmpty()) {
            myUsername.setError("enter a valid username address");
            valid = false;
        } else {
            myUsername.setError(null);
        }
        if (password.isEmpty()) {
           myPasswordView.setError("enter a valid password");
            valid = false;
        } else {
            myPasswordView.setError(null);
        }
        return valid;
    }


   void getAuthenticated(String Username, String Password,String Server) {


       apiServices service = ServiceGenerator.createService(apiServices.class, Username,
               Password,Server);
       Call<ArrayList<Emp>> call = service.getAutheticated();
       call.enqueue(new Callback<ArrayList<Emp>>() {
           @Override
           public void onResponse(Call<ArrayList<Emp>> call, Response<ArrayList<Emp>> response) {

               if(response.code()==200) {
                   isAutheticated=true;
                   final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                   progressDialog.setIndeterminate(true);
                   progressDialog.setMessage("Authenticating...");
                   progressDialog.show();
                   new android.os.Handler().postDelayed(
                           new Runnable() {
                               public void run() {
                                   // On complete call either onLoginSuccess or onLoginFailed
                                   onLoginSuccess();
                                   progressDialog.dismiss();
                               }
                           }, 500);
               }

               else{
                   onLoginFailed();
                   myUsername.setText("");
                   myPasswordView.setText("");


               }

           }

           @Override
           public void onFailure(Call<ArrayList<Emp>> call, Throwable t) {

               onLoginFailed();
               myUsername.setText("");
               myPasswordView.setText("");

           }


       });




   }
}

