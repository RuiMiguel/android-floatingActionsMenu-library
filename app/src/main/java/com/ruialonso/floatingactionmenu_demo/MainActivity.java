package com.ruialonso.floatingactionmenu_demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private RecyclerView recycler;
  private DummyAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initViews();
  }

  private void initViews() {
    recycler = (RecyclerView) findViewById(R.id.recycler);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recycler.setLayoutManager(layoutManager);
    adapter = new DummyAdapter();
    recycler.setAdapter(adapter);

    List<String> dummyList = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      dummyList.add("ELEMENTO " + i);
    }
    adapter.setData(dummyList);
  }

  private class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.ViewHolder> {

    private List<String> data;

    public DummyAdapter() {
      this.data = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      Context context = parent.getContext();
      View view = LayoutInflater.from(context).inflate(R.layout.item_element, parent, false);
      return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
      String message = data.get(position);
      if (position % 2 == 0) {
        holder.itemView.setBackgroundColor(Color.GRAY);
      } else {
        holder.itemView.setBackgroundColor(Color.LTGRAY);
      }
      holder.setTitle(message);
    }

    public void setData(List<String> newData) {
      data.addAll(newData);
    }

    @Override public int getItemCount() {
      return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      private TextView textView;

      public ViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);
      }

      public void setTitle(String message) {
        textView.setText(message);
      }
    }
  }
}
