// Generated by view binder compiler. Do not edit!
package com.hyunki.bard2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.hyunki.bard2.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NoteItemviewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView noteImageView;

  @NonNull
  public final TextView noteNameTextview;

  private NoteItemviewBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView noteImageView,
      @NonNull TextView noteNameTextview) {
    this.rootView = rootView;
    this.noteImageView = noteImageView;
    this.noteNameTextview = noteNameTextview;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NoteItemviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NoteItemviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.note_itemview, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NoteItemviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      ImageView noteImageView = rootView.findViewById(R.id.note_imageView);
      if (noteImageView == null) {
        missingId = "noteImageView";
        break missingId;
      }
      TextView noteNameTextview = rootView.findViewById(R.id.noteName_textview);
      if (noteNameTextview == null) {
        missingId = "noteNameTextview";
        break missingId;
      }
      return new NoteItemviewBinding((ConstraintLayout) rootView, noteImageView, noteNameTextview);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
