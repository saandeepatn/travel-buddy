package com.truiton.placepicker;



        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;


public class TweetTimeline extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_timeline);
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        double lat=bundle.getDouble("lat");
        double lng=bundle.getDouble("lng");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("" + lat+"" +lng+ "")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();







    }
}
