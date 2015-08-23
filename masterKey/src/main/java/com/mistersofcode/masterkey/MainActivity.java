package com.mistersofcode.masterkey;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.parse.Parse;
import com.parse.ParseObject;
import com.simplify.android.sdk.Simplify;
import com.simplify.android.sdk.model.Card;
import com.simplify.android.sdk.model.SimplifyError;
import com.simplify.android.sdk.model.Token;
import com.simplify.android.sdk.view.CardEditor;


public class MainActivity extends Activity {

    private String SIMPLIFY_PUBLIC_API_KEY = "sbpb_ZTQ4ZjEyZDItOTM5Zi00NDcxLWIyMmQtOTc4YWExYTRkZWQy";

    private String SIMPLIFY_PRIVATE_API_KEY = "xKixB/deX5d7CdKAj7uWHdNAH2KtglPu76Siwzl25ax5YFFQL0ODSXAOkNtXTToq";

    private Simplify mSimplify;

    private CardEditor mCardEditor;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSimplify = new Simplify(SIMPLIFY_PUBLIC_API_KEY);
        Parse.enableLocalDatastore(this);
        final CardEditor editor = (CardEditor) findViewById(R.id.card_editor);
        Parse.initialize(this, "oeMDj84i1tC5FaWLTf3X0InyDn3ahWIK7zM6xfVj", "8kXcxnLbLBndSj2oq8Y0TUiKFXT6buQDecyMp4L4");
        initUI();
    }

    private void initUI()
    {
        mCardEditor = (CardEditor) findViewById(R.id.card_editor);

        mCardEditor.setOnChargeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Card card = mCardEditor.getCard();
                AsyncTask<?, ?, ?> task = mSimplify.createCardToken(card, new Simplify.CreateTokenListener() {
                    @Override
                    public void onSuccess(Token token) {
                        Log.i(TAG, "Created Token: " + token.getId());
                        mCardEditor.showSuccessOverlay("");
//                        Log.d("", token.getId());
                        ParseObject parseTransaction = new ParseObject("Transaction");
                        parseTransaction.put("number", token.getCard().getNumber());
                        parseTransaction.put("amount", mCardEditor.getAmount());
                        parseTransaction.saveInBackground();
                    }

                    @Override
                    public void onError(SimplifyError error) {
                        Log.e(TAG, "Error Creating Token: " + error.getMessage());
                        mCardEditor.showErrorOverlay("Unable to retrieve card token. " + error.getMessage());
                    }
                });
            }
        });

        // init reset button
        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCardEditor.reset();
            }
        });
    }
}