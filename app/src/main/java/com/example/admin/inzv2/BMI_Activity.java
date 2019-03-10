package com.example.admin.inzv2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BMI_Activity extends AppCompatActivity {

    private EditText mTallView;
    private EditText mWeightView;
    private TextView mResoultView;
    private CheckBox mMencheckBox;
    private CheckBox mWomencheckBox;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = (Button) findViewById(R.id.obliczbutton);

        mTallView = (EditText) findViewById(R.id.talleditText);
        mWeightView = (EditText) findViewById(R.id.weighteditText);
        mResoultView = (TextView) findViewById(R.id.resulttextView);

        mMencheckBox = (CheckBox)findViewById(R.id.MencheckBox );
        mWomencheckBox = (CheckBox)findViewById(R.id.WomencheckBox );

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                obliczBMI();
                // Perform action on click
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();
    }

    private void obliczBMI(){

        if(!mTallView.getText().toString().equals("") & !mWeightView.getText().toString().equals(""))
        {

            double tall=0;
            double weight=0;
            double bmi=0;
            String result="";

            tall  = Double.parseDouble(mTallView.getText().toString());
            weight  = Double.parseDouble(mWeightView.getText().toString());

            bmi= (weight*10000)/(tall*tall);


        if (bmi<16)
            result="wygłodzenie";
        else if (bmi>16 & bmi<=17)
            result="wychudzenie";
        else if (bmi>17 & bmi<=18.5)
            result="niedowaga";
        else if (bmi>18.5 & bmi<=25)
            result="wartość prawidłowa";
        else if (bmi>25 & bmi<30)
            result="nadwaga";
        else if (bmi>30 & bmi<=35)
            result="I stopień otyłości";
        else if (bmi>35 & bmi<=40)
            result="II stopień otyłości";
        else if (bmi>40)
            result="otyłość skrajna";

            mResoultView.setText(String.format("%." + 2 + "f", bmi) + "  " + result);

        }
        else
        {
            Toast.makeText(this, "Wprowadz wzrost i wagę!!! ",
                    Toast.LENGTH_SHORT).show();

        }

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.MencheckBox:
                mWomencheckBox.setChecked(false);
                break;
            case R.id.WomencheckBox:
                    mMencheckBox.setChecked(false);

                break;
            // TODO: Veggie sandwich
        }
    }

}