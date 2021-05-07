package com.sabithpkcmnr.pcloudlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MyViewHolder> {

    Context context;
    String extraUrlData;
    ArrayList<ModelFile> modelFile;

    public AdapterList(Context context, ArrayList<ModelFile> modelFile, String extraUrlData) {
        this.extraUrlData = extraUrlData;
        this.modelFile = modelFile;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterList.MyViewHolder holder, int position) {
        holder.itemName.setText(modelFile.get(position).getName());
        holder.itemDate.setText(modelFile.get(position).getDate().replace("+0000",""));
        String stringFileSize = modelFile.get(position).getSize();

        if (stringFileSize != null && stringFileSize.length()>2){
            holder.itemSize.setText(ActivityUtils.getFileSize(stringFileSize));
            holder.itemSize.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load("https://pcdn-filedn.pcloud.com/img/icons/16/" + modelFile.get(position).getIcon() + ".png")
                .into(holder.itemIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFile;
                String urlToPass;
                String stringFileExtension;
                String stringName = modelFile.get(position).getName();
                String extraTitleData = extraUrlData + " > " + modelFile.get(position).getName();

                try{
                    stringFileExtension = stringName.substring(stringName.lastIndexOf("."));
                    isFile = true;
                } catch (StringIndexOutOfBoundsException ignored){
                    isFile = false;
                }

                if (isFile){
                    MaterialAlertDialogBuilder myDialogBuilder = new MaterialAlertDialogBuilder(context);
                    myDialogBuilder.setIcon(R.drawable.ic_open_browser);
                    myDialogBuilder.setTitle("Open file using browser");
                    myDialogBuilder.setMessage("What you selected is a file and therefore app cannot access it. Do you want to open it using the browser app installed?");
                    myDialogBuilder.setCancelable(false);
                    myDialogBuilder.setPositiveButton("Open Link", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String webUrl;
                            if (extraUrlData != null && extraUrlData.length()>0){
                                webUrl = extraUrlData + "/" + modelFile.get(position).getName();

                            } else {
                                webUrl = "/" + modelFile.get(position).getName();
                            }
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(ActivityUtils.PUBLIC_STORAGE_URL + webUrl)));
                        }
                    });
                    myDialogBuilder.setNegativeButton("Cancel", null);
                    myDialogBuilder.create().show();



                } else {
                    if (extraUrlData != null && extraUrlData.length()>0){
                        urlToPass = extraUrlData + "/" + modelFile.get(position).getName();

                    } else {
                        urlToPass = "/" + modelFile.get(position).getName();
                    }
                    context.startActivity(new Intent(context, ActivityList.class)
                            .putExtra("extraUrlData", urlToPass)
                            .putExtra("extraTitleData",extraTitleData));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelFile.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView itemIcon;
        TextView itemName, itemSize, itemDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById(R.id.itemIcon);
            itemName = itemView.findViewById(R.id.itemName);
            itemSize = itemView.findViewById(R.id.itemSize);
            itemDate = itemView.findViewById(R.id.itemDate);
        }
    }

}
