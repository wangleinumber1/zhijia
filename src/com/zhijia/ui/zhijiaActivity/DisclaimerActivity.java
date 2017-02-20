package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.zhijia.ui.R;

/**
 * 免责声明
 */
public class DisclaimerActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disclaimer);

        ((TextView) findViewById(R.id.disclaimer_detail)).setText(Html.fromHtml("置家网（http://www.zhijia.com，以下称“本网站”）免责声明系本网站保护用户使用安全的承诺，以及对本网站权益的保护声明。 鉴于网络的特性，本网站将无可避免的与您产生直接或间接的互动关系，故特此做出声明，请您务必仔细阅读：<br/><br/>1、凡以任何方式登陆本网站或直接、间接使用本网站资料者，视为自愿接受本网站声明的约束。<br/><br/>2、如果单位或个人认为本网站某部分内容有侵权嫌疑，敬请立即通知我们，我们将第一时间予以更该或删除。本网站并不承担查清事情的责任和证实事情公正性和合法性的责任，同时在事情查清前保留对该部分内容继续刊载的权利。<br/><br/>3、本网站对于任何包含、经由或连接、下载或从任何与本网站有关服务（以下简称为本服务）所获得的任何内容、信息或广告，不声明或保证其正确性或可靠性；并且对于用户经本服务上的广告、资讯或展示而购买、取得的任何产品、信息或资料，本网站不负保证责任。用户自行负担使用本网站服务的风险。<br/><br/>4、本网站已尽力确保所有资料是准确、完整及最新的。就该资料的针对性、精确性以及特定用途的适合性而言，本网站不可能也无义务做出最对应和最有成效的方案。所以对本网站所引用的资料，本网站概不承诺对其针对性、精确性以及特定用途的适合性负责，同时因依赖该资料所至的任何损失，本网站亦不承担任何法律责任。<br/><br/>5、本网站将对用户您所提供的资料进行严格的管理及保护，本网站将使用相应的技术，防止您的个人资料丢失、被盗用或遭篡改。<br/><br/>6、本声明未涉及的问题参见国家有关法律法规，当本声明与国家法律法规冲突时，以国家法律法规为准。<br/><br/>7、本网站之声明以及其修改权、更新权及最终解释权均属置家网所有。<br/>"));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}