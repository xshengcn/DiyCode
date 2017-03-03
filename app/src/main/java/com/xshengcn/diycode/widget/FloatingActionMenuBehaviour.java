package com.xshengcn.diycode.widget;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.github.clans.fab.FloatingActionMenu;
import com.xshengcn.diycode.R;

public class FloatingActionMenuBehaviour extends CoordinatorLayout.Behavior<FloatingActionMenu> {
  private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
  private boolean mIsAnimatingOut = false;
  private boolean mIsAnimatingIn = false;

  public FloatingActionMenuBehaviour(Context context, AttributeSet attrs) {
  }

  @Override
  public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionMenu child, View dependency) {
    return dependency instanceof Snackbar.SnackbarLayout;
  }

  @Override
  public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionMenu child, View dependency) {
    float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
    child.setTranslationY(translationY);
    return true;
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
      FloatingActionMenu child, View directTargetChild, View target, int nestedScrollAxes) {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
        super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
            nestedScrollAxes);
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, final FloatingActionMenu child,
      View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed);

    if (dyConsumed > 10 && !this.mIsAnimatingOut && !this.mIsAnimatingIn && child.getVisibility() == View.VISIBLE) {
      // User scrolled down and the FAB is currently visible -> hide the FAB
      if (child.isOpened()) {
        child.close(true);
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            animateOut(child);
          }
        }, 200);
      }
      else {
        animateOut(child);
      }
    } else if (dyConsumed < -10 && !this.mIsAnimatingOut && !this.mIsAnimatingIn && child.getVisibility() != View.VISIBLE) {
      // User scrolled up and the FAB is currently not visible -> show the FAB
      animateIn(child);
    }
  }

  // Same animation that FloatingActionButton.Behavior uses to hide the FAB when the AppBarLayout exits
  private void animateOut(final FloatingActionMenu button) {
    if (Build.VERSION.SDK_INT >= 14) {
      ViewCompat.animate(button).translationYBy(200F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
          .setListener(new ViewPropertyAnimatorListener() {
            public void onAnimationStart(View view) {
              FloatingActionMenuBehaviour.this.mIsAnimatingOut = true;
            }

            public void onAnimationCancel(View view) {
              FloatingActionMenuBehaviour.this.mIsAnimatingOut = false;
            }

            public void onAnimationEnd(View view) {
              FloatingActionMenuBehaviour.this.mIsAnimatingOut = false;
              view.setVisibility(View.GONE);
            }
          }).start();
    } else {
      Animation anim = AnimationUtils.loadAnimation(button.getContext(), R.anim.design_fab_out);
      anim.setInterpolator(INTERPOLATOR);
      anim.setDuration(200L);
      anim.setAnimationListener(new Animation.AnimationListener() {
        public void onAnimationStart(Animation animation) {
          FloatingActionMenuBehaviour.this.mIsAnimatingOut = true;
        }

        public void onAnimationEnd(Animation animation) {
          FloatingActionMenuBehaviour.this.mIsAnimatingOut = false;
          button.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(final Animation animation) {
        }
      });
      button.startAnimation(anim);
    }
  }

  // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
  private void animateIn(FloatingActionMenu button) {
    button.setVisibility(View.VISIBLE);
    if (Build.VERSION.SDK_INT >= 14) {
      ViewCompat.animate(button).translationYBy(-200F).alpha(1.0F)
          .setInterpolator(INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener() {
        @Override
        public void onAnimationStart(View view) {
          FloatingActionMenuBehaviour.this.mIsAnimatingIn = true;

        }

        @Override
        public void onAnimationEnd(View view) {
          FloatingActionMenuBehaviour.this.mIsAnimatingIn = false;

        }

        @Override
        public void onAnimationCancel(View view) {
          FloatingActionMenuBehaviour.this.mIsAnimatingIn = false;

        }
      })
          .start();
    } else {
      Animation anim = AnimationUtils.loadAnimation(button.getContext(), android.support.design.R.anim.design_fab_in);
      anim.setDuration(200L);
      anim.setInterpolator(INTERPOLATOR);
      button.startAnimation(anim);
    }
  }
}
