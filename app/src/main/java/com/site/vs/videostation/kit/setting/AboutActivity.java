package com.site.vs.videostation.kit.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import com.site.vs.videostation.Config;
import com.site.vs.videostation.kit.WfcBaseActivity;
import com.site.vs.videostation.kit.WfcWebViewActivity;
import com.site.vs.videostation.R;

public class AboutActivity extends WfcBaseActivity {

    @BindView(R.id.infoTextView)
    TextView infoTextView;

    @Override
    protected int contentLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void afterViews() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            String info = packageInfo.packageName + "\n"
                    + packageInfo.versionCode + " " + packageInfo.versionName + "\n"
                    + Config.IM_SERVER_HOST + " " + Config.IM_SERVER_PORT + "\n"
                    + Config.APP_SERVER_HOST + " " + Config.APP_SERVER_PORT + "\n"
                    + Config.ICE_ADDRESS + " " + Config.ICE_USERNAME + " " + Config.ICE_PASSWORD + "\n";

            infoTextView.setText(info);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.introOptionItemView)
    public void intro() {
        WfcWebViewActivity.loadUrl(this, "野火IM功能介绍", "http://docs.wildfirechat.cn/");
    }

    @OnClick(R.id.agreementOptionItemView)
    public void agreement() {
        WfcWebViewActivity.loadUrl(this, "野火IM用户协议", "http://www.wildfirechat.cn/firechat_user_agreement.html");
    }

    @OnClick(R.id.privacyOptionItemView)
    public void privacy() {
        WfcWebViewActivity.loadUrl(this, "野火IM个人信息保护政策", "http://www.wildfirechat.cn/firechat_user_privacy.html");
    }
}