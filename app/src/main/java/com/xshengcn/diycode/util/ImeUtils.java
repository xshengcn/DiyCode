package com.xshengcn.diycode.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ImeUtils {

  private ImeUtils() {
  }

  public static void showIme(@NonNull View view) {
    InputMethodManager imm =
        (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
  }

  public static void hideIme(@NonNull View view) {
    InputMethodManager imm =
        (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void showImeForced(Context context) {
    InputMethodManager imm =
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
  }
}
