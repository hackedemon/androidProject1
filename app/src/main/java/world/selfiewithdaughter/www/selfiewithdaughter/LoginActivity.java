package world.selfiewithdaughter.www.selfiewithdaughter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                } else if (passString.matches("")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Invalid Password")
                            .setMessage("Password cannot be empty!")
                            .show();
                } else {
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        if (extras.containsKey("ID")) {
                            new JsonTask().execute("http://selfiewithdaughter.world/admin.php", Integer.toString(extras.getInt("ID")),
                                    user.getText().toString(), pass.getText().toString());
                        }
                    } else
                        // connect to server
                        new JsonTask().execute("http://selfiewithdaughter.world/admin.php", user.getText().toString(), pass.getText().toString());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go back
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                connection.setDoInput(true);
                connection.setDoOutput(true);

                if (params[1].matches("-?\\d+(\\.\\d+)?")) {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", params[1])
                            .appendQueryParameter("username", params[2])
                            .appendQueryParameter("pass", params[3]);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                } else {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("username", params[1])
                            .appendQueryParameter("pass", params[2]);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                }

                connection.connect();

                if (connection.getResponseCode() == 200) {
                    inputStream = connection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else
                    jsonResponse = "{ 'response' : 'serverError' }";

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
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("response").matches("success")) {
                    if (getIntent().hasExtra("ID")) {
                        Log.e("has id","true");
                        Toast.makeText(LoginActivity.this,"Deleted Successfully!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this,GovtSchemesActivity.class));
                    } else
                    startActivity(new Intent(LoginActivity.this, AddSchemesActivity.class));
                } else if (jsonObject.getString("response").matches("error")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Error")
                            .setMessage("Incorrect username or password!")
                            .show();
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Error")
                            .setMessage("An error occurred. Please try again!")
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
