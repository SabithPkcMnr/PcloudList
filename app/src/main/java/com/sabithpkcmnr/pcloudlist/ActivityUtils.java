package com.sabithpkcmnr.pcloudlist;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ActivityUtils {

    //Add your Pcloud public link without last splash.
    //You can change the URL from the app level build.gradle file
    public static String PUBLIC_STORAGE_URL = BuildConfig.PCLOUD;

    public static AlertDialog myDialog;
    public static ActivityList myActivity;
    public static String RESPONSE_FILTER = "<script>";
    public static String RESPONSE_FILTER_END = "]";

    public static void requestServerForData(String serverUrl) {
        MaterialAlertDialogBuilder myDialogBuilder = new MaterialAlertDialogBuilder(myActivity);
        myDialogBuilder.setIcon(R.drawable.ic_getting_file);
        myDialogBuilder.setTitle("Connecting server");
        myDialogBuilder.setMessage("Getting info from our server, it will take a few seconds to collect from the Pcloud...");
        myDialogBuilder.setCancelable(false);
        myDialog = myDialogBuilder.create();
        myDialog.show();

        RequestQueue queue = Volley.newRequestQueue(myActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String filterA = response.trim().split(RESPONSE_FILTER)[1];
                            String filterD = filterA.substring(filterA.lastIndexOf("\"content\": [") + 1);
                            String finalResponse = "{\n\"" + filterD.split(RESPONSE_FILTER_END)[0] + "\n]\n}".trim();

                            try {
                                splitJsonDataToList(finalResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (ArrayIndexOutOfBoundsException ignored) {
                            Toast.makeText(myActivity, "Error: not accessible!", Toast.LENGTH_SHORT).show();
                            myDialog.dismiss();
                            myActivity.finish();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myDialog.dismiss();
                myActivity.finishAffinity();
            }
        });
        queue.add(stringRequest);
    }

    public static void splitJsonDataToList(String responseData) throws JSONException {
        JSONObject reader = new JSONObject(responseData);
        JSONArray contentData = reader.getJSONArray("content");

        if (contentData.length() > 0) {
            ArrayList<ModelFile> modelFile = new ArrayList<>();
            for (int a = 0; a < contentData.length(); a++) {
                JSONObject singleItem = contentData.getJSONObject(a);
                String itemSize = "";
                try {
                    itemSize = String.valueOf(singleItem.getInt("size"));
                } catch (JSONException ignored) {
                }
                String itemName = singleItem.getString("name");
                String itemModified = singleItem.getString("modified");
                String itemIcon = singleItem.getString("icon");
                modelFile.add(new ModelFile(itemName, itemSize, itemModified, itemIcon));
            }
            new ViewModelProvider(myActivity).get(ServerViewModel.class).getListData().setValue(modelFile);
            myDialog.dismiss();

        } else {
            Toast.makeText(myActivity, "Empty folder!", Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
            myActivity.finish();
        }
    }

    public static String getFileSize(String fileSize) {
        long finalFileSize = Long.parseLong(fileSize);
        if (finalFileSize <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(finalFileSize) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(finalFileSize / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
