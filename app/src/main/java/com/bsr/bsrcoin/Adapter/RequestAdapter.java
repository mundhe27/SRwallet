package com.bsr.bsrcoin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bsr.bsrcoin.Models.AddRequestModel;
import com.bsr.bsrcoin.MysqlConst.Constants;
import com.bsr.bsrcoin.R;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<AddRequestModel> addRequestModelArrayList;
    public RequestAdapter(Context context, ArrayList<AddRequestModel> addRequestModelArrayList) {
        this.context = context;
        this.addRequestModelArrayList = addRequestModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_view, parent, false);
        return new RequestAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!addRequestModelArrayList.isEmpty()) {
            AddRequestModel model = addRequestModelArrayList.get(position);
            holder.reqid.setText("Request Id : " + model.getReqid());
            holder.userId.setText("User Id    : " + model.getUserid());
            holder.amount.setText("Amount     : " + model.getAmount());
            Glide.with(context).load(model.getImage()).into(holder.image);

            Response.Listener<String> listener = response -> {
                try {
                    JSONObject object = new JSONObject(response);
                    holder.accept.setVisibility(View.GONE);
                    holder.reject.setVisibility(View.GONE);
                    Toast.makeText(context,object.getString("message"),Toast.LENGTH_SHORT).show();
                } catch (Exception e ){
                    e.printStackTrace();
                }
            };


            holder.accept.setOnClickListener(view -> {
                holder.accept.setEnabled(false);
                holder.reject.setEnabled(false);

                Volley.newRequestQueue(context).add(
                        new StringRequest(
                                Request.Method.POST,
                                Constants.url_set_status,
                                listener,
                                Throwable::printStackTrace
                        ){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();
                                map.put("reqid",model.getReqid());
                                map.put("status","1");
                                return map;
                            }
                        }
                );
            });
            holder.reject.setOnClickListener(view ->{
                holder.accept.setEnabled(false);
                holder.reject.setEnabled(false);

                Volley.newRequestQueue(context).add(new StringRequest(
                    Request.Method.POST,
                    Constants.url_set_status,
                    listener,
                    Throwable::printStackTrace
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("reqid",model.getReqid());
                    map.put("status","2");
                    return map;
                }
            });

            });
        }
    }

    @Override
    public int getItemCount() {
        return addRequestModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView reqid, userId, amount;
        FloatingActionButton accept, reject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.request_image);
            reqid = itemView.findViewById(R.id.text_req_id);
            userId = itemView.findViewById(R.id.text_userid);
            amount = itemView.findViewById(R.id.text_amount);
            accept = itemView.findViewById(R.id.req_accept);
            reject = itemView.findViewById(R.id.req_reject);
        }
    }
}
