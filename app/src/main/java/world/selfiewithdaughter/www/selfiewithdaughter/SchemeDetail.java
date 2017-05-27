package world.selfiewithdaughter.www.selfiewithdaughter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SchemeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_detail);

        TextView header = (TextView) findViewById(R.id.detail_header);
        TextView description = (TextView) findViewById(R.id.detail_description);

        Bundle b = getIntent().getExtras();

        header.setText(b.getString("HEADER"));
        description.setText(b.getString("DESCRIPTION"));
    }
}
