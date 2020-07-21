package com.example.infs3605project;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NewsDetailActivity extends AppCompatActivity {
    JsonObject article;
    TextView description;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        if (getIntent().hasExtra("article")) {
            JsonParser jsonParser = new JsonParser();
            article = jsonParser.parse(getIntent().getStringExtra("article")).getAsJsonObject();

            imageView = findViewById(R.id.imageView);

            description = findViewById(R.id.description);

            description.setText(article.get("description").getAsString());

            toolBarLayout.setTitle(article.get("title").getAsString());

            Glide.with(this.getApplicationContext()).load(article.get("urlToImage").getAsString()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }
}