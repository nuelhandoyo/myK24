package nuel.handoyo.myPharm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.widget.DatePicker;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button login, register;
    EditText username, password, passwordConf;
    EditText fullname, birthdate , address, referralcode; //gender,
    RadioGroup rgg;
    RadioButton rbm,rbf;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button btDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        fullname = (EditText)findViewById(R.id.edtText_fullname);
        birthdate = (EditText)findViewById(R.id.edtText_birthdate);
        address = (EditText)findViewById(R.id.edtText_address);
        referralcode = (EditText)findViewById(R.id.edtText_refCode);

        username = (EditText)findViewById(R.id.edtText_usernameRegist);
        password = (EditText)findViewById(R.id.edtText_passwordRegist);
        passwordConf = (EditText)findViewById(R.id.edtText_passwordConfRegist);
        login = (Button)findViewById(R.id.btn_loginRegist);
        register = (Button)findViewById(R.id.btn_registerRegist);

        rgg = (RadioGroup)findViewById(R.id.rg_gender);
        rbm = (RadioButton)findViewById(R.id.gm);
        rbf = (RadioButton)findViewById(R.id.gf);

        //login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });


        //register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFullname = fullname.getText().toString();
                String strBirthdate = birthdate.getText().toString();
                String strAddress = address.getText().toString();
                String strReferralCode = referralcode.getText().toString();

                String strUsername = username.getText().toString();
                String strPassword = password.getText().toString();
                String strPasswordConf = passwordConf.getText().toString();

                String strGender = "";
                int selectedId = rgg.getCheckedRadioButtonId();

                if (selectedId == rbm.getId()){
                    strGender="L";
                } else if (selectedId == rbf.getId()){
                    strGender="P";
                }

                if(db.checkEmpty(strFullname) == true && db.checkEmpty(strBirthdate) == true && db.checkEmpty(strGender) == true && db.checkEmpty(strAddress) == true && db.checkEmpty(strUsername) == true && db.checkEmpty(strPassword) == true && db.checkEmpty(strPasswordConf) == true){
                    if (strPassword.matches("^[a-zA-Z0-9]*$") && strPassword.length() > 5) {
                        if (strPassword.equals(strPasswordConf)) {
                            Boolean validUsername = db.validateNewUsername(strUsername);
                            if(validUsername == true) {
                                Boolean daftar = db.insertUser(strFullname, strBirthdate, strGender, strAddress, strReferralCode, strUsername, strPassword);
                                if (daftar == true) {
                                    Toast.makeText(getApplicationContext(), "Daftar Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Daftar Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Username sudah didaftarkan", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password Tidak Cocok", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Password harus berisi huruf dan angka dan berisi minimal 6 karakter", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        btDatePicker = (Button) findViewById(R.id.bt_datepicker);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });


    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                birthdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

}
