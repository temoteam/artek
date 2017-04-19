package ru.temoteam.artek.app.main;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import ru.temoteam.artek.app.Global;
import ru.temoteam.artek.app.R;

import static android.app.Activity.RESULT_OK;


public class Callback extends ArtekFragment {

    private static final int PICKFILE_RESULT_CODE = 1;
    EditText callbackMessage;
    CheckBox attachLogs;
    Spinner spinnerMethodReq;
    Spinner typeProblem;
    Button attachScreenshot;
    Button sendCallback;
    String[] methodsReq = {"Ответить через ВК (рекомендуется)", "Ответить в приложении (нестабильно)"};
    String[] typesProblem = {"Сообщение об ошибке", "Предложение по усовершенствованию"};
    int selectMethod = 1;
    int selectType = 1;


    String FilePath;
    String message;


    public Callback() {
        super.init(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_callback, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        callbackMessage = (EditText) getActivity().findViewById(R.id.callbackMessage);
        attachLogs = (CheckBox) getActivity().findViewById(R.id.attachLogs);
        spinnerMethodReq = (Spinner) getActivity().findViewById(R.id.spinnerMethodReq);
        typeProblem = (Spinner) getActivity().findViewById(R.id.typeProblem);
        attachScreenshot = (Button) getActivity().findViewById(R.id.attachScreenshot);
        sendCallback = (Button) getActivity().findViewById(R.id.sendCallback);


        ArrayAdapter<String> adapterMethodsReq = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, methodsReq);
        adapterMethodsReq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerMethodReq.setAdapter(adapterMethodsReq);
        spinnerMethodReq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                selectMethod = selectedItemPosition + 1;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> adapterTypesProblem = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typesProblem);
        adapterTypesProblem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        typeProblem.setAdapter(adapterTypesProblem);
        typeProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                selectType = selectedItemPosition + 1;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        attachLogs.setChecked(false);
        attachLogs.setActivated(false);
        attachLogs.setFocusable(false);
        attachLogs.setClickable(false);


        attachScreenshot.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

        sendCallback.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (callbackMessage.getText().toString() != "") {

                    message = callbackMessage.getText().toString();
                    Log.d("callback", "2");
                    new Send2Server().execute();

                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    FilePath = data.getData().getPath();
                    attachScreenshot.setText(FilePath.substring(FilePath.lastIndexOf("/") + 1, FilePath.length()));
                }
                break;
        }
    }


    private class Send2Server extends AsyncTask<String, Void, Boolean> {
        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("callback", "1");
            mDialog = ProgressDialog.show(getContext(), "Отправка сообщения", "Пожалуйста, подождите...", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                RequestParams request = new RequestParams();
                request.put("message", message);
                request.put("id_vk", Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID, null));
                request.put("google_token", Global.sharedPreferences.getString(Global.SharedPreferencesTags.GOOGLE_TOKEN, null));
                request.put("request", selectMethod);
                request.put("type", selectType);
                if (FilePath != "") {
                    request.put("screenshot", new File(FilePath));
                    Log.d("callback", "4");
                }
                SyncHttpClient client = new SyncHttpClient();

                client.post(getContext(), Global.server + "artek/msg/send.php", request, new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.print("Failed..");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.print("Success..");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mDialog.cancel();
            callbackMessage.setText("");
            message = "";
            FilePath = "";
            attachScreenshot.setText("Прикрепить скриншот");
            Snackbar mSnackbar = Snackbar
                    .make(getActivity().findViewById(R.id.mainview), "Ваше сообщение успешно отправлено", Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }
    }

}
