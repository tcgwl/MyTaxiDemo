package com.thunderhou.mytaxi.splash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.dalimao.mytaxi.R;
import com.thunderhou.mytaxi.common.util.ToastUtil;
import com.thunderhou.mytaxi.main.view.MainActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

//表示可以处理权限
@RuntimePermissions
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final AnimatedVectorDrawable anim1 = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim);
            final ImageView logo = ((ImageView) findViewById(R.id.logo));
            logo.setImageDrawable(anim1);
            anim1.start();
        }

        SplashActivityPermissionsDispatcher.gotoMainWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //在需要获取权限的地方注释
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void gotoMain() {
        /**
         * 延时 3 秒然后跳转到 main 页面
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000) ;
    }

    //提示用户为何要开启此权限
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showWhy(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("存储权限需要开启，否则将无法正常工作！")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed(); //再次执行权限请求
                    }
                })
                .show();
    }

    //用户选择拒绝时的提示
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDenied() {
        new AlertDialog.Builder(this)
                .setMessage("在设置-应用-权限中开启存储权限，以正常使用APP！")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    //用户选择不再询问后的提示
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNotAsk() {
        new AlertDialog.Builder(this)
                .setMessage("该APP需要存储权限，不开启将无法正常工作！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
}
