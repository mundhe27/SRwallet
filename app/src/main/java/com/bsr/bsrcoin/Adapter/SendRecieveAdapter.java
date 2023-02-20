package com.bsr.bsrcoin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bsr.bsrcoin.Models.ChequeModel;
import com.bsr.bsrcoin.MysqlConst.Constants;
import com.bsr.bsrcoin.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendRecieveAdapter extends RecyclerView.Adapter<SendRecieveAdapter.ViewHolder> {

    private final String type;
    private final ArrayList<ChequeModel> chequeModelArrayList;
    private final Context context;

    public SendRecieveAdapter(String type, ArrayList<ChequeModel> chequeModelArrayList, Context context) {
        this.type = type;
        this.chequeModelArrayList = chequeModelArrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.send_recieve_cheque_row, parent, false);
        return new SendRecieveAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if((!chequeModelArrayList.isEmpty())) {
            String[] statuses = {"Pending","Accepted","Returned"};
            ChequeModel model = chequeModelArrayList.get(position);
            holder.date.setText(model.getDate());
            holder.account.setText(model.getAccount());
            holder.amount.setText(model.getAmount());
            holder.status.setText(statuses[Integer.parseInt(model.getStatus())]);
            if(type.equalsIgnoreCase("receive")) {
                holder.user.setText("Sender");
                if(model.getStatus().equalsIgnoreCase("0")) {
                    holder.accept.setVisibility(View.VISIBLE);
                }
            } else {
                holder.user.setText("Receiver");
            }
            holder.accept.setOnClickListener(view -> {
                holder.accept.setEnabled(false);
                Volley.newRequestQueue(context).add(
                        new StringRequest(
                                Request.Method.POST,
                                Constants.url_accept_cheque,
                                response -> {
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                        if(!object.getString("message").equalsIgnoreCase("Arguments Not Specified")) {
                                            holder.accept.setEnabled(true);
                                            holder.accept.setVisibility(View.GONE);
                                            if(object.getString("message").equalsIgnoreCase("Cheque Accepted Successfully ")) {
                                                model.status = "1";
                                                holder.status.setText(statuses[1]);
                                            }
                                            else {
                                                model.status = "2";
                                                holder.status.setText(statuses[2]);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                },
                                Throwable::printStackTrace
                        ) {
                            @NotNull
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String,String> params = new HashMap<>();
                                params.put("chequeId" , model.getChequeId());
                                return params;
                            }
                        }
                );
            });

        } else {
            Toast.makeText(context, "No Cheques Received", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return chequeModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date, user, account, amount, status;
        AppCompatButton accept;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.c_date);
            user = itemView.findViewById(R.id.sender_reciever);
            account = itemView.findViewById(R.id.c_account);
            amount = itemView.findViewById(R.id.c_amount);
            status = itemView.findViewById(R.id.c_status);
            accept = itemView.findViewById(R.id.c_accept);
        }
    }
}
