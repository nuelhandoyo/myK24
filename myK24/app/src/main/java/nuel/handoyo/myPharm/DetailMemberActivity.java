package nuel.handoyo.myPharm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailMemberActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    Button ton2;
    TextView text1, text2, text3, text4, text5, text6;
    String icode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_member);

        dbHelper = new DatabaseHelper(this);
        text1 = (TextView) findViewById(R.id.textViewAT);
        text2 = (TextView) findViewById(R.id.textViewBT);
        text3 = (TextView) findViewById(R.id.textViewCT);
        text4 = (TextView) findViewById(R.id.textViewDT);
        text5 = (TextView) findViewById(R.id.textViewFT);
        text6 = (TextView) findViewById(R.id.textViewGT);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Intent nowIntent = getIntent();
        icode = nowIntent.getStringExtra("code");
        cursor = db.rawQuery("SELECT * FROM users WHERE membercode = '" +
                icode + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(1).toString());
            text2.setText(cursor.getString(6).toString());
            text3.setText(cursor.getString(2).toString());
            text4.setText(cursor.getString(3).toString());
            text5.setText(cursor.getString(4).toString());
            text6.setText(cursor.getString(5).toString());
        }
        ton2 = (Button) findViewById(R.id.button1);
        ton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
}