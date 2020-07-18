package nuel.handoyo.myPharm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import static nuel.handoyo.myPharm.R.id.txtname;

public class MemberActivity extends AppCompatActivity {

    DatabaseHelper db;
    public static MainActivity ma;
    SharedPreferences pref;

    String icode,iname;
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        Intent nowIntent = getIntent();
        icode = nowIntent.getStringExtra("code");
        iname = nowIntent.getStringExtra("name");

        text1 = (TextView) findViewById(txtname);
        text1.setText(iname);
//        Log.i("xyz", "onItemClick: "+iname);


        db = new DatabaseHelper(this);

        Boolean checkSession = db.checkSession("ada");
        if (checkSession == false) {
            Intent loginIntent = new Intent(MemberActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent nowIntent = getIntent();
        icode = nowIntent.getStringExtra("code");
        //noinspection SimplifiableIfStatement
        if (id == R.id.optionprofile) {
            Intent in = new Intent(getApplicationContext(), DetailMemberActivity.class);
            in.putExtra("code", icode);
            startActivity(in);
            return true;
        }else if (id == R.id.optionabout) {
            Intent inz = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            inz.putExtra("code",icode);
            startActivity(inz);
            return true;
        }if (id == R.id.optionlogout) {
            MainActivity.ma.doLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}