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

    Button btnActTwo, BuyBtn;
    TextView cart;
    DBhelper dBhelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    Toast toast;
    float costS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnActTwo = (Button) findViewById(R.id.btnNewAct);
        BuyBtn = (Button) findViewById(R.id.Buybtn);
        btnActTwo.setOnClickListener(this);


        cart = (TextView) findViewById(R.id.cart);
        cart.setText("0");

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toast = Toast.makeText(getApplicationContext(),
                        "Вы потратили из своего кошеля: " + cart.getText(), Toast.LENGTH_SHORT);
                toast.show();
                cart.setText("0");
                costS = 0;
            }});

        BuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                toast = Toast.makeText(getApplicationContext(),
                        "Вы потратили из своего кошеля: " + cart.getText(), Toast.LENGTH_SHORT);
                toast.show();
                cart.setText("0");
                costS = 0;
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



                Button Buyof = new Button(this);
                Buyof.setOnClickListener(this);
                params.weight = 1.0f;
                Buyof.setLayoutParams(params);
                Buyof.setText("Добавить в корзину");
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




            case R.id.btnNewAct:
                Intent intent = new Intent(this, LoginActvActivity.class);
                startActivity(intent);
                break;


            default:

                if (((Button) v).getText() == "Добавить в корзину") {

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
