package com.example.infs3605project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PurityTest extends AppCompatActivity {

    List<TestQuestion> dataList = new ArrayList<>();
    Users userLogin = new Users();

    ListView listView;
    MyAdapter myAdapter;
    TextView scoreTextView;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity_test);

        initData();

        listView = findViewById(R.id.listView);
        scoreTextView = findViewById(R.id.score);
        submit = findViewById(R.id.submit);

        scoreTextView.setText("20");

        myAdapter = new MyAdapter(dataList, scoreTextView);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TestQuestion data = myAdapter.getItem(position);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                for (TestQuestion data : dataList) {
                    if (data.isChecked()) {
                        count++;
                    }
                }

                userLogin.setPurityScore(count);
                scoreTextView.setText("" + count + "/" + dataList.size());
                Toast.makeText(view.getContext(), "Total Score: " + count + "/" + dataList.size(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    void initData() {
        TestQuestion data0 = new TestQuestion();
        data0.setQuestion("I use the same password for everything");
        dataList.add(data0);

        TestQuestion data1 = new TestQuestion();
        data1.setQuestion("Test");
        dataList.add(data1);

        TestQuestion data2 = new TestQuestion();
        data2.setQuestion("Test");
        dataList.add(data2);

        TestQuestion data3 = new TestQuestion();
        data3.setQuestion("Test");
        dataList.add(data3);

        TestQuestion data4 = new TestQuestion();
        data4.setQuestion("Test");
        dataList.add(data4);


        userLogin.setFirstName("tom");
    }

    private class MyAdapter extends BaseAdapter {
        TextView scoreTextView;
        List<TestQuestion> dataList;

        public MyAdapter(List<TestQuestion> dataList, TextView scoreTextView) {
            this.dataList = dataList;
            this.scoreTextView = scoreTextView;
        }

        @Override
        public int getCount() {
            return dataList != null ? dataList.size() : 0;
        }

        @Override
        public TestQuestion getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.purity_list_item, container, false);
            }

            final TestQuestion data = dataList.get(position);

            TextView name = convertView.findViewById(R.id.name);
            final CheckBox checkBox = convertView.findViewById(R.id.checkBox);

            name.setText(data.getQuestion());
            checkBox.setChecked(data.isChecked());

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.setChecked(!data.isChecked());
                    data.setChecked(checkBox.isChecked());

                    int count = 0;
                    for (TestQuestion data : dataList) {
                        if (data.isChecked()) {
                            count++;
                        }
                    }
                    scoreTextView.setText("" + count);
                }
            });

            return convertView;
        }
    }
}