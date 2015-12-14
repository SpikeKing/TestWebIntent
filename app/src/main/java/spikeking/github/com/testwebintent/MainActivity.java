package spikeking.github.com.testwebintent;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 测试WebIntent的Demo
 *
 * @author C.L.Wang
 */
public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "DEBUG-WCL: " + MainActivity.class.getSimpleName();

    private static final String FILE_NAME = "file:///android_asset/web_intent.html";

    @Bind(R.id.main_wv_web) WebView mWvWeb; // WebView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转WebIntentActivity
                startActivity(new Intent(MainActivity.this, WebIntentActivity.class));
            }
        });

        mWvWeb.loadUrl(FILE_NAME);
    }

    @Override public void onBackPressed() {
        // 优先后退网页
        if (mWvWeb.canGoBack()) {
            mWvWeb.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // 打开浏览器选项
        if (id == R.id.action_open_in_browser) {
            // 获取文件名, 打开assets文件使用文件名
            String[] as = FILE_NAME.split("/");
            openUrlInBrowser(as[as.length - 1]);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 在浏览器中打开
     *
     * @param url 链接(本地HTML或者网络链接)
     */
    private void openUrlInBrowser(String url) {
        Uri uri;
        if (url.endsWith(".html")) { // 文件
            uri = Uri.fromFile(createFileFromInputStream(url));
        } else { // 链接
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            uri = Uri.parse(url);
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // 启动浏览器, 谷歌浏览器, 小米手机浏览器支持, 其他手机或浏览器不支持.
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "没有应用处理这个请求. 请安装浏览器.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * 存储assets内的文件
     *
     * @param url 文件名
     * @return 文件类(File)
     */
    private File createFileFromInputStream(String url) {
        try {
            // 打开Assets内的文件
            InputStream inputStream = getAssets().open(url);
            // 存储位置 /sdcard
            File file = new File(
                    Environment.getExternalStorageDirectory().getPath(), url);
            OutputStream outputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
