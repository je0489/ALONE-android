package com.example.seok.alone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seok.alone.network.Mission;
import com.example.seok.alone.network.MissionList;
import com.example.seok.alone.network.NetworkModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by KimCP on 16. 2. 12..
 */
public class FrgTwo extends Fragment {

    GridView gridView;
    ArrayList<Bitmap> picArr = new ArrayList<Bitmap>();
    ArrayList<String> textArr = new ArrayList<String>();
    TextView misTitle;
    ArrayList<Mission> missionLists = new ArrayList<Mission>();
    String idx = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frgtwo, container, false);

        //미션 정보 가져오기
        misTitle = (TextView)view.findViewById(R.id.tv_mis_title);
        gridView = (GridView) view.findViewById(R.id.gridView);
        NetworkModel.getInstance().getMission(new NetworkModel.OnNetworkResultListener<MissionList>() {
            @Override
            public void onResult(MissionList result) {
                //Toast.makeText(MainActivity.this, result.getUserList().get(1).getContent(), Toast.LENGTH_SHORT).show();
               // misTitle.setText(result.getMissionListList().size());
                setText(result.getMissionListList().get(2).getMission());
            }

            @Override
            public void onFail(int code) {

            }
        });


        // 서버에 저장된 미션 글 정보 모두 가져오기
        // 번호로 내림차순 정렬 후, 하나씩


        NetworkModel.getInstance().getMission2(new NetworkModel.OnNetworkResultListener<MissionList>() {
            @Override
            public void onResult(MissionList result) {
                //Log.i("abc1", result.getMissionListList().get(1).getTitle());

                missionLists = result.getMissionListList();

                Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.addicon);
                picArr.add(bm1);
                Bitmap bm;
                //Log.i("abc", missionLists.size() + "");

                String url1 = missionLists.get(0).getImgurl();
                String like1 = missionLists.get(0).getLikenum();
                String url2 = missionLists.get(1).getImgurl();
                String like2 = missionLists.get(1).getLikenum();
                //System.out.println("url" + url);

                //String title = missionLists.get(1).getTitle();
                //System.out.println("title" + title);
                for (int i = 0; i < missionLists.size(); i++) {
                    //picArr.add(missionLists.get(i).);
                    bm = BitmapFactory.decodeFile(missionLists.get(i).getImgurl());
                    picArr.add(bm);
                }

                textArr.add("");
                for (int i = 0; i < missionLists.size(); i++) {
                    //textArr.add("숫자" + Integer.toString(i));
                    textArr.add("좋아요" + missionLists.get(i).getLikenum());
                }


                gridView.setAdapter(new gridAdapter());
            }

            @Override
            public void onFail(int code) {

            }
        });


        return view;
    }

    public void setText(String title){
        misTitle.setText(title);
    }

    public class gridAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public gridAdapter() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return picArr.size();
        }

        public Object getItem(int position) {
            return picArr.get(position);    //아이템을 호출할 때 사용하는 메소드
        }

        @Override

        public long getItemId(int position) {
            return position;    //아이템의 아이디를 구할 때 사용하는 메소드
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.photo, parent, false);
            }

            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_photo);
            TextView textView = (TextView)convertView.findViewById(R.id.textView_photo);
            ImageButton imgBtn = (ImageButton)convertView.findViewById(R.id.ImageButton_photo);

            //System.out.println(picArr.get(0));
            //System.out.println(picArr.get(1));

            //System.out.println(textArr.get(0));
            //System.out.println(textArr.get(1));

            if(position == 0){
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.addicon, imageView, MyApplication.getDisplayImageOptions());
                //textView.setVisibility(View.GONE);
                //imgBtn.setVisibility(View.GONE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FrgTwo.this.getActivity(), missionProcessing.class);
                        intent.putExtra("title", misTitle.getText().toString());
                        startActivity(intent);
                    }
                });
            }else{

                //textView.setVisibility(View.VISIBLE);
                //imgBtn.setVisibility(View.VISIBLE);
                //Log.i("T",missionLists.get(position-1).getImgurl());
                //ImageLoader.getInstance().displayImage(missionLists.get(2).getImgurl(), imageView, MyApplication.getDisplayImageOptions());

                for(int i=0; i<missionLists.size(); i++) {
                    ImageLoader.getInstance().displayImage(missionLists.get(i).getImgurl(), imageView, MyApplication.getDisplayImageOptions());
                }

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FrgTwo.this.getActivity(), missionShowing.class);
                        intent.putExtra("title", misTitle.getText().toString());
                        startActivity(intent);
                    }
                });

            }

            //imageView.setImageBitmap(picArr.get(position));
            textView.setText(textArr.get(position));


            return convertView;
        }
    }
}