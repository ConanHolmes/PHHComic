package com.ningjiahao.phhcomic.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ningjiahao.phhcomic.R;
import com.ningjiahao.phhcomic.base.BaseActivity;
import com.ningjiahao.phhcomic.bean.IsUpdateBean;
import com.ningjiahao.phhcomic.bean.ManHuaKuBean;
import com.ningjiahao.phhcomic.config.URLConstants;
import com.ningjiahao.phhcomic.fragment.FindFragment;
import com.ningjiahao.phhcomic.fragment.ManHuaKuFragment;
import com.ningjiahao.phhcomic.fragment.QuanZiFragment;
import com.ningjiahao.phhcomic.fragment.XiaoWoFragment;
import com.ningjiahao.phhcomic.interfaces.GetPartId;
import com.ningjiahao.phhcomic.service.DownLoadService;

import java.io.Serializable;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener ,GetPartId {
    private ManHuaKuFragment manHuaKuFragment;
    private QuanZiFragment quanZiFragment;
    private XiaoWoFragment xiaoWoFragment;
    private FindFragment findFragment;
    private RadioButton mainactivity_xiaoxu,mainactivity_faxian,mainactivity_manhuaku,mainactivity_quanzi;
    private FragmentManager fragmentManager;
    private ManHuaKuBean manHuaKuBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
        isUpdate();
    }

    private void initFragment() {
        manHuaKuBean= (ManHuaKuBean) getIntent().getSerializableExtra("key");
        manHuaKuFragment=new ManHuaKuFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("key",manHuaKuBean);
        manHuaKuFragment.setArguments(bundle);
        quanZiFragment=new QuanZiFragment();
        xiaoWoFragment=new XiaoWoFragment();
        findFragment=new FindFragment();
        FragmentTransaction fragmentTransation=fragmentManager.beginTransaction();
        fragmentTransation.add(R.id.Framelayout_manhuaku,manHuaKuFragment,"manHuaKuFragment");
        fragmentTransation.show(manHuaKuFragment);
        fragmentTransation.add(R.id.Framelayout_manhuaku,quanZiFragment,"quanZiFragment");
        fragmentTransation.hide(quanZiFragment);
        fragmentTransation.add(R.id.Framelayout_manhuaku,xiaoWoFragment,"xiaoWoFragment");
        fragmentTransation.hide(xiaoWoFragment);
        fragmentTransation.add(R.id.Framelayout_manhuaku,findFragment,"findFragment");
        fragmentTransation.hide(findFragment);
        fragmentTransation.commit();
    }

    private void initView() {
        mainactivity_xiaoxu= (RadioButton) findViewById(R.id.mainactivity_xiaoxu);
        mainactivity_xiaoxu.setOnClickListener(this);
        mainactivity_faxian= (RadioButton) findViewById(R.id.mainactivity_faxian);
        mainactivity_faxian.setOnClickListener(this);
        mainactivity_manhuaku= (RadioButton) findViewById(R.id.mainactivity_manhuaku);
        mainactivity_manhuaku.setOnClickListener(this);
        mainactivity_quanzi= (RadioButton) findViewById(R.id.mainactivity_quanzi);
        mainactivity_quanzi.setOnClickListener(this);
        fragmentManager=getSupportFragmentManager();

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransation=fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.mainactivity_xiaoxu:
                //fragmentTransation.replace(R.id.Framelayout_manhuaku,xiaoWoFragment);
                fragmentTransation.hide(manHuaKuFragment);
                fragmentTransation.hide(quanZiFragment);
                fragmentTransation.show(xiaoWoFragment);
                fragmentTransation.hide(findFragment);
                break;
            case R.id.mainactivity_faxian:
                fragmentTransation.hide(xiaoWoFragment);
                fragmentTransation.hide(manHuaKuFragment);
                fragmentTransation.show(findFragment);
                fragmentTransation.hide(quanZiFragment);
                break;
            case R.id.mainactivity_manhuaku:
                fragmentTransation.hide(xiaoWoFragment);
                fragmentTransation.hide(quanZiFragment);
                fragmentTransation.show(manHuaKuFragment);
                fragmentTransation.hide(findFragment);
                break;
            case R.id.mainactivity_quanzi:
                fragmentTransation.hide(xiaoWoFragment);
                fragmentTransation.hide(manHuaKuFragment);
                fragmentTransation.show(quanZiFragment);
                fragmentTransation.hide(findFragment);
                break;
        }
        fragmentTransation.commit();
    }

    @Override
    public void getPartid(int id){
        FragmentTransaction fragmentTransation=fragmentManager.beginTransaction();
        fragmentTransation.hide(xiaoWoFragment);
        fragmentTransation.hide(manHuaKuFragment);
        fragmentTransation.show(findFragment);
        fragmentTransation.hide(quanZiFragment);
        mainactivity_manhuaku.setSelected(false);
        fragmentTransation.commit();
    }
    public String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void isUpdate() {
        myRetrofitApi.getIsUpdateBean(URLConstants.URL_ISUPDATE_DATA)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IsUpdateBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(IsUpdateBean isUpdateBean) {
                        int version = isUpdateBean.getData().getVersion();
                        final String vsersionUrl = isUpdateBean.getData().getVsersionUrl();
                        int NowVersion=Integer.valueOf(getVersionCode(mContext));
                        if(NowVersion<version){
                            AlertDialog.Builder buider=new AlertDialog.Builder(mContext);
                            buider.setTitle("我们的漫画");
                            buider.setIcon(R.mipmap.icon);
                            buider.setMessage("发现有新的版本，点击确定更新");
                            buider.setNegativeButton("取消",null);
                            buider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this,DownLoadService.class);
                                    intent.putExtra("key",vsersionUrl);
                                    startService(intent);
                                }
                            });
                            buider.create();
                            buider.show();
                        }
                    }
                });
    }
}
