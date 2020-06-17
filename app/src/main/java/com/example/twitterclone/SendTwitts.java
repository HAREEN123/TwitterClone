package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTwitts extends AppCompatActivity {

    private EditText edtSendTwitts;
    private Button btnSendTwitts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_twitts);

        edtSendTwitts = findViewById(R.id.edtSendTwitt);
        btnSendTwitts = findViewById(R.id.btnSendTwitt);

        btnSendTwitts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtSendTwitts.getText().toString().equals("")) {

                    FancyToast.makeText(SendTwitts.this, "You must enter a twitt.!!!", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();


                }else{

                    ParseObject parseObject = new ParseObject("MyTweet");// we are going to have a new class in the parse dashboard.
                    parseObject.put("tweet", edtSendTwitts.getText().toString());// parse file means our image.
                    parseObject.put("user", ParseUser.getCurrentUser().getUsername());// this is a new column to the class naming the Description.
                    final ProgressDialog dialog = new ProgressDialog(SendTwitts.this);
                    dialog.setMessage("Loading...");
                    dialog.show();

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                FancyToast.makeText(SendTwitts.this, "Done!!!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

                            } else {

                                FancyToast.makeText(SendTwitts.this, "Unknown Error: " + e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                            }

                            dialog.dismiss();

                        }
                    });




                }




            }

        });
    }



    }