//Ibrahim Ouenzar
//CSD230
//Final Project

package com.example.appfinalproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String jsonResponse = getIntent().getStringExtra("json_response");
        List<Book> books = parseJson(jsonResponse);
        mAdapter = new BookAdapter(books);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Book> parseJson(String jsonResponse) {
        List<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                String title = volumeInfo.optString("title");
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                String authors = (authorsArray != null) ? authorsArray.join(", ") : null;

                books.add(new Book(title, authors));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
    public class Book {
        private String title;
        private String authors;

        public Book(String title, String authors) {
            this.title = title;
            this.authors = authors;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthors() {
            return authors;
        }
    }
}