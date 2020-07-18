package nuel.handoyo.myPharm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button login, register;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        username = (EditText)findViewById(R.id.edtText_username);
        password = (EditText)findViewById(R.id.edtText_password);
        login = (Button)findViewById(R.id.btn_login);
        register = (Button)findViewById(R.id.btn_register);

        //register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        //login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = username.getText().toString();
                String strPassword = password.getText().toString();
                String masuk = db.checkLogin(strUsername, strPassword);
                if (masuk != "0") {
                    Boolean updateSession = db.upgradeSession("ada", 1);
                    if (updateSession == true) {
//                        Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                        String strX = masuk.substring(0,1);
                        String strA = masuk.substring(1,13);
                        String strB = masuk.substring(13);

//                        Log.i("xyz", "define: "+strX+"|"+strA+"|"+strB);
                        if(strX.equals("0")){
                            Intent mainIntentx = new Intent(LoginActivity.this, MemberActivity.class);
                            mainIntentx.putExtra("code",strA);
                            mainIntentx.putExtra("name",strB);
                            startActivity(mainIntentx);

//                            Log.i("xyz", "onItemClick: "+masuk);
                            finish();
                        }else{
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.putExtra("code",strA);
                            mainIntent.putExtra("name",strB);
                            startActivity(mainIntent);
//                            Log.i("xyz", "onItemClick: "+masuk);
                            finish();
                        }

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Masuk Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
