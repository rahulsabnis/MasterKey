package com.mistersofcode.masterkey;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.parse.Parse;
import com.parse.ParseObject;
import com.simplify.android.sdk.Simplify;
import com.simplify.android.sdk.model.Card;
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
        final CardEditor editor = (CardEditor) findViewById(R.id.card_editor);
        Parse.initialize(this, "oeMDj84i1tC5FaWLTf3X0InyDn3ahWIK7zM6xfVj", "8kXcxnLbLBndSj2oq8Y0TUiKFXT6buQDecyMp4L4");
        final Simplify.CreateTokenListener listener = new Simplify.CreateTokenListener() {
            @Override
            public void onSuccess(Token token) {
                Log.i("Simplify", "Created Token: " + token.getId());
                ParseObject parseToken = new ParseObject("Token");
                parseToken.put("token", token.getId());
                editor.showSuccessOverlay("Payment Successful");
            }

            @Override
            public void onError(SimplifyError simplifyError) {
                Log.e("Simplify", "Error Creating Token: " + simplifyError.getMessage());
                editor.showErrorOverlay("Payment Failed");
            }
        };
        editor.setOnChargeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = editor.getCard();
                AsyncTask<?, ?, ?> createTokenTask = mSimplify.createCardToken(card, listener);
                Log.i("", "Card Number: " + card.getNumber());
                Log.i("", "Amount: " + editor.getAmount());
                ParseObject parseTransaction = new ParseObject("Transaction");
                parseTransaction.put("number", card.getNumber());
                parseTransaction.put("amount", editor.getAmount());
                parseTransaction.saveInBackground();
            }
        });

    }
}
