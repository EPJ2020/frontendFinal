package com.example.lfg_source.main.swipe;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import com.example.lfg_source.R;
import com.example.lfg_source.animation.DetectSwipeGestureListener;
import com.example.lfg_source.entity.AnswerEntity;

import java.util.ArrayList;
import java.util.Objects;

import co.lujun.androidtagview.TagContainerLayout;

public class SwipeFragment extends Fragment {
  private TextView name;
  private TextView description;
  private TagContainerLayout mTagContainerLayout;
  private Drawable drawable;
  private ProgressBar mProgress;

  private GestureDetectorCompat gestureDetectorCompat;

  protected void getViewElements(View view) {
    name = view.findViewById(R.id.name);
    description = view.findViewById(R.id.description);
    mTagContainerLayout = view.findViewById(R.id.tagcontainerLayout);

    mProgress = view.findViewById(R.id.circularProgressbar);
    Resources res = getResources();
    drawable = res.getDrawable(R.drawable.circular);
    view.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            gestureDetectorCompat.onTouchEvent(event);
            return true;
          }
        });
  }

  public void setInterested(boolean value) {
    // TODO Funtioniert noch nicht -> Fix
    int userId = getUserId();
    int groupId = getGroupId();

    if (userId != -1 && groupId != -1) {
      AnswerEntity answer = new AnswerEntity(groupId, userId, value);
      sendMessage(answer);
    }
  }

  void setViewElements(String lastName, String description, ArrayList<String> tags) {
    this.name.setText(lastName);
    this.description.setText(description);
    this.mTagContainerLayout.setTags(tags);
  }

  protected void setGestureSwipe() {
    DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener(this);
    gestureDetectorCompat =
        new GestureDetectorCompat(
            Objects.requireNonNull(getActivity()).getParent(), gestureListener);
  }

  public void showSuggestion() {}

  public int getUserId() {
    return 0;
  }

  public int getGroupId() {
    return 0;
  }

  public void sendMessage(AnswerEntity answer) {}

  protected void setProgress() {
    mProgress.setProgress(60);
    mProgress.setMax(100);
    mProgress.setProgressDrawable(drawable);
  }
}
