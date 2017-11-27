package com.example.seok.alone;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seok.alone.network.MagazineList;
import com.example.seok.alone.network.Naver;
import com.example.seok.alone.network.NetworkModel;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost tabHost;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = this.getIntent();
        token = intent.getStringExtra("token");

        tabHost=(FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        TabHost.TabSpec tab1= tabHost.newTabSpec("tab1");
        TabHost.TabSpec tab2= tabHost.newTabSpec("tab2");

        tab1.setIndicator("MEGAZINE",null);
        tab2.setIndicator("MISSION",null);

        tabHost.addTab(tab1, com.example.seok.alone.MagazineActivity.class, null);
        tabHost.addTab(tab2, com.example.seok.alone.FrgTwo.class, null);

        TabWidget widget = tabHost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_indicator_ab_holo);
        }


        NetworkModel.getInstance().getMagazine(new NetworkModel.OnNetworkResultListener<MagazineList>() {
            @Override
            public void onResult(MagazineList result) {
                //Toast.makeText(MainActivity.this, result.getUserList().get(1).getContent(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(int code) {

            }
        });

        NetworkModel.getInstance().getInfo(token, new NetworkModel.OnNetworkResultListener<Naver>() {
            @Override
            public void onResult(Naver result) {
                Toast.makeText(MainActivity.this, result.response.name+"님 환영합니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

}
