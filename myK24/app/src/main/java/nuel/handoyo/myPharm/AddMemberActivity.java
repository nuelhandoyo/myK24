package nuel.handoyo.myPharm;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddMemberActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    Button ton1, ton2;
    EditText text1, text2, text3, text4, text5, text6;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button btDatePicker;

    RadioGroup rgg;
    RadioButton rbm,rbf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        dbHelper = new DatabaseHelper(this);
        text1 = (EditText) findViewById(R.id.edtText_fullname);
        text2 = (EditText) findViewById(R.id.edtText_birthdate);
        text4 = (EditText) findViewById(R.id.edtText_address);
        text5 = (EditText) findViewById(R.id.edtText_usernameRegist);
        text6 = (EditText) findViewById(R.id.edtText_refCode);
        ton1 = (Button) findViewById(R.id.button1);
        ton2 = (Button) findViewById(R.id.button2);

        rgg = (RadioGroup)findViewById(R.id.rg_gender);
        rbm = (RadioButton)findViewById(R.id.gm);
        rbf = (RadioButton)findViewById(R.id.gf);


        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String strFullname = text1.getText().toString();
                String strBirthdate = text2.getText().toString();
                String strAddress = text4.getText().toString();
                String strUsername = text5.getText().toString();
                String strReferralCode = text6.getText().toString();

                String strGender = "";
                int selectedId = rgg.getCheckedRadioButtonId();

                if (selectedId == rbm.getId()){
                    strGender="L";
                } else if (selectedId == rbf.getId()){
                    strGender="P";
                }

                Boolean daftar = dbHelper.insertUser(strFullname, strBirthdate, strGender, strAddress, strReferralCode, strUsername, strUsername);
                if (daftar == true) {
                    Toast.makeText(getApplicationContext(), "Daftar Berhasil", Toast.LENGTH_LONG).show();
                    Log.i("xyz", "onItemClick: a");

                } else {
                    Toast.makeText(getApplicationContext(), "Daftar Gagal", Toast.LENGTH_LONG).show();
                    Log.i("xyz", "onItemClick: b");
                }
                MainActivity.ma.RefreshList();
                finish();

            }
        });
        ton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Log.i("xyz", "onItemClick: errx");
                finish();
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

                text2.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}