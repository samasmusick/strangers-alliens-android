package com.strangersalliens.catalogo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int FILE_PICKER_REQUEST = 4817;
    private static final String EDITOR_URL = "https://catalogo-virtual-strangers.samas-musick.workers.dev/editor.html";
    private WebView webView;
    private ValueCallback<Uri[]> fileChooserCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(0xFF070A05);
        getWindow().setNavigationBarColor(0xFF070A05);
        webView = new WebView(this);
        webView.setBackgroundColor(0xFF080B06);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String host = request.getUrl().getHost();
                if (host != null && (host.endsWith("workers.dev") || host.endsWith("supabase.co"))) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, request.getUrl())); }
                catch (Exception ignored) { Toast.makeText(MainActivity.this, "No se pudo abrir el enlace", Toast.LENGTH_SHORT).show(); }
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileChooserCallback != null) fileChooserCallback.onReceiveValue(null);
                fileChooserCallback = callback;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                String[] accept = params.getAcceptTypes();
                if (accept != null && accept.length > 0) intent.putExtra(Intent.EXTRA_MIME_TYPES, accept);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, params.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE);
                try { startActivityForResult(intent, FILE_PICKER_REQUEST); }
                catch (Exception e) { fileChooserCallback = null; Toast.makeText(MainActivity.this, "No se pudo abrir el selector de archivos", Toast.LENGTH_LONG).show(); return false; }
                return true;
            }
        });
        setContentView(webView);
        webView.loadUrl(EDITOR_URL);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != FILE_PICKER_REQUEST || fileChooserCallback == null) return;
        Uri[] results = null;
        if (resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int n = data.getClipData().getItemCount(); results = new Uri[n];
                for (int i = 0; i < n; i++) results[i] = data.getClipData().getItemAt(i).getUri();
            } else if (data.getData() != null) results = new Uri[]{data.getData()};
        }
        fileChooserCallback.onReceiveValue(results);
        fileChooserCallback = null;
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (fileChooserCallback != null) { fileChooserCallback.onReceiveValue(null); fileChooserCallback = null; }
        if (webView != null) { webView.stopLoading(); webView.destroy(); }
        super.onDestroy();
    }
}
