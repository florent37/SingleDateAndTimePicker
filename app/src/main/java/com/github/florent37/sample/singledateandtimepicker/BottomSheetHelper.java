package com.github.florent37.sample.singledateandtimepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class BottomSheetHelper {

    public interface Listener {
        void onOpen();
        void onClose();
    }

    private View view;
    private View bottom_sheet_background;

    private Listener listener;

    public BottomSheetHelper setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public BottomSheetHelper(Context context, int layoutId) {
        if(context instanceof Activity) {
            final Activity activity = (Activity) context;
            this.view = LayoutInflater.from(context).inflate(layoutId, null);
            final ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            content.addView(view);
            this.view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    View v = BottomSheetHelper.this.view;
                    v.setTranslationY(v.getHeight());
                    v.getViewTreeObserver().removeOnPreDrawListener(this);
                    v.setVisibility(View.INVISIBLE);
                    return false;
                }
            });

            bottom_sheet_background = view.findViewById(R.id.bottom_sheet_background);
            bottom_sheet_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tryHide();
                }
            });
        }
    }

    public void display() {
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getHeight(), 0);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(listener != null){
                    listener.onOpen();
                }
            }
        });
        objectAnimator.start();
    }

    public void hide() {
        view.setVisibility(View.VISIBLE);
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, view.getHeight());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                if(listener != null){
                    listener.onClose();
                }
            }
        });
        objectAnimator.start();
    }

    public void tryHide() {
        if (view.getVisibility() == View.VISIBLE) {
            hide();
        }
    }
}
