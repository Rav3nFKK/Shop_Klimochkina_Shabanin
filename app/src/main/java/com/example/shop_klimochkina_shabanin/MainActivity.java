package com.example.shop_klimochkina_shabanin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addBt, clearBt;
    EditText NameET, CostET;
    TextView cart;
    DBhelper dBhelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    float costS = 0;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBt = (Button) findViewById(R.id.AddBut);


        clearBt = (Button) findViewById(R.id.ClearBut);
        addBt.setOnClickListener(this);
        clearBt.setOnClickListener(this);

        NameET = (EditText) findViewById(R.id.NameET);
        CostET = (EditText) findViewById(R.id.Cost);


        cart = (TextView) findViewById(R.id.cart);
        cart.setText("0");
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast toast;
                toast = Toast.makeText(getApplicationContext(),
                        "Вы потратили из своего кошеля: " + cart.getText(), Toast.LENGTH_SHORT);
                toast.show();
                cart.setText("0");
                costS =0;
            }
        });

        dBhelper = new DBhelper(this);
        database = dBhelper.getWritableDatabase();
        UpdateTable();

    }

    public void UpdateTable() {
        Cursor cursor = database.query(DBhelper.TABLE_PRODUCT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBhelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBhelper.KEY_TITLE);
            int costIndex = cursor.getColumnIndex(DBhelper.KEY_COST);

            TableLayout layOutput = findViewById(R.id.TabLay);
            layOutput.removeAllViews();
            do {
                TableRow TBrow = new TableRow(this);
                TBrow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                TBrow.addView(outputID);

                TextView outputNm = new TextView(this);
                params.weight = 2.0f;
                outputNm.setLayoutParams(params);
                outputNm.setText(cursor.getString(nameIndex));
                TBrow.addView(outputNm);


                TextView outCost = new TextView(this);
                params.weight = 1.0f;
                outCost.setLayoutParams(params);
                outCost.setText(cursor.getString(costIndex));
                TBrow.addView(outCost);


                Button clearOnEl = new Button(this);
                clearOnEl.setOnClickListener(this);
                params.weight = 1.0f;
                clearOnEl.setLayoutParams(params);
                clearOnEl.setText("Удоли");
                clearOnEl.setId(cursor.getInt(idIndex));
                TBrow.addView(clearOnEl);

                Button Buyof = new Button(this);
                Buyof.setOnClickListener(this);
                params.weight = 1.0f;
                Buyof.setLayoutParams(params);
                Buyof.setText("Купить");
                Buyof.setId(cursor.getInt(idIndex));
                TBrow.addView(Buyof);

                layOutput.addView(TBrow);


            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;

        switch (v.getId()) {


            case R.id.AddBut:
                String name = NameET.getText().toString();
                String cost = CostET.getText().toString();


                contentValues = new ContentValues();
                if (name.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Необходимо название товара...", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    contentValues.put(DBhelper.KEY_TITLE, name);
                    contentValues.put(DBhelper.KEY_COST, cost);

                    database.insert(DBhelper.TABLE_PRODUCT, null, contentValues);
                }

                UpdateTable();
                NameET.setText(null);
                CostET.setText(null);
                break;


            case R.id.ClearBut:
                database.delete(DBhelper.TABLE_PRODUCT, null, null);
                TableLayout layOutput = findViewById(R.id.TabLay);
                layOutput.removeAllViews();
                costS = 0;
                cart.setText("");
                break;


            default:
                if (((Button) v).getText() == "Удоли") {

                    View outputDBRow = (View) v.getParent();
                    ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                    outputDB.removeView(outputDBRow);
                    outputDB.invalidate();

                    database.delete(DBhelper.TABLE_PRODUCT, DBhelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                    contentValues = new ContentValues();
                    Cursor cursorupd = database.query(DBhelper.TABLE_PRODUCT, null, null, null, null, null, null);
                    if (cursorupd.moveToFirst()) {
                        int idIndex = cursorupd.getColumnIndex(DBhelper.KEY_ID);
                        int nameIndex = cursorupd.getColumnIndex(DBhelper.KEY_TITLE);
                        int costIndex = cursorupd.getColumnIndex(DBhelper.KEY_COST);
                        int realId = 1;
                        do {
                            if (cursorupd.getInt(idIndex) > realId) {
                                contentValues.put(dBhelper.KEY_ID, realId);
                                contentValues.put(DBhelper.KEY_TITLE, cursorupd.getString(nameIndex));
                                contentValues.put(DBhelper.KEY_COST, cursorupd.getString((costIndex)));
                                database.replace(DBhelper.TABLE_PRODUCT, null, contentValues);
                            }
                            realId++;
                        } while (cursorupd.moveToNext());
                        if (cursorupd.moveToLast() && v.getId() != realId) {
                            database.delete(DBhelper.TABLE_PRODUCT, DBhelper.KEY_ID + " = ?", new String[]{cursorupd.getString(idIndex)});
                        }
                    }
                } else if (((Button) v).getText() == "Купить") {


                    Cursor c = database.query("contacts", null, "_id = ?", new String[]{String.valueOf(v.getId())}, null, null, null);
                    int t = c.getColumnIndex(DBhelper.KEY_COST);
                    c.moveToFirst();
                    float s = c.getFloat(t);
                    c.close();
                    costS += s;
                    cart.setText(String.valueOf(costS));
                }
                break;
        }
    }
}