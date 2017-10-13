package com.androidtutorialpoint.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by sithi on 10/9/2017.
 */

public class TermsActivity extends AppCompatActivity {

    private CheckBox chk1, chk2, chk3;
    private Button btnDisplay;
    private Bundle bundle;
    ProgressDialog progressDialog;
    private static final String URL_FOR_REGISTRATION = "http://jury.herokuapp.com/register";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        this.bundle= getIntent().getExtras();
        addListenerOnChkIos();
        addListenerOnButton();

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    public void addListenerOnChkIos() {

        chk1 = (CheckBox) findViewById(R.id.chk1);

        chk1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {

                }

            }
        });

    }

    public void addListenerOnButton() {

        chk1 = (CheckBox) findViewById(R.id.chk1);
        chk2 = (CheckBox) findViewById(R.id.chk2);
        chk3 = (CheckBox) findViewById(R.id.chk3);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (chk1.isChecked() && chk2.isChecked() && chk3.isChecked()) {
                    registerUser(bundle.getString("username"),bundle.getString("email"),bundle.getString("password"));
                }else{
                    Toast.makeText(getApplicationContext(), "Should agree on All the terms to Register", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void registerUser(final String name,  final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();
        JSONObject request = new JSONObject();
        try
        {
            request.put("username", name);
            request.put("email", email);
            request.put("password", password);
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, URL_FOR_REGISTRATION, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE",response.toString());
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Hi " + name +", You are successfully Added!", Toast.LENGTH_SHORT).show();
                        // Launch login activity
                        Intent intent = new Intent(
                                TermsActivity.this,
                                InstructionsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            try {
                                JSONObject obj = new JSONObject(jsonError);
                                String errorType= obj.getString("error");
                                Toast.makeText(getApplicationContext(),errorType, Toast.LENGTH_LONG).show();

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + jsonError + "\"");
                            }
                        }

                        hideDialog();
                    }
                });
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


}