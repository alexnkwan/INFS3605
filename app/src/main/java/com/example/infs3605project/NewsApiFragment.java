package com.example.infs3605project;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsApiFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private JsonArray articles;

    //http://newsapi.org/v2/everything?q=cyber-security&from=2020-07-19&sortBy=publishedAt&apiKey=4cbbfa424e1d4b1793397feb497dcce5
    String apiUrl = "http://newsapi.org/v2/everything";
    String apiKey = "4cbbfa424e1d4b1793397feb497dcce5";
    String from = "2020-07-19";
    String sortBy = "publishedAt";
    String q = "cyber-security";

    String result = "{}";

    public static NewsApiFragment newInstance() {
        return new NewsApiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_api_fragment, container, false);
        initSearch(view);
        initRecyclerView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    void initSearch(View view) {
        searchView = view.findViewById(R.id.searchView);
        searchView.setQueryHint("search");
        searchView.setQuery(q, false);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    q = query;
                    searchTask task = new searchTask();
                    task.execute((Void) null);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }


    void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        customAdapter = new CustomAdapter(this.getContext());
        recyclerView.setAdapter(customAdapter);
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";
        private Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout layout;
            private TextView title;
            private ImageView imageView;

            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                layout = v.findViewById(R.id.layout);
                title = v.findViewById(R.id.title);
                imageView = v.findViewById(R.id.imageView);
            }
        }

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.news_api_list_item, viewGroup, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            final JsonObject article = (JsonObject) articles.get(position);

            JsonElement title = article.get("title");
            JsonElement urlToImage = article.get("urlToImage");

            if (title != null) {
                viewHolder.title.setText(title.getAsString());
            }
            if (urlToImage != null) {
                Glide.with(context).load(urlToImage.getAsString()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.imageView);
            }

            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), NewsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("article", String.valueOf(article));
                    i.putExtras(bundle);
                    startActivityForResult(i, 0);
                }
            });
        }

        @Override
        public int getItemCount() {
            return articles != null ? articles.size() : 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        searchTask task = new searchTask();
        task.execute((Void) null);
    }

    public class searchTask extends AsyncTask<Void, Void, Boolean> {

        searchTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = String.format(
                        "%s?q=%s&from=%s&sortBy=%s&apiKey=%s",
                        apiUrl, q, from, sortBy, apiKey);

                result = fetchNews(url);

                if (!result.isEmpty()) {
                    System.out.println("result: " + result);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(result).getAsJsonObject();
                    articles = jsonObject.getAsJsonArray("articles");
                    System.out.println("result: " + articles.size());
                    return true;
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                customAdapter.notifyDataSetChanged();
            } else {
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    private String fetchNews(String urlString) {
        //String response = "{\"status\":\"ok\",\"totalResults\":281,\"articles\":[{\"source\":{\"id\":null,\"name\":\"Business Wire\"},\"author\":null,\"title\":\"Immersive Labs Launches Cyber Crisis Simulator to Improve Incident Response\",\"description\":\"BOSTON & BRISTOL, England--(BUSINESS WIRE)--Immersive Labs, the company empowering organizations to equip, exercise, and evidence human cyber capabilities, today announced an industry-first solution to create better-drilled crisis response across institutions…\",\"url\":\"https://www.businesswire.com/news/home/20200720005362/en/Immersive-Labs-Launches-Cyber-Crisis-Simulator-Improve\",\"urlToImage\":\"https://mms.businesswire.com/media/20200720005362/en/803652/23/Immersive_labs_logo.jpg\",\"publishedAt\":\"2020-07-20T13:07:50Z\",\"content\":\"BOSTON &amp; BRISTOL, England--(BUSINESS WIRE)--Immersive Labs, the company empowering organizations to equip, exercise, and evidence human cyber capabilities, today announced an industry-first solut… [+2412 chars]\"}]}";
        String response = "";
        try {
            System.out.println("result: " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                response += str;
            }
            in.close();
        } catch (Exception e) {
            System.out.println("result: " + e.getMessage());
        }
        return response;
    }
}