package com.androidtutorialpoint.androidlogin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

/**
 * Created by sithi on 10/10/2017.
 */

public class FragmentTwo extends Fragment implements View.OnClickListener  {

    private static SeekBar identityHateBar;
    private static SeekBar obsceneBar;
    private static SeekBar insultBar;
    private static SeekBar threatBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two_layout, container, false);


    }

    @Override
    public void onClick(View view) {

    }


//    public void getobsceneBarLevel(){
//        obsceneBar = (SeekBar)findViewById(R.id.obsceneBar);
//        int a = obsceneBar.getProgress();
//
//        Log.d("AAAAAaa", Integer.toString(a));
//    }


}
