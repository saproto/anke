package com.proto;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.proto.oauth.AuthorizationToken;
import com.proto.oauth.SAProtoClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });
    }

    public void authenticateUser(){
        //TODO Dennis doe deze eens goed wegwerken
        String clientID = "Put Your Client ID Here";
        String clientSecret = "Put Your Client CodeHere!"
        String token;
        String redirect_uri = "Insert The Redirect URI Here";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.proto.utwente.nl/oauth/authorize"+"?client_id="+clientID+"&response_type=code"+"&scope=*"+"&redirect_uri="+ redirect_uri));
        startActivity(intent);

    }

    @Override
    public void onResume(){
        super.onResume();
        String code;
        String clientID = "Put Your Client ID Here";
        String clientSecret = "Put Your Client CodeHere!"
        String token;
        String redirect_uri = "Insert The Redirect URI Here";
        String token;


        Uri uri = getIntent().getData();

        final String baseUrl = "https://www.proto.utwente.nl/";
        if(uri != null && uri.toString().startsWith(redirect_uri)) {
//            //AccountManager am = new AccountManager();

            code = uri.getQueryParameter("code");
            Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "We have data:      " + code, Toast.LENGTH_SHORT).show();

            Uri tokenRequest = Uri.parse("https://www.proto.utwente.nl/oauth/token"+"?grant_type="+ "client_credentials" + "&client_id="+clientID+"&client_secret="+clientSecret+"&redirect_uri="+ redirect_uri + "&code="+code);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SAProtoClient service = retrofit.create(SAProtoClient.class);
            Call<AuthorizationToken> userToken = service.getAuthToken("authorization_code", clientID,clientSecret,redirect_uri,code);

            userToken.enqueue(new Callback<AuthorizationToken>() {
                                  @Override
                                  public void onResponse(Call<AuthorizationToken> call, Response<AuthorizationToken> response) {
                                      if(response.isSuccessful()){
                                          Toast.makeText(LoginActivity.this, "Getting the token was a success:" + response.body(), Toast.LENGTH_SHORT).show();
                                          //startMainActivity();
                                          // addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                                      }
                                      else{
                                          try{
                                              JSONObject jObjError = new JSONObject(response.errorBody().string());
                                              Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_SHORT).show();

                                              // mAccountManager.addAccount(null,null,null, null, null, null, null);
                                          }catch (Exception e){
                                              Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                          }
                                      }
                                  }

                                  @Override
                                  public void onFailure(Call<AuthorizationToken> call, Throwable t) {
                                      Toast.makeText(LoginActivity.this, "The call was a failure!", Toast.LENGTH_SHORT).show();
                                  }
                              }
            );
        }
    }

    public void startMainActivity(){
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}
