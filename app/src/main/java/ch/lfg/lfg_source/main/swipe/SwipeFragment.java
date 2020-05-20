package ch.lfg.lfg_source.main.swipe;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import ch.lfg.lfg_source.R;
import ch.lfg.lfg_source.animation.DetectSwipeGestureListener;
import ch.lfg.lfg_source.entity.AnswerEntity;
import co.lujun.androidtagview.TagContainerLayout;

public class SwipeFragment extends Fragment {
  private static final int PROGRESSBAR_MAX = 100;

  private TextView name;
  private TextView description;
  private TagContainerLayout tagContainerLayout;
  private Drawable drawable;
  private ProgressBar progressBar;

  private GestureDetectorCompat gestureDetectorCompat;

  protected void getViewElements(View view) {
    name = view.findViewById(R.id.name);
    description = view.findViewById(R.id.description);
    tagContainerLayout = view.findViewById(R.id.tagcontainerLayout);
    progressBar = view.findViewById(R.id.circularProgressbar);
    Resources resources = getResources();
    drawable = resources.getDrawable(R.drawable.circular);
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
    String text = getString(R.string.rejected);
    if (value) {
      text = getString(R.string.accepted);
    }
    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

    int userId = getUserId();
    int groupId = getGroupId();

    if (userId != -1 && groupId != -1) {
      AnswerEntity answer = new AnswerEntity(groupId, userId, value);
      sendMessage(answer);
    }
  }

  void setViewElements(String name, String description, ArrayList<String> tags) {
    this.name.setText(name);
    this.description.setText(description);
    this.tagContainerLayout.setTags(tags);
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

  protected void setProgress(int percent) {
    progressBar.setProgress(percent);
    progressBar.setMax(PROGRESSBAR_MAX);
    progressBar.setProgressDrawable(drawable);
  }
}
