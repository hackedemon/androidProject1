package world.selfiewithdaughter.www.selfiewithdaughter;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SchemeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView header = (TextView) findViewById(R.id.detail_header);
        TextView description = (TextView) findViewById(R.id.detail_description);

        final Bundle b = getIntent().getExtras();

        header.setText(b.getString("HEADER"));
        description.setText(b.getString("DESCRIPTION"));
        Log.e("id value",Integer.toString(b.getInt("ID")));

        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SchemeDetail.this, LoginActivity.class);
                i.putExtra("ID", b.getInt("ID"));
                startActivity(i);
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
}