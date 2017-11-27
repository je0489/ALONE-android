package com.example.seok.alone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seok.alone.network.Mission;
import com.example.seok.alone.network.MissionList;
import com.example.seok.alone.network.NetworkModel;

import java.util.ArrayList;

/**
 * Created by Seok on 2016-02-14.
 */
public class missionShowing extends Activity {

    TextView todayMission;
    ArrayList<Mission> missionLists = new ArrayList<Mission>();
    String title;
    String writer;
    Bitmap usrPhoto;
    String likeNum;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_show);

        todayMission = (TextView)findViewById(R.id.today_mission);

        //미션정보 가져오기
        NetworkModel.getInstance().getMission(new NetworkModel.OnNetworkResultListener<MissionList>() {
            @Override
            public void onResult(MissionList result) {
                todayMission.setText(result.getMissionListList().get(2).getMission());
                //setText(result.getMissionListList().get(2).getMission());
            }

            @Override
            public void onFail(int code) {

            }
        });

        // 게시글 클릭 시 디비에서 글번호에 대한 코멘트 정보 모두 불러오기 -> 개수 체크해서 최신 순으로(번호 높은 순) 리스트뷰 생성 저장 -> 화면에 뿌림
        // 버튼 클릭 시 서버에 글번호에 대한 comment 저장 & 화면에 출력 가장 처음에 출력 -> 나머지 아래로 밀림

        NetworkModel.getInstance().getMission2(new NetworkModel.OnNetworkResultListener<MissionList>() {
            @Override
            public void onResult(MissionList result) {
                //Log.i("abc1", result.getMissionListList().get(1).getTitle());
                missionLists = result.getMissionListList();

            }

            @Override
            public void onFail(int code) {
            }
        });
    }
}

