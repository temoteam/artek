package org.artek.app.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.artek.app.ExceptionHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Updater {

    private final int THIS_VERSION = 0;
    private final String LAST_API_FILE_PACH = "http://lohness.com/artek/update/update.txt";
    private int lastVertion;
    private String updateFile;
    private String updateURL;

    private Activity activity;


    public Updater(Activity activity) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        this.activity = activity;
        updateFile = Environment.getExternalStorageDirectory().getPath() + "/update.apk";
        new CheckerForUpdates().execute();
    }


    public boolean checkForUpdates() throws IOException {
        getLastVersion();
        Log.i("lastVertion", "" + lastVertion);
        return THIS_VERSION < lastVertion;

    }

    public String getUpdate() throws IOException {

        File update = new File(updateFile);
        if (update.exists())
            update.delete();
        update = new File(updateFile);
        update.createNewFile();
        //getUpdate(activity.getApplicationContext());
        download(updateURL, update);
        return updateFile;
    }


    public String getUpdate(Context context) {
        File update = new File(Environment.DIRECTORY_DOWNLOADS + "/update.apk");
        if (update.exists())
            update.delete();
        downloadByDownloader(context, Uri.parse(updateURL));

        return Environment.DIRECTORY_DOWNLOADS + "/update.apk";
    }


    private void download(String urlStr, File file) throws IOException {
        URL url = new URL(urlStr);
        Log.i("download", urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    private void downloadByDownloader(Context context, Uri uri) {
        DownloadManager.Request r = new DownloadManager.Request(uri);
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
        r.allowScanningByMediaScanner();
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        long i = dm.enqueue(r);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private void getLastVersion() throws IOException {
        URL url = new URL(LAST_API_FILE_PACH);
        Scanner in = new Scanner(url.openStream());
        lastVertion = in.nextInt();
        updateURL = in.next();

    }

    class CheckerForUpdates extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return checkForUpdates();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.i("updates", "" + aBoolean);
            if (aBoolean) {
                AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(activity);
                ad.setTitle("Доступно обновление");  // заголовок
                ad.setMessage("Текущая верся программы " + THIS_VERSION + ", последняя версия " + lastVertion); // сообщение
                ad.setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        /*
                        Toast.makeText(activity.getApplicationContext(), "Началась загрузка обновления",
                                Toast.LENGTH_SHORT).show();
                        new InstallUpdate().execute();*/
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lohness.com/artek/app/index.php"));
                        activity.startActivity(intent);
                    }
                });
                ad.setCancelable(false);
                ad.setNegativeButton("Обновоить позже", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.show();
            } else {
                Toast.makeText(activity.getApplicationContext(), "Обновления не обнаружены",
                        Toast.LENGTH_SHORT).show();
            }
        }

        class InstallUpdate extends AsyncTask<Void, Void, String> {
            private ProgressDialog dialog;

            public InstallUpdate() {
                dialog = new ProgressDialog(activity);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                this.dialog.setMessage("Загрузка");
                this.dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    getUpdate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String aString) {
                super.onPostExecute(aString);
                if (aString != null) {
                    Log.i("downloadedfile", aString + new File(aString).exists());

                    if (new File(aString).exists()) {
                        Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                        install.setData(Uri.fromFile(new File(aString)));
                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                        install.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, activity.getApplicationInfo().packageName);
                        install.putExtra(Intent.EXTRA_ALLOW_REPLACE, true);
                        activity.startActivity(install);

                    }
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


            }
        }
    }
}

