package world.selfiewithdaughter.www.selfiewithdaughter;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pd;
    String userString;
    String passString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = (Button) findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText user = (EditText) findViewById(R.id.loginUsername);
                EditText pass = (EditText) findViewById(R.id.loginPassword);

                userString = user.getText().toString();
                passString = pass.getText().toString();

                if (userString.matches("")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Invalid Username")
                            .setMessage("Username cannot be empty!")
                            .show();
                } else if (passString.matches("")){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Invalid Password")
                            .setMessage("Password cannot be empty!")
                            .show();
                } else {
                    // connect to server
                    new JsonTask().execute("http://ratofy.xyz/admin.php", user.getText().toString(), pass.getText().toString());
                }
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            String jsonResponse = "";

            if (params[0] == null) {
                return jsonResponse;
            }

            HttpURLConnection connection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.addRequestProperty("username",userString);
                connection.addRequestProperty("pass",passString);
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    inputStream = connection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while ( line != null ) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Success")
                    .setMessage("Login Successful!")
                    .show();
        }
    }
}