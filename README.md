# MoveEditText

![](https://github.com/vivianyan0818/screenshot/1.gif)

当页面有EditText时，会遇到软键盘将EditText遮住的情况。

这样用户体验极为不好。我们可以通过设置SOFT_INPUT_ADJUST_XXX来解决问题


SOFT_INPUT_ADJUST_NOTHING:         不调整(输入法完全直接覆盖住,未开放此参数)
SOFT_INPUT_ADJUST_PAN:                 把整个Layout顶上去露出获得焦点的EditText,不压缩多余空间，见图1
SOFT_INPUT_ADJUST_RESIZE:            整个Layout重新编排,重新分配多余空间，和adjustPan为2选1的值，见图2
SOFT_INPUT_ADJUST_UNSPECIFIED:  系统自己根据内容自行选择上两种方式的一种执行(默认配置)
##设置参数有两种方式

## 1) 在manifest中对应的Activity中设置

```


<activity
    android:name=".activity.LandingActivity"
    android:windowSoftInputMode="stateHidden|adjustPan"
    android:screenOrientation="portrait" />

```

##2)在Activity中的onCreate中setContentView之前写上

```


getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
```



图1：
![](https://github.com/vivianyan0818/screenshot/2.gif)


#2.设置adjustResize需注意：

#### 1）需要在EditText外层用ScrollView 包裹，并且需要注意是否 fitsSystemWindows设为true。

        如果ScrollView不是顶层layout，需要将可以压缩的顶层Layout设置为fitsSystemWindows设为true。

#### 2）使用Scrollview后需要将Scrollview滑动的位置指向当前EditText

方法：

```


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
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {              
                        scrollview.smoothScrollTo(0, (int) toView.getY());
                    }
                });
            }
        }, 100);
    }
}

private Handler handler = new Handler();
```







adjustResize的效果是
![](https://github.com/vivianyan0818/screenshot/2.gif)
