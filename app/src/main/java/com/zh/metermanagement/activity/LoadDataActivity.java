package com.zh.metermanagement.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shen.sweetdialog.SweetAlertDialog;
import com.zh.metermanagement.R;
import com.zh.metermanagement.activity.base.BaseActivity;
import com.zh.metermanagement.application.MyApplication;
import com.zh.metermanagement.bean.MeterBean;
import com.zh.metermanagement.utils.LogUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/4.
 */

public class LoadDataActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    /** 加载数据 -- 按钮 */
    Button mBtnLoadData;

    @Override
    public int getContentLayout() {
        return R.layout.activity_load_data;
    }

    @Override
    public void initTitleListener(TextView tvTitle, Button btnBack, Button btnMenu) {
        mTvTitle = tvTitle;
        mBtnBack = btnBack;
        mBtnMenu = btnMenu;

        mBtnBack.setOnClickListener(this);
        mBtnMenu.setOnClickListener(this);
    }

    @Override
    public void initTitleData(TextView tvTitle, Button btnBack, Button btnMenu) {
        mTvTitle.setVisibility(View.VISIBLE);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnMenu.setVisibility(View.GONE);

        mTvTitle.setText("加载数据");
    }

    @Override
    public void initView() {
        mBtnLoadData = (Button) findViewById(R.id.btn_loadDate);
    }

    @Override
    public void initListener() {
        mBtnLoadData.setOnClickListener(this);
    }

    @Override
    public void initData() {


    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
                break;

            case R.id.btn_loadDate:

                SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("此操作会清空本地数据库\n并将import.xls加载到本地数据库")
                        .setConfirmText("确认")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                showLoadingDialog("","导入中...");
                                taskPresenter.importExcelToDb(importObserver);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });

                // mSweetAlertDialog.setCancelable(false);
                mSweetAlertDialog.show();

                break;
        }
    }



    /**
     * 将数据从excel中读取到db
     * rxjava -- 主线程
     */
    Observer importObserver = new Observer<Long>() {

        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Long aLong) {

        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            closeDialog();
            showToast("加载数据失败");
        }

        @Override
        public void onComplete() {
            //closeDialog();

            //showLoadingDialog("","加载中...");
            taskPresenter.readDbToBean(readObserver);
        }
    };



    /**
     * 将数据从数据库读取到内存中
     * rxjava -- 主线程
     */
    Observer readObserver = new Observer<List<MeterBean>>() {

        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<MeterBean> meterBeen) {
            LogUtils.i("meterBeen.size()" + meterBeen.size());
            showToast("导入了" + meterBeen.size() + "条数据");
            MyApplication.setMeterBeanList(meterBeen);
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            closeDialog();

            showToast("加载数据失败");
        }

        @Override
        public void onComplete() {
            closeDialog();

            //showToast("加载数据成功");
        }
    };
}
