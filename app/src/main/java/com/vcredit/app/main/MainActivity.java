package com.vcredit.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import butterknife.Unbinder;
import com.vcredit.base.AbsBaseActivity;
import com.vcredit.app.R;
import com.vcredit.global.App;
import com.vcredit.global.OnTabItemClickListenner;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shibenli on 2016/7/6.
 */
public class MainActivity extends AbsBaseActivity{

    @Override
    protected int layout() {
        return R.layout.main_navigation_activity;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instantiation();
    }

    /**
     * tab页初始化
     */
    protected void instantiation() {
        // tab按钮 与 关联模块
        tabMap = new HashMap<>();
        // 首页
//        tabMap.put(R.id.main_textBill, new Submodule(
//                null,
//                new int[]{},
//                (TextView) findViewById(R.id.main_textBill),
//                new BillFragment()));
//        //
//        tabMap.put(R.id.main_textCredit, new Submodule(
//                null,
//                new int[]{},
//                (TextView) findViewById(R.id.main_textCredit),
//                new CreditFragment()));
//        //
//        tabMap.put(R.id.main_tabMine, new Submodule(
//                null,
//                new int[]{},
//                (TextView) findViewById(R.id.main_textMine),
//                new MineFragment()));


        // 向fragment容器中添加首页
        toggleHomeFragment(false);
    }

    private void toggleFragment(Submodule submodule){
        toggleFragment(submodule, true);
    }

    /**
     * tab切换
     */
    private void toggleFragment(Submodule submodule, boolean withClick) {
        if (submodule == null){
            return;
        }

        if (submodule.fragmentView != null) {
            if (curSubmodule == submodule){
                if (withClick && submodule.fragmentView instanceof OnTabItemClickListenner){
                    ((OnTabItemClickListenner) submodule.fragmentView).onTabItemClick();
                    return;
                }
            }

            replaceFragment(R.id.main_fragment_container, submodule.fragmentView);
        }

        // Tab活动状态切换
        if (curSubmodule != null)
            curSubmodule.setTabStatus(false);
        submodule.setTabStatus(true);

        // 更新选中项
        curSubmodule = submodule;
    }

    /**
     * 切换页面到HomeFragment
     */
    private void toggleHomeFragment(boolean withClick) {
        Submodule submodule = tabMap.get(R.id.main_tabBill);
        toggleFragment(submodule, withClick);
    }

    @OnClick({R.id.main_tabBill,R.id.main_tabCredit, R.id.main_tabMine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tabBill: {
                toggleHomeFragment(true);
            }
            break;
            case R.id.main_tabCredit: {
                Submodule submodule = tabMap.get(R.id.main_tabCredit);
                toggleFragment(submodule);
            }
            break;
            case R.id.main_tabMine: {
                Submodule submodule = tabMap.get(R.id.main_tabMine);
                toggleFragment(submodule);
            }
            break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null){
            boolean returnHome = intent.getBooleanExtra("return_home", false);
            if (returnHome){
                toggleHomeFragment(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        App.getInstance().exitBy2Click(this);
    }


    /**
     * 子模块
     */
    private class Submodule {
        /**
         * 构造函数
         */
        public Submodule(TextView icon, int[] imgStrs, TextView title, Fragment fragmentView) {
            this.icon = icon;
            this.imgStrs = imgStrs;
            this.title = title;
            this.fragmentView = fragmentView;
        }

        // tab图片控件，元素1为默认图片，元素2为选中状态
        private TextView icon;

        private int[] imgStrs;

        private TextView title;

        // tab内容
        public Fragment fragmentView;

        // 设置tab图片
        public void setTabStatus(boolean isOpen) {
//            icon.setSelected(isOpen);
//            icon.setText(icon.getContext().getString(isOpen ? imgStrs[1] : imgStrs[0]));
//            title.setSelected(isOpen);
        }
    }

    /**
     * 将tab按钮与子模块建立关联
     */
    private HashMap<Integer, Submodule> tabMap;

    /**
     * 当前模块
     */
    private Submodule curSubmodule;
}
