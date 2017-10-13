package com.androidtutorialpoint.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sithi on 10/9/2017.
 */

public class GameActivity extends AppCompatActivity {

    private static SeekBar toxicBar;
    private static TextView questionDisplay;
    private String[] questions;
    private int questionID;
    private RadioGroup toxicityRadioGroup;
    ProgressDialog progressDialog;
    String Usertoken;
    private static final String URL_FOR_QS = "http://jury.herokuapp.com/api/next10_unanswered_questions";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        questionDisplay = (TextView)findViewById(R.id.textDisplay);
        Bundle bundle = getIntent().getExtras();
        String user = bundle.getString("username");
        String token = bundle.getString("Token");
        Log.d("99999999999999999999", user+token);
        this.Usertoken =token;
        toxicityRadioGroup = (RadioGroup) findViewById(R.id.toxicity_radio_group);

        // Allows you to interact with Fragments in an Activity
        FragmentManager fragmentManager = getFragmentManager();

        // beginTransaction() begins the FragmentTransaction which allows you to
        // add, attach, detach, hide, remove, replace, animate, transition or
        // show fragments
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        // The Configuration object provides device configuration info
        // http://developer.android.com/reference/android/content/res/Configuration.html
        Configuration configInfo = getResources().getConfiguration();


        if (toxicityRadioGroup.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked,toxic levels step 1
            // call the button clicked identifier method submitForm()
            FragmentOne fragmentOne = new FragmentOne();

            fragmentTransaction.replace(android.R.id.content,
                    fragmentOne);

        }
        else
        {
            // one of the radio buttons is checked,toxic level step 2
            FragmentTwo fragmentTwo = new FragmentTwo();

            fragmentTransaction.replace(android.R.id.content,
                    fragmentTwo);
        }

        // Depending on the screen orientation replace with the correct fragment
//        if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE){
//
//            FragmentOne fragmentOne = new FragmentOne();
//
//            fragmentTransaction.replace(android.R.id.content,
//                    fragmentOne);
//
//        } else {
//
//            FragmentTwo fragmentTwo = new FragmentTwo();
//
//            fragmentTransaction.replace(android.R.id.content,
//                    fragmentTwo);
//
//        }

        // Schedule for the replacement of the Fragment as soon as possible
        fragmentTransaction.commit();

        // setContentView(R.layout.activity_my);
    }


    private void submitForm() {

        int selectedId = toxicityRadioGroup.getCheckedRadioButtonId();
        String toxicityLevel;
        if(selectedId == R.id.toxic_radio_btn)
            toxicityLevel = "toxic";
        if(selectedId == R.id.nottoxic_radio_btn)
            toxicityLevel = "nottoxic";
        else
            toxicityLevel = "somewhattoxic";

//        registerUser(signupInputName.getText().toString(),
//                signupInputEmail.getText().toString(),
//                signupInputPassword.getText().toString(),
//                gender,
//                signupInputAge.getText().toString());
    }





    public void onNextClick(View v){
        getQuestions(this.Usertoken);
    }

    public void onPreviousClick(View v){
        if (v.getId()== R.id.previousButton){
            // Intent i  = new Intent( Home.this, PostJob.class);
            //i.putExtra("User",user.toString());
            //startActivity(i);
        }
    }

    public void getToxicBarLevel(){
        toxicBar = (SeekBar)findViewById(R.id.obsceneBar);
        int a = toxicBar.getProgress();

        Log.d("AAAAAaa", Integer.toString(a));
    }

    public void setText(String text){
        questionDisplay.setText(text);
    }

    public void getQuestions( final String token){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, URL_FOR_QS, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
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
                }){

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest,"Game");
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
