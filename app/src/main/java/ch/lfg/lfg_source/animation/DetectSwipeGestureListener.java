package ch.lfg.lfg_source.animation;

import android.view.GestureDetector;
import android.view.MotionEvent;

import ch.lfg.lfg_source.main.swipe.SwipeFragment;

public class DetectSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

  private static int Min_Swipe_Distance_X = 100;
  private static int Max_Swipe_Distance_X = 1000;
  private SwipeFragment swipeFragment;

  public DetectSwipeGestureListener(SwipeFragment swipeFragment) {
    this.swipeFragment = swipeFragment;
  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    float deltaX = e1.getX() - e2.getX();
    float deltaXAbs = Math.abs(deltaX);

    if (deltaXAbs >= Min_Swipe_Distance_X && deltaXAbs <= Max_Swipe_Distance_X) {
      if (deltaX >= 0) {
        this.swipeFragment.setInterested(false);
        this.swipeFragment.showSuggestion();
      } else {
        this.swipeFragment.setInterested(true);
        this.swipeFragment.showSuggestion();
      }
    }
    return true;
  }

  @Override
  public boolean onSingleTapConfirmed(MotionEvent e) {
    return super.onSingleTapConfirmed(e);
  }

  @Override
  public boolean onDoubleTap(MotionEvent e) {
    return super.onDoubleTap(e);
  }
}
