package com.example.compaq.read_it;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by COMPAQ on 14/02/18.
 */

public class SearchActivity extends AppCompatActivity implements Callback{

    public static String BASE_URL = ""; //"https://www.googleapis.com/books/v1/";
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        Log.e("TAG", "onCreate: search0");
        super.onCreate(savedInstanceState, persistentState);
        Log.e("TAG", "onCreate: search 1");
        setContentView(R.layout.activity_search);
        Log.e("TAG", "onCreate: search2");
        final EditText editText = findViewById(R.id.editText);
        Log.e("TAG", "onCreate: search3");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        final OkHttpClient client = new OkHttpClient();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = editText.getText().toString();
                inputString = inputString.replace(" ", "+");

                Request request = new Request.Builder()
                        .url(BASE_URL + "volumes?q=" + inputString)
                        .build();

                client.newCall(request).enqueue(SearchActivity.this);
            }
        });
        Log.e("TAG", "onCreate: search4");
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SearchActivity.this, "Failed to load. Try again :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String s = response.body().string();

        ArrayList<Book> books = new ArrayList<>();
        try {
            books = parseJson(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SearchAdapter bookAdapter = new SearchAdapter(books,SearchActivity.this);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(bookAdapter);
            }
        });
    }

    private ArrayList<Book> parseJson(String s) throws JSONException{
        ArrayList<Book> books = new ArrayList<>();
//        Log.e("TAG", "parseJson: ");

        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonArray = jsonObject.getJSONArray("items");

        for(int i = 0; i<jsonArray.length();i++){
            JSONObject currentObject = jsonArray.getJSONObject(i);

            String selflink = currentObject.getString("selfLink");
            JSONObject info = currentObject.getJSONObject("volumeInfo");
            String title = info.getString("title");
            String subtitle = info.getString("subtitle");
            ArrayList<String> authors = (ArrayList<String>) info.get("authors");
            JSONObject image = info.getJSONObject("imageLinks");
            String imageLink = image.getString("thumbnail");

            books.add(new Book(title,authors,subtitle,selflink,imageLink,0));
        }
//        Log.e("TAG", "parseJson: " + users.get(3).getLogin());
        return books;
    }
}
