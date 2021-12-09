package com.example.shop_klimochkina_shabanin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActvActivity extends AppCompatActivity implements View.OnClickListener {

    EditText logintxt, passwordtxt;
    Button autobtn, regbtn;
    DBhelper dBhelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actv);
        logintxt = findViewById(R.id.login);
        passwordtxt = findViewById(R.id.password);
        autobtn = findViewById(R.id.auto);
        regbtn = findViewById(R.id.reg);

        autobtn.setOnClickListener(this);
        regbtn.setOnClickListener(this);

        dBhelper = new DBhelper(this);
        database = dBhelper.getWritableDatabase();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auto:
                Cursor logCursor = database.query(DBhelper.TABLE_USERS, null, null, null, null, null, null);

                boolean logged = false;
                if (logCursor.moveToFirst()) {
                    int usernameindex = logCursor.getColumnIndex(DBhelper.KEY_LOGIN);
                    int passwordindex = logCursor.getColumnIndex(DBhelper.KEY_PASSWORD);

                    do {
                        if (logintxt.getText().toString().equals(logCursor.getString(usernameindex)) && passwordtxt.getText().toString().equals(logCursor.getString(passwordindex))) {
                            if (logintxt.getText().toString().equals("admin"))
                                startActivity(new Intent(this, Activity2.class));
                            else
                                startActivity(new Intent(this, MainActivity.class));

                            logged = true;
                            break;
                        }
                    } while (logCursor.moveToNext());
                }
                logCursor.close();
                if (!logged)
                    Toast.makeText(this, "Данный пользователь не зарегистрирован.", Toast.LENGTH_LONG).show();

                break;

            case R.id.reg:
                Cursor signcursor = database.query(DBhelper.TABLE_USERS, null, null, null, null, null, null);

                boolean finded = false;
                if (signcursor.moveToFirst()) {
                    int usernameIndex = signcursor.getColumnIndex(DBhelper.KEY_LOGIN);
                    do {
                        if (logintxt.getText().toString().equals(signcursor.getString(usernameIndex))) {
                            Toast.makeText(this, "Данный пользователь уже зарегестрирован", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    } while (signcursor.moveToNext());
                }
                if (!finded) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBhelper.KEY_LOGIN, logintxt.getText().toString());
                    contentValues.put(DBhelper.KEY_PASSWORD, passwordtxt.getText().toString());
                    database.insert(DBhelper.TABLE_USERS, null, contentValues);
                    Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
                }
                signcursor.close();
                break;
        }
    }
}