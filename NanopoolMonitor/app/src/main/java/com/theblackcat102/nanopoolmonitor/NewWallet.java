package com.theblackcat102.nanopoolmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.theblackcat102.nanopoolmonitor.Models.Wallet;

public class NewWallet extends AppCompatActivity {
    private EditText wallet_id;
    private Button submit;
    private RadioGroup wallet_type;
    private TextView hint;
    private NewWallet pointer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wallet);
        wallet_id = (EditText) findViewById(R.id.wallet_id);
        wallet_type = (RadioGroup)findViewById(R.id.wallet_option);
        submit = (Button) findViewById(R.id.submit_btn);
        hint = (TextView) findViewById(R.id.wallet_hint);
        pointer = this;
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String walletType = "zec";
                String walletID;
                int radioButtonID = wallet_type.getCheckedRadioButtonId();
                switch(radioButtonID){
                    case R.id.etc:
                        walletType = "etc";
                        break;
                    case R.id.zec:
                        walletType = "zec";
                        break;
                    case R.id.xmr:
                        walletType = "xmr";
                        break;
                    case R.id.eth:
                        walletType = "eth";
                        break;
                }
                walletID = wallet_id.getText().toString();
                if(walletType == null){
                    hint.setText(getString(R.string.hint_wallet));
                }
                else if(walletID == null){
                    hint.setText(getString(R.string.hint_wallet));
                }
                else{
                    Wallet newWallet = new Wallet(walletType,walletID);
                    newWallet.save();
                    Intent intent = new Intent(pointer, MainActivity.class);
                    intent.putExtra(Wallet.WALLET_ID,walletID);
                    intent.putExtra(Wallet.WALLET_TYPE,walletType);
                    startActivity(intent);
                }


            }
        });
    }
}
