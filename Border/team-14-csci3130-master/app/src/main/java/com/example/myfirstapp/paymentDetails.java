package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class paymentDetails extends AppCompatActivity {
    TextView txtId, txtAmount, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId=(TextView)findViewById(R.id.txtId);
        txtAmount=(TextView)findViewById(R.id.txtAmount);
        txtStatus=(TextView)findViewById(R.id.txtStatus);

        Intent intent=getIntent();

        try{
            JSONObject jsonObject=new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try{
            txtId.setText(response.getString("id"));
            txtAmount.setText(response.getString(String.format("$%s",paymentAmount)));
            txtStatus.setText(response.getString("Status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}