package com.example.seok.alone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.StringTokenizer;
import java.io.*;
import java.util.regex.*;

import com.example.seok.alone.network.MagazineList;
import com.example.seok.alone.network.NetworkModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class MagazineActivity extends Fragment {
    ListView ListHomeMain;
    ArrayList<HomeMainList> ArHomeMainList = new ArrayList<HomeMainList>();
    String searchWord;
    Intent intent;
    ImageView Img,searchBTN;
    EditText searchET;
    TextView hashTag1,hashTag2,hashTag3,hashTag4,hashTag5,hashTag6,hashTag7;

    private class HomeMainList {
        String mainImage;

        HomeMainList(String mainImage) {
            this.mainImage = mainImage;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_magazine, container, false);
        ListHomeMain = (ListView) view.findViewById(R.id.listMain);
        searchET = (EditText) view.findViewById(R.id.homeSearchEditText);
        searchBTN = (ImageView) view.findViewById(R.id.ImageSearch);
        searchBTN.setOnClickListener(SearchClickListener);
        hashTag1 = (TextView) view.findViewById(R.id.hashTag1);
        hashTag2 = (TextView) view.findViewById(R.id.hashTag2);
        hashTag3 = (TextView) view.findViewById(R.id.hashTag3);
        hashTag4 = (TextView) view.findViewById(R.id.hashTag4);
        hashTag5 = (TextView) view.findViewById(R.id.hashTag5);
        hashTag6 = (TextView) view.findViewById(R.id.hashTag6);
        hashTag7 = (TextView) view.findViewById(R.id.hashTag7);

        initHomeMainList();
        setClickListeners(getContext());
        return view;
    }

    ImageView.OnClickListener SearchClickListener = new ImageView.OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == R.id.ImageSearch) {
                if ( searchET.getText().toString().length() == 0 ) {
                    Toast.makeText(getActivity(),"검색어를 입력하세요",LENGTH_SHORT).show();
                    searchET.setText("");
                } else {
                    TokenSearch(searchET.getText().toString());

                    intent = new Intent(getContext(), MagazineSearchActivity.class);
                    intent.putExtra("HASHTAG",searchWord);
                    startActivity(intent);
                }
            }
        }
    };

    private void setClickListeners(final Context context) {
        hashTag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MagazineSearchActivity.class);
                intent.putExtra("HASHTAG", "#만화");
                startActivity(intent);
            }
        });

        hashTag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MagazineSearchActivity.class);
                intent.putExtra("HASHTAG", "#여행");
                startActivity(intent);
            }
        });

        hashTag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MagazineSearchActivity.class);
                intent.putExtra("HASHTAG", "#요리");
                startActivity(intent);
            }
        });

        hashTag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MagazineSearchActivity.class);
                intent.putExtra("HASHTAG", "#놀이");
                startActivity(intent);
            }
        });

        hashTag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MagazineSearchActivity.class);
                intent.putExtra("HASHTAG", "#전시");
                startActivity(intent);
            }
        });

        hashTag6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MagazineSearchActivity.class);
                intent.putExtra("HASHTAG", "#식물");
                startActivity(intent);
            }
        });

        hashTag7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), MagazineSearchActivity.class);
                intent.putExtra("HASHTAG", "#인테리어");
                startActivity(intent);
            }
        });
    }

    public void TokenSearch(String strString) {
        searchWord = "";
        StringTokenizer st = new StringTokenizer(strString," ");
        while(st.hasMoreTokens()) {
            searchWord += "#" + st.nextToken();
        }
    }

    AdapterView.OnItemClickListener MainItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            intent = new Intent(getContext(), MagazineDetailActivity.class);
            intent.putExtra("POSITION",Integer.toString(position));

            startActivity(intent);
        }
    };

    public class BaseMainMagazineAdapter extends BaseAdapter {
        Context homeEditorImage;
        LayoutInflater inflacter;
        ArrayList<HomeMainList> ArHomeMain = null;
        int layout;

        BaseMainMagazineAdapter(Context context, int alayout, ArrayList<HomeMainList> aarSrc) {
            homeEditorImage = context;
            inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = alayout;
            ArHomeMain = aarSrc;
        }

        public int getCount() {
            return ArHomeMain.size();
        }

        public String getItem(int position) {
            return ArHomeMain.get(position).mainImage;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null)
                convertView = inflacter.inflate(layout, parent, false);
            Img = (ImageView)convertView.findViewById(R.id.listMainPicture);
            ImageLoader.getInstance().displayImage(ArHomeMain.get(position).mainImage, Img, MyApplication.getDisplayImageOptions());

            return convertView;
        }
    };

    private void initHomeMainList() {
        NetworkModel.getInstance().getMagazine(new NetworkModel.OnNetworkResultListener<MagazineList>() {
            @Override
            public void onResult(MagazineList magazineList) {
                HomeMainList lc;
                for (int i = 0; i < magazineList.getUserList().size(); i++) {
                    lc = new HomeMainList(magazineList.getUserList().get(i).getImgurl());
                    ArHomeMainList.add(lc);
                }
                BaseMainMagazineAdapter baseMenuAdapter = new BaseMainMagazineAdapter(getContext(), R.layout.activity_magazine_layout, ArHomeMainList);
                ListHomeMain.setAdapter(baseMenuAdapter);
                ListHomeMain.setOnItemClickListener(MainItemClickListener);
            }

            @Override
            public void onFail(int code) {
            }
        });
    }
}
