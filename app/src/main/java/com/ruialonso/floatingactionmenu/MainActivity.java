package com.ruialonso.floatingactionmenu;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initViews();
  }

  private void initViews() {
    /*
    com.ruialonso.library.FloatingActionsMenu floatingActionsMenu =
        (com.ruialonso.library.FloatingActionsMenu) findViewById(R.id.floating_actions_menu);
    floatingActionsMenu.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

      }
    });

    com.ruialonso.library.FloatingActionButton floatingActionButtonSearch =
        (com.ruialonso.library.FloatingActionButton) findViewById(R.id.floating_action_button_search);
    floatingActionButtonSearch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, R.string.fab_button_search_text, Snackbar.LENGTH_SHORT).expand();
      }
    });

    com.ruialonso.library.FloatingActionButton floatingActionButtonProfile =
        (com.ruialonso.library.FloatingActionButton) findViewById(R.id.floating_action_button_profile);
    floatingActionButtonProfile.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, R.string.fab_button_profile_text, Snackbar.LENGTH_SHORT).expand();
      }
    });

    com.ruialonso.library.FloatingActionButton floatingActionButtonEmotion1 =
        (com.ruialonso.library.FloatingActionButton) findViewById(R.id.floating_action_button_emotion1);
    floatingActionButtonEmotion1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, R.string.fab_button_emotion1_text, Snackbar.LENGTH_SHORT).expand();
      }
    });

    com.ruialonso.library.FloatingActionButton floatingActionButtonEmotion2 =
        (com.ruialonso.library.FloatingActionButton) findViewById(R.id.floating_action_button_emotion2);
    floatingActionButtonEmotion2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, R.string.fab_button_emotion2_text, Snackbar.LENGTH_SHORT).expand();
      }
    });

    com.ruialonso.library.FloatingActionButton floatingActionButtonEmotion3 =
        (com.ruialonso.library.FloatingActionButton) findViewById(R.id.floating_action_button_emotion3);
    floatingActionButtonEmotion3.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, R.string.fab_button_emotion3_text, Snackbar.LENGTH_SHORT).expand();
      }
    });

    com.ruialonso.library.FloatingActionButton floatingActionButtonEmotion4 =
        (com.ruialonso.library.FloatingActionButton) findViewById(R.id.floating_action_button_emotion4);
    floatingActionButtonEmotion4.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, R.string.fab_button_emotion4_text, Snackbar.LENGTH_SHORT).expand();
      }
    });
*/
  }
}
