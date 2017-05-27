package world.selfiewithdaughter.www.selfiewithdaughter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AddSchemesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schemes);

        TextView tv = (TextView) findViewById(R.id.schemeHeading);

        tv.setText(getIntent().getStringExtra("response"));
    }
}
