package com.example.seok.alone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seok.alone.network.MagazineList;
import com.example.seok.alone.network.NetworkModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MagazineSearchActivity extends AppCompatActivity {
    ListView ListSearch;
    ArrayList<hashSearchList> ArhashSearchList = new ArrayList<hashSearchList>();
    TextView hashTag1,hashTag2,hashTag3,hashTag4;
    String searchWord;
    ImageView searchImg;
    TextView searchDetail;

    private class hashSearchList {
        String searchImage;
        String searchDetail;
        hashSearchList(String searchImage,String searchDetail) {
            this.searchImage = searchImage;
            this.searchDetail = searchDetail;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_search);

        hashTag1 = (TextView) findViewById(R.id.mainDetailHashTag1);
        hashTag2 = (TextView) findViewById(R.id.mainDetailHashTag2);
        hashTag3 = (TextView) findViewById(R.id.mainDetailHashTag3);
        hashTag4 = (TextView) findViewById(R.id.mainDetailHashTag4);

        Intent intent = getIntent();
        searchWord = intent.getStringExtra("HASHTAG");

        hashTag1.setVisibility(View.INVISIBLE);
        hashTag2.setVisibility(View.INVISIBLE);
        hashTag3.setVisibility(View.INVISIBLE);
        hashTag4.setVisibility(View.INVISIBLE);

        ListSearch = (ListView)findViewById(R.id.listSearch);
        setHashTag(searchWord);
        initSearhWordnList();
    }

    AdapterView.OnItemClickListener SearchItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(),"옹ㅅㅇ너",Toast.LENGTH_SHORT);
        }
    };

    public class BaseMagazineSearchAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflacter;
        ArrayList<hashSearchList> ArHashWord;
        int layout;

        BaseMagazineSearchAdapter(Context context, int alayout, ArrayList<hashSearchList> aarSrc) {
            this.context = context;
            inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = alayout;
            ArHashWord = aarSrc;
        }

        public int getCount() {
            return ArHashWord.size();
        }

        public String getItem(int position) {
            return ArHashWord.get(position).searchImage;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null)
                convertView = inflacter.inflate(layout, parent, false);
            searchImg = (ImageView)convertView.findViewById(R.id.SearchPicture);
            ImageLoader.getInstance().displayImage(ArHashWord.get(position).searchImage, searchImg, MyApplication.getDisplayImageOptions());

            searchDetail = (TextView) convertView.findViewById(R.id.SearchPictureDetail);
            searchDetail.setText(ArHashWord.get(position).searchDetail);

            searchDetail.setMaxLines(4);
            searchDetail.setVerticalScrollBarEnabled(true);
            searchDetail.setHorizontalFadingEdgeEnabled(true);
            searchDetail.setMovementMethod(new ScrollingMovementMethod());

            return convertView;
        }
    };

    private void initSearhWordnList() {
        NetworkModel.getInstance().getSearch(new NetworkModel.OnNetworkResultListener<MagazineList>() {
            @Override
            public void onResult(MagazineList magazineList) {
                hashSearchList lc;
                for (int i = 0; i < magazineList.getUserList().size(); i++) {
                    lc = new hashSearchList(magazineList.getUserList().get(i).getImgurl(),magazineList.getUserList().get(i).getContent());
                    ArhashSearchList.add(lc);
                }
                BaseMagazineSearchAdapter baseSearchAdapter = new BaseMagazineSearchAdapter(getBaseContext(), R.layout.activity_magazine_search_layout, ArhashSearchList);
                ListSearch.setAdapter(baseSearchAdapter);
                ListSearch.setOnItemClickListener(SearchItemClickListener);
            }

            @Override
            public void onFail(int code) {
            }
        },searchWord);
    }

    private void setHashTag(String strSearchWord) {
        StringTokenizer st = new StringTokenizer(strSearchWord,"#");
        int tokenCounter = st.countTokens();
        int i = 0;
        Log.e("(AWS)tokenCounter 확인 : ", "" + tokenCounter);

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
                    if (4 <= tokenCounter) {
                        hashTag4.setText("# " + st.nextToken());
                        hashTag4.setVisibility(View.VISIBLE);
                        i++;
                    }
                }
            }
        }
    }
}
