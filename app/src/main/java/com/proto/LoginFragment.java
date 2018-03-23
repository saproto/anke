package com.proto;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private static String CLIENT_ID = "74ce6f68fae842d192f0a079926a11d3";
    private static String CLIENT_SECRET = "";
    private static String REDIRECT_URI = "http://localhost";
    private static String GRANT_TYPE = "authorization_code";
    private static String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static String OAUTH_URL = "https://accounts.spotify.com/authorize";
    private static String OAUTH_SCOPE = "";

    SharedPreferences pref;

    WebView webView;

    private String accessToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);

        final Activity activity = getActivity();

        pref = getActivity().getSharedPreferences("AppPref", MODE_PRIVATE);

        final Dialog auth_dialog;

        auth_dialog = new Dialog(getActivity());
        auth_dialog.setContentView(R.layout.auth_dialog);
        webView = (WebView)auth_dialog.findViewById(R.id.webv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(OAUTH_URL+"?redirect_uri="+
                REDIRECT_URI+"&response_type=code&client_id="+
                CLIENT_ID+"&scope="+OAUTH_SCOPE);
        webView.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;
            Intent resultIntent = new Intent();

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            String authCode;

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Activity activity = getActivity();
                if (url.contains("?code=") && !authComplete) {
                    // Fetch auth code
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    Log.i("", "CODE : " + authCode);
                    authComplete = true;

                    // Send intent back to main activity with auth code
                    resultIntent.putExtra("code", authCode);
                    activity.setResult(Activity.RESULT_OK, resultIntent);
                    activity.setResult(Activity.RESULT_CANCELED);

                    // Save auth code
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Code", authCode);
                    editor.commit();

                    auth_dialog.dismiss();
                    new LoginFragment.TokenGet().execute();
                    Toast.makeText(activity.getApplicationContext(),
                                    "Authorization Code is: " + authCode,
                                    Toast.LENGTH_SHORT).show();
                } else if (url.contains("error=access_denied")) {
                    Log.i("", "ACCESS_DENIED_HERE");

                    resultIntent.putExtra("code", authCode);
                    authComplete = true;
                    activity.setResult(Activity.RESULT_CANCELED, resultIntent);
                    Toast.makeText(activity.getApplicationContext(), "Error Occured",
                                    Toast.LENGTH_SHORT).show();

                    auth_dialog.dismiss();
                }
            }
        });
        auth_dialog.show();
        auth_dialog.setTitle("Authorize Proto Account");
        auth_dialog.setCancelable(true);

        return root;
    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog dialog;
        String code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Contacting Spotify ...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            code = pref.getString("Code", "");
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken parser = new GetAccessToken();
            JSONObject json = parser.getToken(TOKEN_URL, code, CLIENT_ID, CLIENT_SECRET,
                                              REDIRECT_URI, GRANT_TYPE);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            dialog.dismiss();
            if (json != null) {
                try {
                    String token = json.getString("access_token");
                    String expireDate = json.getString("expires_in");
                    String refreshToken = json.getString("refresh_token");

                    Log.d("Token Access", token);
                    Log.d("Expire", expireDate);
                    Log.d("Refresh", refreshToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Network Error",
                                Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }
}
