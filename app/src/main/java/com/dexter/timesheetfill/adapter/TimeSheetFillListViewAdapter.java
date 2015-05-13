package com.dexter.timesheetfill.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dexter.dex_erp.JSONParser;
import com.dexter.dex_erp.R;
import com.dexter.dex_erp.SessionManager;
import com.dexter.dex_erp.TimesheetfillActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeSheetFillListViewAdapter extends
        ArrayAdapter<TimeSheetFillData> {
    Context context;
    JSONParser jsonParser = new JSONParser();
    String strTimesheetsId = null;
    List<TimeSheetFillData> taskname;
    String userid;
    private Activity activity;
    private SessionManager session;

    public TimeSheetFillListViewAdapter(Activity a, int textViewResourceId,
                                        ArrayList<TimeSheetFillData> entries) {
        super(a, textViewResourceId, entries);
        this.taskname = entries;
        this.activity = a;
        session = new SessionManager(activity);
    }

    public TimeSheetFillListViewAdapter(Context context,
                                        List<TimeSheetFillData> items) {
        super(context, R.layout.list_row_timesheet, items);
        this.taskname = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            HashMap<String, String> user = session.getUserDetails();
            userid = user.get(com.dexter.dex_erp.SessionManager.KEY_ID);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error" + e);
        }

        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.list_row_timesheet, null);

            viewHolder = new ViewHolder();

            viewHolder.tvTimeSheetId = (TextView) view
                    .findViewById(R.id.txt_ts_timesheetid);
            viewHolder.tvProjectName = (TextView) view
                    .findViewById(R.id.txt_ts_projectname);
            viewHolder.tvTaskName = (TextView) view
                    .findViewById(R.id.txt_ts_taskname);
            viewHolder.tvSubTaskName = (TextView) view
                    .findViewById(R.id.txt_ts_subtaskname);
            viewHolder.tvComment = (TextView) view
                    .findViewById(R.id.txt_ts_comments);
            viewHolder.tvHours = (TextView) view
                    .findViewById(R.id.txt_ts_hours);

            viewHolder.imgBtnEdit = (ImageButton) view
                    .findViewById(R.id.btn_ts_edit);
            viewHolder.imgBtnDelete = (ImageButton) view
                    .findViewById(R.id.btn_ts_delete);

            viewHolder.imgBtnEdit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                }
            });

            viewHolder.imgBtnDelete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("DELETE BUTTON",
                            "Button Delete Clicked from com.dexter.timesheetfill.adapter");
                    strTimesheetsId = viewHolder.tvTimeSheetId.getText()
                            .toString();
                    final TimesheetfillActivity objTimesheetfillActivity = new TimesheetfillActivity();
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_dialog);

                    Button btn_ok = (Button) dialog
                            .findViewById(R.id.btn_delete_ok);
                    Button btn_cancel = (Button) dialog
                            .findViewById(R.id.btn_delete_cancel);
                    btn_ok.setOnClickListener(new View.OnClickListener() {

                        @SuppressLint("SimpleDateFormat")
                        @Override
                        public void onClick(View v) {

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String url = getContext()
                                                .getResources()
                                                .getString(
                                                        R.string.url_timesheet_insert)
                                                + objTimesheetfillActivity
                                                .getWeekMonthYear();
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair(
                                                "TimeSheetsId", strTimesheetsId));
                                        params.add(new BasicNameValuePair(
                                                "Delete", "0"));
                                        JSONObject json = jsonParser
                                                .makeHttpRequest(url, "POST",
                                                        params);

                                        // check log cat from response
                                        Log.d("Create Response",
                                                json.toString());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            thread.start();

                            dialog.dismiss();

                            Toast.makeText(
                                    getContext(),
                                    getContext().getResources().getString(
                                            R.string.data_success_delete),
                                    Toast.LENGTH_SHORT).show();

                        }

                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            view.setTag(viewHolder);
            // viewHolder.workAllocation.setTag(rowItem.get(position));
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        final TimeSheetFillData country = taskname.get(position);
        if (country != null) {

            holder.tvTimeSheetId.setText(country.getTIMESHEET_ID());
            holder.tvProjectName.setText(country.getTIMESHEET_PROJECTNAME());
            holder.tvTaskName.setText(country.getTIMESHEET_TASKNAME());
            holder.tvSubTaskName.setText(country.getTIMESHEET_SUBTASKNAME());
            holder.tvComment.setText(country.getTIMESHEET_COMMENT());
            holder.tvHours.setText(country.getTIMESHEET_HOURS());

        }

        return view;
    }

    /* private view holder class */
    private class ViewHolder {

        public TextView tvTimeSheetId;
        public TextView tvProjectName;
        public TextView tvTaskName;
        public TextView tvSubTaskName;
        public TextView tvComment;
        public TextView tvHours;

        public ImageButton imgBtnEdit;
        public ImageButton imgBtnDelete;

    }

}
