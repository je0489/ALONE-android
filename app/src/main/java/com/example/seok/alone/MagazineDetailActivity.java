package com.example.seok.alone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

import com.example.seok.alone.network.MagazineList;
import com.example.seok.alone.network.NetworkModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class MagazineDetailActivity extends AppCompatActivity {
    TextView hashTag1,hashTag2,hashTag3;
    String strPOSITION;
    ImageView mainImage;
    TextView mainImageDetail;
    int POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_detail);

        mainImage = (ImageView) findViewById(R.id.MainPicture);
        mainImageDetail = (TextView) findViewById(R.id.MainPictureDetail);
        hashTag1 = (TextView) findViewById(R.id.mainDetailHashTag1);
        hashTag2 = (TextView) findViewById(R.id.mainDetailHashTag2);
        hashTag3 = (TextView) findViewById(R.id.mainDetailHashTag3);

        hashTag1.setVisibility(View.INVISIBLE);
        hashTag2.setVisibility(View.INVISIBLE);
        hashTag3.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        strPOSITION = intent.getStringExtra("POSITION");
        POSITION = Integer.parseInt(strPOSITION);

        mainImageDetail.setMaxLines(12);
        mainImageDetail.setVerticalScrollBarEnabled(true);
        mainImageDetail.setHorizontalFadingEdgeEnabled(true);
        mainImageDetail.setMovementMethod(new ScrollingMovementMethod());

        initDetail();
    }

    private void initDetail() {

        NetworkModel.getInstance().getMagazine(new NetworkModel.OnNetworkResultListener<MagazineList>() {
            @Override
            public void onResult(MagazineList magazineList) {
                mainImageDetail.setText(magazineList.getUserList().get(POSITION).getContent());
                ImageLoader.getInstance().displayImage(magazineList.getUserList().get(POSITION).getImgurl(), mainImage, MyApplication.getDisplayImageOptions());

                StringTokenizer st = new StringTokenizer(magazineList.getUserList().get(POSITION).getHashtag(),"#");
                int tokenCounter = st.countTokens();
                int i = 0;
                Log.e("(AWS)tokenCounter 확인 : ", ""+tokenCounter);

                if( 1 <= tokenCounter ) {
                    hashTag1.setText("# " + st.nextToken());
                    hashTag1.setVisibility(View.VISIBLE);
                    i++;
                    if (2 <= tokenCounter) {
                        hashTag2.setText("# " + st.nextToken());
                        hashTag2.setVisibility(View.VISIBLE);
                        i++;
                        if (3 <= tokenCounter) {
                            hashTag3.setText("# " + st.nextToken());
                            hashTag3.setVisibility(View.VISIBLE);
                            i++;
                        }
                    }
                }
            }
            @Override
            public void onFail(int code) {}
        });
    }

}
