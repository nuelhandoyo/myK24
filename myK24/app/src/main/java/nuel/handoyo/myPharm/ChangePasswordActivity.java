package nuel.handoyo.myPharm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    Button ton1, ton2;
    EditText text1, text2, text3;
    public static MainActivity ma;
    SharedPreferences prefx;

    String icode,iname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbHelper = new DatabaseHelper(this);
        text1 = (EditText) findViewById(R.id.edtText_passOld);
        text2 = (EditText) findViewById(R.id.edtText_passNew);
        text3 = (EditText) findViewById(R.id.edtText_passConf);

        ton1 = (Button) findViewById(R.id.button1);
        ton2 = (Button) findViewById(R.id.button2);

        Intent nowIntent = getIntent();
        icode = nowIntent.getStringExtra("code");

        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String strPassOld = text1.getText().toString();
                String strPassNew = text2.getText().toString();
                String strPassConf = text3.getText().toString();

                if(dbHelper.checkEmpty(strPassOld) == true && dbHelper.checkEmpty(strPassNew) == true && dbHelper.checkEmpty(strPassConf) == true){
                    if (strPassNew.matches("^[a-zA-Z0-9]*$") && strPassNew.length() > 5) {
                        if (strPassNew.equals(strPassConf)) {
                            if (!strPassNew.equals(strPassOld)) {
                                Boolean up = dbHelper.changePassword(icode, strPassNew);
                                if (up == true) {
                                    Toast.makeText(getApplicationContext(), "Ubah Password Berhasil", Toast.LENGTH_LONG).show();
//                                    Log.i("xyz", "onItemClick: a");
                                    MainActivity.ma.doLogout();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Ubah Password Gagal", Toast.LENGTH_LONG).show();
//                                    Log.i("xyz", "onItemClick: b");
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Password baru tidak boleh sama dengan password lama", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Password harus berisi huruf dan angka dan lebih dari 5 karakter", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                }

//
            }
        });
        ton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
            }
        });
    }
}