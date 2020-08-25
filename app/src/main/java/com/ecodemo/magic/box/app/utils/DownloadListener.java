package com.ecodemo.magic.box.app.utils;

public interface DownloadListener {
    void onFinished();

    void onProgress(float progress);

    void onPause();

    void onCancel();
}

