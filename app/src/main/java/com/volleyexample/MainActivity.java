package com.volleyexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    String url = "http://192.168.1.18/245webserver/trackertest/webservice/checkvendor/";
    String strUsername, strPassword, strGroupname;
    EditText uName, pWord, gpName;
    ProgressDialog mProgress;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uName = (EditText) findViewById(R.id.editText);
        pWord = (EditText) findViewById(R.id.editText2);
        gpName = (EditText)findViewById(R.id.editText3);
        register = (Button) findViewById(R.id.button);

        mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);

        register.setOnClickListener(this);
    }

        private void getJsonObjectReq(final String strUsername, final String strPassword, final String strGroupname){
            showProgressDialog();
            JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG + "Response", jsonObject.toString());
                    hideProgressDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG + "Error", volleyError.toString());
                    hideProgressDialog();
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String,String>();
                    params.put("username", strUsername);
                    params.put("password", strPassword);
                    params.put("groupname", strGroupname);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(jObjReq,"json_object_request");

        }

    @Override
    public void onClick(View v) {
        if(v == register){
            strUsername = uName.getText().toString().trim();
            strPassword = pWord.getText().toString().trim();
            strGroupname = gpName.getText().toString().trim();
            getJsonObjectReq(strUsername, strPassword, strGroupname);
        }
    }

    private void showProgressDialog(){
        if(!mProgress.isShowing()){
            mProgress.show();
        }
    }

    private void hideProgressDialog(){
        if(mProgress.isShowing()){
            mProgress.hide();
        }
    }
}
