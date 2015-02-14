package jennmeedo.gmail.com.instagramphotes;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by momar on 2/13/15.
 */
public class PhotoFetcher {
    private static String clientID = "25c4e98d03394d44b1fe8c9427a1f299";
    private static String endPointURI = "https://api.instagram.com/v1/media/popular?client_id=%s";
    private PhotoAdapter adapter;
    private ArrayList<Photo> photoList = new ArrayList<Photo>();

    public void setAdapter(PhotoAdapter adapter) {
        this.adapter = adapter;
    }

    public ArrayList<Photo> getPhotoList() {
        return photoList;
    }


    static
    {
        endPointURI = String.format(endPointURI,clientID);
    }



    public void fetchData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(endPointURI, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray array = null;

                android.util.Log.i("DEBUG", response.toString());
                try {
                    array = response.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        photoList.add(getPhoto(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
             if(adapter != null)
             {
                 adapter.notifyDataSetChanged();
             }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                android.util.Log.i("DEBUG", responseString.toString());

            }
        });

    }
        private Photo getPhoto(JSONObject o){

            Photo photo = new Photo();
            try {
                photo.userName  = o.getJSONObject("user").getString("username");
                photo.caption = o.getJSONObject("caption").getString("text");
                photo.height = o.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                photo.noOfLikes = o.getJSONObject("likes").getInt("count");
                photo.url = o.getJSONObject("images").getJSONObject("standard_resolution").getString("url");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return photo;
    }

}
