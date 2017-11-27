package com.example.seok.alone.network;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Seok on 2016-02-13.
 */
public class NetworkModel {

    private static String SERVER_URL = "`"; //연결할 서버 아이피

    AsyncHttpClient client; //라이브러리

    private static NetworkModel instance; //서버와 통신하는 하나의 클래스

    public static NetworkModel getInstance(){
        if(instance == null){
            instance = new NetworkModel();
        }
        return instance;
    }

    private NetworkModel(){
        client = new AsyncHttpClient();
    }

    public interface OnNetworkResultListener<T>{
        public void onResult(T result);
        public void onFail(int code);
    }

    public void getMagazine(final OnNetworkResultListener<MagazineList> listener){
        /*  서버 요청 parmas
        RequestParams params = new RequestParams();
        params.put("uuid","1234");
        */

        client.get(SERVER_URL+"/magazine", new AsyncHttpResponseHandler() { // 서버 요청 데이터 전송시, SERVER_URL+"/magazine",params
            // post

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String strResult = new String(responseBody); //String 형태로 변환  -> Object로 변환해줘야함
                Gson gson = new Gson(); // 구글에서 만든 라이브러리 JSON <-> 자바 OBject 변환을 도움
                MagazineList magazineList = new MagazineList();

                magazineList = gson.fromJson(strResult,MagazineList.class);  // 바인딩

                listener.onResult(magazineList);  // 호출한 class로 결과값 전송
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    public void getSearch(final OnNetworkResultListener<MagazineList> listener,String hashTag){
        RequestParams params = new RequestParams();
        params.put("searchString",hashTag);

        client.post(SERVER_URL+"/search",params,new AsyncHttpResponseHandler() { // 서버 요청 데이터 전송시, SERVER_URL+"/magazine",params
            // post

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String strResult = new String(responseBody); //String 형태로 변환  -> Object로 변환해줘야함
                Gson gson = new Gson(); // 구글에서 만든 라이브러리 JSON <-> 자바 OBject 변환을 도움
                MagazineList magazineList = new MagazineList();

                magazineList = gson.fromJson(strResult,MagazineList.class);  // 바인딩
                listener.onResult(magazineList);  // 호출한 class로 결과값 전송
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }


    public void getMission(final OnNetworkResultListener<MissionList> listener) {

        client.get(SERVER_URL + "/weekmission",new AsyncHttpResponseHandler() {  //post방식은 전부 같음 client.post만 변경
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody); //json -> String

                Gson gson = new Gson(); // 자바 오브젝트를 제이슨으로, 반대로 변환
                MissionList list = new MissionList();
                list = gson.fromJson(result,MissionList.class);
                listener.onResult(list); //결과값을 get
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void getMission2(final OnNetworkResultListener<MissionList> listener) {

        client.get(SERVER_URL + "/mission",new AsyncHttpResponseHandler() {  //post방식은 전부 같음 client.post만 변경
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody); //json -> String

                Gson gson = new Gson(); // 자바 오브젝트를 제이슨으로, 반대로 변환
                MissionList list = new MissionList();
                list = gson.fromJson(result,MissionList.class);
                Log.i("abc1",list.getMissionListList().get(1).getTitle());

                listener.onResult(list); //결과값을 get
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void getInfo(String token, final OnNetworkResultListener<Naver> listener){
        client.addHeader("Authorization", token);
        client.get("https://openapi.naver.com/v1/nid/me", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.i("Naver",result);
                Gson gson = new Gson();
                Naver naver = new Naver();
                naver = gson.fromJson(result, Naver.class);
                listener.onResult(naver);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
