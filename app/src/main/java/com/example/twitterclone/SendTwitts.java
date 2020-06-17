package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTwitts extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSendTwitts;
    private Button btnSendTwitts;
    private ListView viewTweetsListView;
    private Button btnViewTweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_twitts);

        edtSendTwitts = findViewById(R.id.edtSendTwitt);
        btnSendTwitts = findViewById(R.id.btnSendTwitt);
        viewTweetsListView = findViewById(R.id.viewTweetListView);
        btnViewTweets = findViewById(R.id.btnViewTweets);

        btnViewTweets.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {

        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(SendTwitts.this,tweetList,android.R.layout.simple_list_item_2,new String[]{"tweetUserName","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});

        try {
            ParseQuery<ParseObject>parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user",ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                        if(objects.size()>0 && e == null){
                            for (ParseObject tweetObject: objects){

                                HashMap<String,String> userTweet = new HashMap<>();
                                userTweet.put("tweetUserName",tweetObject.getString("user"));
                                userTweet.put("tweetValue",tweetObject.getString("tweet"));
                                tweetList.add(userTweet);


                            }

                            viewTweetsListView.setAdapter(adapter);
                        }
                }
            });

        }catch (Exception e){

            e.printStackTrace();
        }



    }
}