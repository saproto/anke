package com.proto.oauth;



import android.content.Intent;



import android.net.Uri;



import android.os.Bundle;



import android.support.v7.app.AppCompatActivity;



import android.view.View;



import android.widget.Toast;



import com.proto.MainActivity;



import com.proto.R;



import com.proto.home.ArticleEntry;



import com.proto.service.RetroFitServiceGenerator;



//import com.proto.user.ProfilePicture;



import com.proto.user.User;



import org.json.JSONObject;



import retrofit2.Call;



import retrofit2.Callback;



import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {



    SAProtoClient service;



    User user;



    @Override



    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);



        service = RetroFitServiceGenerator.createService(SAProtoClient.class);



        findViewById(R.id.oauthButton).setOnClickListener(new View.OnClickListener() {



            @Override



            public void onClick(View v) {



                authenticateUser();



            }



        });



        findViewById(R.id.protoinkButton).setOnClickListener(new View.OnClickListener() {



            @Override



            public void onClick(View v) {



                getArticles();



            }



        });



    }



    public void getArticles(){



        Call<ArticleEntry[]> articles = service.getProtoInkArticles();



        articles.enqueue(new Callback<ArticleEntry[]>() {



            @Override



            public void onResponse(Call<ArticleEntry[]> call, Response<ArticleEntry[]> response) {



                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();



            }



            @Override



            public void onFailure(Call<ArticleEntry[]> call, Throwable t) {



                Toast.makeText(LoginActivity.this, "Deze natuurlijk ook nog", Toast.LENGTH_SHORT).show();



            }



        });



    }



    public void authenticateUser(){



        //TODO Dennis doe deze eens goed wegwerken



        String clientID = "2";



        String clientSecret = "n9aKesBbxYAyV1oRyO6gPwo4nqF7CKtneubsEz6M";



        String token;



        String redirect_uri = "proto.utwente.nl://callback";



        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.proto.utwente.nl/oauth/authorize"+"?client_id="+clientID+"&response_type=code"+"&scope=*"+"&redirect_uri="+ redirect_uri));



        startActivity(intent);



    }



    @Override



    public void onResume(){



        super.onResume();



        String code;



        String clientID = "2";



        String clientSecret = "n9aKesBbxYAyV1oRyO6gPwo4nqF7CKtneubsEz6M";



        String redirect_uri = "proto.utwente.nl://callback";



        Uri uri = getIntent().getData();



        final String baseUrl = "https://www.proto.utwente.nl/";



        if(uri != null && uri.toString().startsWith(redirect_uri)) {



//      //AccountManager am = new AccountManager();



            code = uri.getQueryParameter("code");



            //Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();



            //Toast.makeText(this, "We have data:   " + code, Toast.LENGTH_SHORT).show();



            Uri tokenRequest = Uri.parse("https://www.proto.utwente.nl/oauth/token"+"?grant_type="+ "client_credentials" + "&client_id="+clientID+"&client_secret="+clientSecret+"&redirect_uri="+ redirect_uri + "&code="+code);



            //final SAProtoClient service = RetroFitServiceGenerator.createService(SAProtoClient.class);



            Call<AuthorizationToken> userToken = service.getAuthToken("authorization_code", clientID,clientSecret,redirect_uri,code);



            userToken.enqueue(new Callback<AuthorizationToken>() {



                                  @Override



                                  public void onResponse(Call<AuthorizationToken> call, Response<AuthorizationToken> response) {



                                      if(response.isSuccessful()){



                                         // Toast.makeText(LoginActivity.this, "Getting the token was a success:" + response.body(), Toast.LENGTH_SHORT).show();



                                          AuthorizationToken token = response.body();



                                          // Toast.makeText(LoginActivity.this, "Token: " + token.getAccessToken(), Toast.LENGTH_SHORT).show();



                                          //getUserInfoPlane(service, token);



                                          getUserInfo(service, token);



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



    private void getUserInfo(SAProtoClient service, final AuthorizationToken token) {



        Call<User> userInfo =service.getUser("Bearer "+token.getAccessToken());



        userInfo.enqueue(new Callback<User>() {



            @Override



            public void onResponse(Call<User> User, Response<User> response) {



                if (response.isSuccessful()) {



                    //response.



                    //testWrite();



                    user = response.body();



                    //Toast.makeText(LoginActivity.this, "Getting hte user was a success", Toast.LENGTH_SHORT).show();



                    //getUserImage(user, token.getAccessToken());



                    startMainActivity();



                } else {



                    try {



                        JSONObject jObjError = new JSONObject(response.errorBody().string());



                        Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_SHORT).show();



                        // mAccountManager.addAccount(null,null,null, null, null, null, null);



                    } catch (Exception e) {



                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();



                    }



                }



            }



            @Override



            public void onFailure(Call<User> call, Throwable t) {



                Toast.makeText(LoginActivity.this, "Get a new thingy"+t.toString(), Toast.LENGTH_SHORT).show();



            }



        });



    }


/*
    public void getUserImage(final User user, String token){



        Call<ProfilePicture> test = service.test("Bearer "+token);



        test.enqueue(new Callback<ProfilePicture>() {



                         @Override



                         public void onResponse(Call<ProfilePicture> call, Response<ProfilePicture> response) {



                             user.setProfilePicture(response.body());



                             Toast.makeText(LoginActivity.this, "Wat krijg je allemaal binnen", Toast.LENGTH_SHORT).show();



                         }



                         @Override



                         public void onFailure(Call<ProfilePicture> call, Throwable t) {



                             Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();



                         }



                     }



        );



    /*Picasso



        .with(LoginActivity.this)



        .load("")



        .fetch(new com.squareup.picasso.Callback() {



          @Override



          public void onSuccess() {



            startMainActivity();



          }



          @Override



          public void onError() {



          }



        });*/



    //}



  /*public void testWrite(){



    SharedPreferences test = com.proto.service.SharedPreferencesFunctions.getSharedPreferences();



    SharedPreferences.Editor editor = test.edit();



    editor.putString("name", "Dennis");



    editor.apply();



    Toast.makeText(this, "The name="+test.getString("name",null), Toast.LENGTH_SHORT).show();



  }



  public void testRead(){



    SharedPreferences test = com.proto.service.SharedPreferencesFunctions.getSharedPreferences();



    test.getString("name", "");



  }*/



    public void startMainActivity(){



        Intent main = new Intent(this, MainActivity.class);



        main.putExtra("USER", user);



        startActivity(main);



        finish();



    }
}