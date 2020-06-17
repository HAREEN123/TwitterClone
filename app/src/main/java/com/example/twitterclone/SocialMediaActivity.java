package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    private String followedUser = ""; // otherwise it will create the null automatically.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);


        listView = findViewById(R.id.listView);// here i need to populate the list view the parse users that we get from the server.
        // This will automatically update based on the data from the array adapter.
        // here we need to ged the all users if not, objects from the server.
        arrayList = new ArrayList(); // to take a array list
        arrayAdapter = new ArrayAdapter(SocialMediaActivity.this, android.R.layout.simple_list_item_checked, arrayList); // let the list view we have an array adapter.
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);// we are going to have checked and unchecked.

        listView.setOnItemClickListener(SocialMediaActivity.this);// Whenever the user taps on one of the items of our list view ,users tab will be notified.then we can do something about that.
        final TextView txtLoadingUsers = findViewById(R.id.txtLoadingUsers);


        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername()); // we do not need the current user from the query as he is already the user.

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) { // users is a list.

                    if (e == null) {
                        if (users.size() > 0) {

                            for (ParseUser user : users) {

                                arrayList.add(user.getUsername()); // in order to populate our list view.
                                // We need to have an Array adapter and this will update the list view. otherwise, we can not interact with the list view directly.
                            }

                            listView.setAdapter(arrayAdapter);
                            txtLoadingUsers.animate().alpha(0).setDuration(2000);
                            listView.setVisibility(View.VISIBLE);

                            for (String twitterUser : arrayList){
                                if(ParseUser.getCurrentUser().getList("fanOf") != null){
                                    if(ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)){
                                        followedUser = followedUser + twitterUser;
                                        listView.setItemChecked(arrayList.indexOf(twitterUser),true);
                                        FancyToast.makeText(SocialMediaActivity.this, ParseUser.getCurrentUser().getUsername() + " is following " + followedUser, Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                                }
                            }


                        }
                    }

                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutUserItem) {

            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    // Social media activity will be eliminated from the stack.
                    Intent intent = new Intent(SocialMediaActivity.this, MainActivity.class); // this must be put in oder to move in to the next activity window...
                    startActivity(intent);
                    finish();

                }
            });


        }else if(item.getItemId()==R.id.postImageItem){

            Intent intent = new Intent(SocialMediaActivity.this,SendTwitts.class); // this must be put in oder to move in to the next activity window...
            startActivity(intent);

    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;

        if(checkedTextView.isChecked()){

            FancyToast.makeText(SocialMediaActivity.this,arrayList.get(position) + " is now followed!", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));
        }else{

            FancyToast.makeText(SocialMediaActivity.this,arrayList.get(position) + " is not followed!", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOfList);// current lis is the lest that not contained the un-followed user.

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(SocialMediaActivity.this, " Saved..!!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
            }
        });

    }
}

