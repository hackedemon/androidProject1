package world.selfiewithdaughter.www.selfiewithdaughter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TwitterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        TextView userName = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        userName.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
    }

    public void buttonClick(View view) {
        EditText message = (EditText) findViewById(R.id.message);
        String encodedTweetText = message.getText().toString();
        String url = "https://twitter.com/intent/tweet?text=%40narendramodi%20" + encodedTweetText + "&hashtags=selfieWithDaughter";
        Intent i = new Intent(this, TwitterWeb.class);
        i.putExtra("URL", url);
        startActivity(i);
    }
}
