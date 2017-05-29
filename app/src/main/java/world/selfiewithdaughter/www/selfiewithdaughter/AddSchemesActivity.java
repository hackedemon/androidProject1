package world.selfiewithdaughter.www.selfiewithdaughter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class AddSchemesActivity extends AppCompatActivity {

    String headerString;
    String descriptionString;
    ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schemes);

        final EditText header = (EditText) findViewById(R.id.schemeHeading);
        final EditText description = (EditText) findViewById(R.id.schemeDescription);
        final Button button = (Button) findViewById(R.id.addSchemeButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                headerString = header.getText().toString();
                descriptionString = description.getText().toString();

                if (headerString.matches("")) {
                    new AlertDialog.Builder(AddSchemesActivity.this)
                            .setTitle("Invalid Header")
                            .setMessage("Header cannot be empty!")
                            .show();
                } else if (descriptionString.matches("")) {
                    new AlertDialog.Builder(AddSchemesActivity.this)
                            .setTitle("Invalid Description")
                            .setMessage("Description cannot be empty!")
                            .show();
                } else {
                    // connect to server
                    new JsonTask().execute("http://ratofy.xyz/api.php", header.getText().toString(), description.getText().toString());
                }
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(AddSchemesActivity.this);
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

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("header", params[1])
                        .appendQueryParameter("description", params[2]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

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
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("response").matches("success")) {
                    EditText header = (EditText) findViewById(R.id.schemeHeading);
                    EditText description = (EditText) findViewById(R.id.schemeDescription);
                    header.setText(null);
                    description.setText(null);
                    Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(AddSchemesActivity.this)
                            .setTitle("Error")
                            .setMessage("An error occurred. Please try again")
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
