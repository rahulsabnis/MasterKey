package com.mistersofcode.masterkey;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseObject;
import com.simplify.android.sdk.Simplify;
import com.simplify.android.sdk.model.SimplifyError;
import com.simplify.android.sdk.model.Token;
import com.simplify.android.sdk.view.CardEditor;


public class MainActivity extends Activity {

    private Simplify mSimplify = new Simplify("sbpb_ZTQ4ZjEyZDItOTM5Zi00NDcxLWIyMmQtOTc4YWExYTRkZWQy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "oeMDj84i1tC5FaWLTf3X0InyDn3ahWIK7zM6xfVj", "8kXcxnLbLBndSj2oq8Y0TUiKFXT6buQDecyMp4L4");
        Simplify.CreateTokenListener listener = new Simplify.CreateTokenListener() {
            @Override
            public void onSuccess(Token token) {
                Log.i("Simplify", "Created Token: " + token.getId());
            }

            @Override
            public void onError(SimplifyError simplifyError) {
                Log.e("Simplify", "Error Creating Token: " + simplifyError.getMessage());
            }
        };
        CardEditor editor = (CardEditor) findViewById(R.id.card_editor);
        ParseObject transaction = new ParseObject("Transaction");
        transaction.put("Card Number", editor.getCard().getNumber());
        transaction.put("Charity Phrase", "Unknown");
        // TODO More Fields 
        transaction.saveInBackground();
    }
}
