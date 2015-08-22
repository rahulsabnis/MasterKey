package com.mistersofcode.masterkey;

import android.app.Activity;
import android.os.Bundle;
import com.simplify.android.sdk.Simplify;


public class MainActivity extends Activity {

    private Simplify mSimplify = new Simplify("sbpb_ZTQ4ZjEyZDItOTM5Zi00NDcxLWIyMmQtOTc4YWExYTRkZWQy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
