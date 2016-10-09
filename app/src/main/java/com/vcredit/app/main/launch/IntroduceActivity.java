package com.vcredit.app.main.launch;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import com.vcredit.app.R;
import com.vcredit.app.main.common.BaseLoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2016/3/30.
 */
public class IntroduceActivity extends BaseLoginActivity {
    @BindView(R.id.intr_viewpager)
    ViewPager intrViewpager;
    @BindView(R.id.intr_view1)
    View intrView1;
    @BindView(R.id.intr_view2)
    View intrView2;
    @BindView(R.id.intr_view3)
    View intrView3;
    @BindView(R.id.intr_btn_close)
    Button intrBtnClose;
    private int[] RES = new int[]{R.mipmap.guide_pages01, R.mipmap.guide_pages02, R.mipmap.guide_pages03};
    private List<View> viewList = new ArrayList<>();
    private int cache = 0;

    @Override
    protected int layout() {
        return R.layout.introduce_activity;
    }

    protected void initData() {
        viewList.add(intrView1);
        viewList.add(intrView2);
        viewList.add(intrView3);
    }

    protected void dataBind() {
        IntroduceAdapter adapter = new IntroduceAdapter(this, RES, getIntent());
        intrViewpager.setAdapter(adapter);
        intrViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                line(cache, position);
                cache = position;
                intrBtnClose.setVisibility(position != (RES.length - 1) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        intrBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不是自动登录
                if(!mAutoLogin) {
                    openHomePage();
                }else{
                    //如果是自动登录
                    login();
                }
            }
        });
    }

    private void line(int previous, int current) {
        if (previous >= 0)
            viewList.get(previous).setBackgroundResource(R.drawable.shape_dotted_normal);
        if (current >= 0)
            viewList.get(current).setBackgroundResource(R.drawable.shape_dotted_select);
    }

    /**
     * 启动当前界面（一份代码，到处启动）
     *
     * @param context
     */
    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, IntroduceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(intent);
    }
}

class IntroduceAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mRes;
    private Intent intent;

    public IntroduceAdapter(Context context, int[] res, Intent intent) {
        mContext = context;
        mRes = res;
        this.intent = intent;
    }

    @Override
    public int getCount() {
        return mRes.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.introduce_activity_item, container,false);
        ((ImageView) view.findViewById(R.id.introduce_img))
                .setImageResource(mRes[position]);
        container.addView(view);
        return view;
    }
}
