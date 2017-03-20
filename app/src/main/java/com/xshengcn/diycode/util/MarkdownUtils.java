package com.xshengcn.diycode.util;

import android.support.annotation.NonNull;
import android.widget.EditText;

public class MarkdownUtils {

    public static void addCode(@NonNull EditText editText, String category) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        boolean newLine = hasNewLine(source, selectionStart);
        String result = (newLine ? "" : "\n")
                + String.format("```%s\n%s\n```\n", category, substring);
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(selectionStart + result.length() - 5);
    }

    public static void addImage(@NonNull EditText editText, String desc, String url) {
        int selectionStart = editText.getSelectionStart();
        String result = String.format("![%s](%s)\n", desc, url);
        int length = selectionStart + result.length();
        editText.getText().insert(selectionStart, result);
        editText.setSelection(length);
    }

    public static void addLink(@NonNull EditText editText, String desc, String url) {
        int selectionStart = editText.getSelectionStart();
        String result = String.format("[%s](%s)\n", desc, url);
        int length = selectionStart + result.length();
        editText.getText().insert(selectionStart, result);
        editText.setSelection(length);
    }

    private static boolean hasNewLine(@NonNull String source, int selectionStart) {
        try {
            if (source.isEmpty()) {
                return true;
            }
            source = source.substring(0, selectionStart);
            return source.charAt(source.length() - 1) == 10;
        } catch (StringIndexOutOfBoundsException e) {
            return true;
        }
    }
}
