package nuel.handoyo.myPharm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DatabaseHelper dbcenter;
    public static MainActivity ma;
    SharedPreferences pref;

    String icode,iname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        Boolean checkSession = db.checkSession("ada");
        if (checkSession == false) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });

        ma = this;
        dbcenter = new DatabaseHelper(this);
        RefreshList();
    }

    public void doLogout(){
        Boolean updtSession = db.upgradeSession("kosong", 1);
        if (updtSession == true) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }


    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM users where referralcode<>'myk24admin' and sign=1", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = (ListView) findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
//                Log.i("xyz", "yoo: "+daftar[arg2]);
                final String selection = daftar[arg2]; //.getItemAtPosition(arg2).toString();
                final CharSequence[] dialogitem = {"Lihat Member", "Update Member", "Reset Password","Hapus Member"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), DetailMemberActivity.class);
                                i.putExtra("code", selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getApplicationContext(), UpdateMemberActivity.class);
                                in.putExtra("code", selection);
                                startActivity(in);
                                break;
                            case 2:
                                dbcenter.resetPassword(selection);
                                Toast.makeText(getApplicationContext(), "Berhasil direset", Toast.LENGTH_SHORT).show();
                                RefreshList();
                                break;
                            case 3:
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("update users set sign=0 where membercode='"+selection+"'");
//                                db.execSQL("delete from users where nama = '" + selection + "'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();

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
//            Log.i("xyz", "ole "+icode+"|");
            startActivity(in);
            return true;
        }else if (id == R.id.optionabout) {
            Intent inz = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            inz.putExtra("code",icode);

//            Log.i("xyz", "oles "+icode+"|"+codenow);
            startActivity(inz);
            return true;
        }if (id == R.id.optionlogout) {
            doLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
