package spikeking.github.com.testwebintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 网页Activity
 * <p/>
 * Created by wangchenlong on 15/12/7.
 */
public class WebIntentActivity extends Activity {

    @Bind(R.id.web_intent_et_title) EditText mEtTitle;
    @Bind(R.id.web_intent_et_link) EditText mEtLink;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        ButterKnife.bind(this);

        // 获取WebIntent信息
        if (isShareIntent()) {
            ShareCompat.IntentReader intentReader = ShareCompat.IntentReader.from(this);
            mEtLink.setText(intentReader.getText());
            mEtTitle.setText(intentReader.getSubject());
        }
    }

    @Override protected void onResume() {
        super.onResume();
        // 底部出现动画
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
    }

    // 判断是不是WebIntent
    private boolean isShareIntent() {
        return getIntent() != null && Intent.ACTION_SEND.equals(getIntent().getAction());
    }

    @Override public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
