package com.fourd.moveedittext;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private  ScrollView scrollView;
    private EditText etAccount;
    private LinearLayout llAccount;
    private EditText etPsw;
    private LinearLayout llPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        etAccount = (EditText)findViewById(R.id.etAccount);
        llAccount = (LinearLayout)findViewById(R.id.llAccount);
        etPsw = (EditText)findViewById(R.id.etPsw);
        llPsw = (LinearLayout)findViewById(R.id.llPsw);
        etAccount.setOnFocusChangeListener(new OnMyFocusChangeListener(llAccount));
        etPsw.setOnFocusChangeListener(new OnMyFocusChangeListener(llPsw));

        etAccount.setOnClickListener(new OnMyClickListener(llAccount));
        etPsw.setOnClickListener(new OnMyClickListener(llPsw));
    }


    public class OnMyClickListener implements View.OnClickListener {
        private View toView;  //toView 为希望移到scrollview的子View的位置。
        public OnMyClickListener(View toView) {
            this.toView = toView;
        }

        @Override
        public void onClick(View view) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0, (int) toView.getY());
                        }
                    });
                }
            }, 100);
        }
    }


    public class OnMyFocusChangeListener implements View.OnFocusChangeListener {
        private View toView;  //toView 为希望移到scrollview的子View的位置。
        public OnMyFocusChangeListener(View toView) {
            this.toView = toView;
        }
        @Override
        public void onFocusChange(final View v, boolean hasFocus) {
            if(!hasFocus)
                return;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0, (int) toView.getY());
                        }
                    });
                }
            }, 100);
        }
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
