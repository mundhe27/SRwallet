package com.bsr.bsrcoin.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.bsr.bsrcoin.Models.LoanModel;
import com.bsr.bsrcoin.Models.UserModel;
import com.bsr.bsrcoin.MysqlConst.Constants;
import com.bsr.bsrcoin.R;
import com.bsr.bsrcoin.SharedPrefmanager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewInsuranceAdapter extends RecyclerView.Adapter<ViewInsuranceAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<LoanModel> loanModels;
    private final Activity activity;
    private String amts, durations;

    public ViewInsuranceAdapter(Context context, ArrayList<LoanModel> loanModels, Activity activity) {
        this.context = context;
        this.loanModels = loanModels;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.loan_insurance_row, parent, false);
        return new ViewInsuranceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(loanModels.isEmpty()) {
            Toast.makeText(context, "No Insurance Till Now", Toast.LENGTH_SHORT).show();
        } else {
            LoanModel loanModel = loanModels.get(position);

            holder.reqinr.setVisibility(View.GONE);
            holder.rejectinr.setVisibility(View.GONE);
            holder.acceptinr.setVisibility(View.GONE);
            String[] statuses = {"Pending", "Accepted By Agent", "Accepted", "Rejected"};
            int[] Color = {context.getResources().getColor(R.color.purple_200),
                    context.getResources().getColor(R.color.yellow),
                    context.getResources().getColor(R.color.green),
                    context.getResources().getColor(R.color.error)};

            holder.roi.setVisibility(View.GONE);

            if(SharedPrefmanager.getInstance(context.getApplicationContext()).isAgent()) {
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.userid.setVisibility(View.VISIBLE);
                if(loanModel.getUpdate().equalsIgnoreCase("1"))
                    holder.Edit.setVisibility(View.VISIBLE);
                if(loanModel.getStatus().equalsIgnoreCase("0"))
                    holder.viewDetails.setVisibility(View.VISIBLE);
                if(loanModel.getStatus().equalsIgnoreCase("0"))
                {
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);
                } else {
                    holder.accept.setVisibility(View.GONE);
                    holder.reject.setVisibility(View.GONE);
                }
            } else if(SharedPrefmanager.getInstance(context.getApplicationContext()).isAdmin()) {
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.userid.setVisibility(View.VISIBLE);
                if(loanModel.getStatus().equalsIgnoreCase("1")) {
                    holder.Edit.setVisibility(View.VISIBLE);
                    holder.request.setVisibility(View.VISIBLE);
                }

                if(loanModel.getStatus().equalsIgnoreCase("1"))
                    holder.viewDetails.setVisibility(View.VISIBLE);
                if(loanModel.getStatus().equalsIgnoreCase("1"))
                {
                    holder.accept.setVisibility(View.VISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);
                } else {
                    holder.accept.setVisibility(View.GONE);
                    holder.reject.setVisibility(View.GONE);
                }
            }

            Glide.with(context).load(Constants.url_insurance_image + loanModel.getImage()).into(holder.asset);


            holder.id.setText(loanModel.getId());
            holder.type.setText(loanModel.getType());
            holder.duration.setText(loanModel.getDuration());
            holder.ammount.setText(loanModel.getAmount());
            holder.rate.setText(loanModel.getRate());
            holder.agent.setText(loanModel.getAgent());
            holder.status.setText(statuses[Integer.parseInt(loanModel.getStatus())]);
            holder.status.setTextColor(Color[Integer.parseInt(loanModel.getStatus())]);



            holder.request.setOnClickListener(v -> Volley.newRequestQueue(context).add(
                    new StringRequest(
                            Request.Method.POST,
                            Constants.url_insurance_request,
                            response -> {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    Toast.makeText(
                                            context,
                                            object.getString("message"),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            Throwable::printStackTrace
                    ) {
                        @NonNull
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("id", loanModel.getLoan_id());
                            return params;
                        }
                    }
            ));

            holder.viewDetails.setOnClickListener(v -> {
                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading User Data");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Volley.newRequestQueue(context).add(
                        new StringRequest(
                                Request.Method.GET,
                                Constants.url_read + loanModel.getId(),
                                response -> {
                                    progressDialog.dismiss();

                                    try {
                                        JSONObject object = new JSONObject(response);
                                        JSONObject user = object.getJSONArray("user").getJSONObject(0);

                                        UserModel userModel = new UserModel(user.getString("userId"),
                                                user.getString("name"),
                                                user.getString("email"),
                                                user.getString("phone_num"),
                                                user.getString("address"),
                                                user.getString("dob"),
                                                user.getString("occupation"),
                                                user.getString("agent"),
                                                user.getString("adhar_number"),
                                                user.getString("annual_Income"));

                                        View view = LayoutInflater.from(context).inflate(R.layout.view_user, activity.findViewById(R.id.userCard));
                                        TextView id = view.findViewById(R.id.user_id);
                                        TextView name = view.findViewById(R.id.user_name);
                                        TextView mail = view.findViewById(R.id.user_email);
                                        TextView phone = view.findViewById(R.id.user_phone);
                                        TextView addr = view.findViewById(R.id.user_address);
                                        TextView dob = view.findViewById(R.id.user_dob);
                                        TextView occup = view.findViewById(R.id.user_occupation);
                                        TextView agent = view.findViewById(R.id.user_agent);
                                        TextView aadhar = view.findViewById(R.id.user_aadhar);
                                        TextView income = view.findViewById(R.id.user_annual_income);

                                        id.setText(userModel.getId());
                                        name.setText(userModel.getName());
                                        mail.setText(userModel.getEmail());
                                        phone.setText(userModel.getPhone());
                                        addr.setText(userModel.getAddress());
                                        dob.setText(userModel.getDob());
                                        occup.setText(userModel.getOccupation());
                                        agent.setText(userModel.getAgent());
                                        aadhar.setText(userModel.getAadhar());
                                        income.setText(userModel.getIncome());

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setView(view);
                                        AlertDialog dialog = builder.create();
                                        view.findViewById(R.id.close).setOnClickListener(view1 -> {
                                            dialog.dismiss();
                                        });
                                        dialog.show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                },
                                error -> progressDialog.dismiss()
                        ){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("id", loanModel.getId());
                                return params;
                            }
                        }
                );
            });

            holder.Edit.setOnClickListener(v -> {

                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Updating Data..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                View view = LayoutInflater.from(context).inflate(R.layout.loan_insurance_update, activity.findViewById(R.id.updateCard));
                EditText amt, rate, duration;

                amt = view.findViewById(R.id.Edit_Amt);
                rate = view.findViewById(R.id.Edit_Rate);
                duration = view.findViewById(R.id.Edit_Duation);

                amt.setText(loanModel.getAmount());
                rate.setVisibility(View.GONE);
                duration.setText(loanModel.getDuration());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                view.findViewById(R.id.dialog_cancel).setOnClickListener(view1 -> dialog.dismiss());
                view.findViewById(R.id.dialog_update).setOnClickListener(view1 -> {

                    amts = amt.getText().toString();
                    durations = duration.getText().toString();

                    if(amts.equalsIgnoreCase(""))
                        amts = loanModel.getAmount();
                    if(durations.equalsIgnoreCase(""))
                        durations = loanModel.getDuration();

                    Volley.newRequestQueue(context).add(
                            new StringRequest(
                                    Request.Method.POST,
                                    Constants.url_insurance_update,
                                    (Response.Listener<String>) response -> {
                                        progressDialog.dismiss();
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                            if(object.getString("message").equalsIgnoreCase("Insurance was updated."))
                                            {
                                                holder.Edit.setVisibility(View.GONE);
                                                if(durations.indexOf("Year") > 0)
                                                    holder.duration.setText(durations);
                                                else
                                                    holder.duration.setText(durations + "Year");
                                                holder.ammount.setText(amts);
                                            }
                                            dialog.dismiss();
                                        } catch (JSONException e) { e.printStackTrace(); }

                                    },
                                    error -> progressDialog.dismiss()
                            ){
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("id", loanModel.getLoan_id());
                                    params.put("userId", loanModel.getId());
                                    params.put("insurance_amount", amts);
                                    params.put("insurance_type",loanModel.getType());
                                    params.put("agent_name", loanModel.getAgent());
                                    if(durations.indexOf("Year") > 0)
                                        params.put("duration", durations);
                                    else
                                        params.put("duration", durations + " Year");
                                    params.put("i_update", "0");
                                    return params;
                                }
                            }
                    );

                });
                dialog.show();
            });

            holder.accept.setOnClickListener(v -> Volley.newRequestQueue(context).add(
                    new StringRequest(
                            Request.Method.POST,
                            Constants.url_insurance_status,
                            response -> {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    if(object.getString("message").equalsIgnoreCase("Insurance was updated.")) {
                                        holder.frameLayout.setVisibility(View.GONE);
                                        holder.status.setTextColor(Color[1]);
                                        holder.status.setText(statuses[1]);
                                        holder.viewDetails.setVisibility(View.GONE);
                                        holder.Edit.setVisibility(View.GONE);
                                        if(SharedPrefmanager.getInstance(context.getApplicationContext()).isAdmin()) {
                                            holder.status.setTextColor(Color[2]);
                                            holder.status.setText(statuses[2]);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> {

                            }
                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("id", loanModel.getLoan_id());
                            if(SharedPrefmanager.getInstance(context.getApplicationContext()).isAdmin()) {
                                params.put("status", "2");
                            } else {
                                params.put("status", "1");
                            }
                            return params;
                        }
                    }
            ));

            holder.reject.setOnClickListener(v -> Volley.newRequestQueue(context).add(
                    new StringRequest(
                            Request.Method.POST,
                            Constants.url_insurance_status,
                            response -> {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    if(object.getString("message").equalsIgnoreCase("Insurance was updated.")) {
                                        holder.frameLayout.setVisibility(View.GONE);
                                        holder.status.setTextColor(Color[3]);
                                        holder.status.setText(statuses[3]);
                                        holder.viewDetails.setVisibility(View.GONE);
                                        holder.Edit.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> {

                            }
                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("id", loanModel.getLoan_id());
                            params.put("status", "3");
                            return params;
                        }
                    }
            ));
        }
    }

    @Override
    public int getItemCount() {
        return loanModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, type, duration, ammount, rate, agent, status;
        FrameLayout frameLayout;
        FloatingActionButton accept, Edit, reject;
        AppCompatButton viewDetails, request, reqinr, acceptinr, rejectinr;
        TableRow userid, roi;
        ImageView asset;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.l_i_user_id);
            type = itemView.findViewById(R.id.l_i_type);
            duration = itemView.findViewById(R.id.l_i_duration);
            ammount = itemView.findViewById(R.id.l_i_amount);
            rate = itemView.findViewById(R.id.l_i_roi);
            agent = itemView.findViewById(R.id.l_i_agent);
            status = itemView.findViewById(R.id.l_i_status);
            frameLayout = itemView.findViewById(R.id.a_r_e_frame);
            accept = itemView.findViewById(R.id.l_i_accept);
            reject = itemView.findViewById(R.id.l_i_reject);
            Edit = itemView.findViewById(R.id.l_i_edit);
            viewDetails = itemView.findViewById(R.id.viewProfile);
            userid = itemView.findViewById(R.id.userRow);
            roi = itemView.findViewById(R.id.roiRow);
            request = itemView.findViewById(R.id.send_request);
            reqinr = itemView.findViewById(R.id.request_inr);
            acceptinr = itemView.findViewById(R.id.accept_inr);
            rejectinr = itemView.findViewById(R.id.reject_inr);
            asset = itemView.findViewById(R.id.loan_asset);
        }
    }
}
