package nuel.handoyo.myPharm;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UpdateMemberActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    Button ton1, ton2;
    EditText text1, text2, text3, text4, text5, text6;
    String membercodex;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button btDatePicker;

    RadioGroup rgg;
    RadioButton rbm,rbf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member);

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

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM users WHERE membercode = '" +
                getIntent().getStringExtra("code") + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(2).toString());
            text2.setText(cursor.getString(3).toString());
            String gndr = cursor.getString(4).toString();
            if(gndr.equals("L")){
                rbm.setChecked(true);
                rbf.setChecked(false);
            }else if(gndr.equals("P")){
                rbm.setChecked(false);
                rbf.setChecked(true);
            }
            text4.setText(cursor.getString(5).toString());
            text6.setText(cursor.getString(8).toString());
            membercodex = cursor.getString(1).toString();
        }
        // daftarkan even onClick pada btnSimpan
        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String strGender = "";
                int selectedId = rgg.getCheckedRadioButtonId();

                if (selectedId == rbm.getId()){
                    strGender="L";
                } else if (selectedId == rbf.getId()){
                    strGender="P";
                }

                db.execSQL("update users set fullname='"+
                        text1.getText().toString() +"', birthdate='" +
                        text2.getText().toString()+"', gender='"+
                        strGender +"', address='" +
                        text4.getText().toString() +"', referralcode='" +
                        text6.getText().toString() + "' where membercode='" +
                        membercodex+"'");
                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                MainActivity.ma.RefreshList();
                finish();
            }
        });
        ton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
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