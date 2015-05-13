package com.dex.i_erp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dex.i_erp.data.ProjectDataHolder;
import com.dex.i_erp.data.TimesheetData;
import com.dexter.dex_erp.JSONParser;
import com.dexter.dex_erp.R;
import com.dexter.dex_erp.TimesheetfillActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimesheetCustomAdapter extends ArrayAdapter<TimesheetData> {
    Context context;
    int layoutResourceId;
    JSONParser jsonParser = new JSONParser();
    String strTimesheetsId = null;

    ArrayList<TimesheetData> data = new ArrayList<TimesheetData>();

    public TimesheetCustomAdapter(Context context, int layoutResourceId,
                                  ArrayList<TimesheetData> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, final View convertView,
                        final ViewGroup parent) {
        View row = convertView;
        ProjectDataHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ProjectDataHolder();

            holder.txt_ts_projectid = (TextView) row
                    .findViewById(R.id.txt_ts_projectid);
            holder.txt_ts_projectname = (TextView) row
                    .findViewById(R.id.txt_ts_projectname);
            holder.txt_ts_taskid = (TextView) row
                    .findViewById(R.id.txt_ts_taskid);
            holder.txt_ts_taskname = (TextView) row
                    .findViewById(R.id.txt_ts_taskname);
            holder.txt_ts_subtaskid = (TextView) row
                    .findViewById(R.id.txt_ts_subtaskid);
            holder.txt_ts_subtaskname = (TextView) row
                    .findViewById(R.id.txt_ts_subtaskname);
            holder.txt_ts_comments = (TextView) row
                    .findViewById(R.id.txt_ts_comments);
            holder.txt_ts_hours = (TextView) row
                    .findViewById(R.id.txt_ts_hours);
            holder.txt_ts_timesheetweek = (TextView) row
                    .findViewById(R.id.txt_ts_timesheetweek);
            holder.txt_ts_timesheetmonth = (TextView) row
                    .findViewById(R.id.txt_ts_timesheetmonth);
            holder.txt_ts_datetime = (TextView) row
                    .findViewById(R.id.txt_ts_datetime);
            holder.btn_ts_delete = (ImageButton) row
                    .findViewById(R.id.btn_ts_delete);
            holder.btn_ts_edit = (ImageButton) row
                    .findViewById(R.id.btn_ts_edit);

            row.setTag(holder);
        } else {
            holder = (ProjectDataHolder) row.getTag();
        }
        final TimesheetData timesheet_user_data = data.get(position);

        if (timesheet_user_data != null) {
            holder.txt_ts_projectid.setText(timesheet_user_data.projectid);
            holder.txt_ts_projectname.setText(timesheet_user_data.projectname);
            holder.txt_ts_taskid.setText(timesheet_user_data.taskid);
            holder.txt_ts_taskname.setText(timesheet_user_data.taskname);
            holder.txt_ts_subtaskid.setText(timesheet_user_data.subtaskid);
            holder.txt_ts_subtaskname.setText(timesheet_user_data.subtaskname);
            holder.txt_ts_comments.setText(timesheet_user_data.comments);
            holder.txt_ts_hours.setText(timesheet_user_data.hours);
            holder.txt_ts_timesheetweek
                    .setText(timesheet_user_data.timesheetweek);
            holder.txt_ts_timesheetmonth
                    .setText(timesheet_user_data.timesheetmonth);
            holder.txt_ts_datetime.setText(timesheet_user_data.datetime);

            holder.btn_ts_delete.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("SimpleDateFormat")
                public void onClick(final View v) {

                    Log.i("Delete Button Clicked", "**********");

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_dialog);

                    final TimesheetfillActivity objTimesheetfillActivity = new TimesheetfillActivity();
                    Button btn_delete_ok = (Button) dialog
                            .findViewById(R.id.btn_delete_ok);
                    Button btn_delete_cancel = (Button) dialog
                            .findViewById(R.id.btn_delete_cancel);

                    btn_delete_ok
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = getContext().getResources().getString(R.string.url_timesheet_insert) + objTimesheetfillActivity.getWeekMonthYear();
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("TimeSheetsId", strTimesheetsId));
                                    params.add(new BasicNameValuePair("Delete", "0"));
                                    JSONObject json = jsonParser.makeHttpRequest(url, "POST",
                                            params);

                                    // check log cat from response
                                    Log.d("Create Response", json.toString());

                                    dialog.dismiss();
                                }
                            });
                    /*
					 * @SuppressLint("SimpleDateFormat")
					 * 
					 * @Override public void onClick(View v) {
					 * 
					 * // DBAdapter_Timesheet dbadapter_ts = new
					 * DBAdapter_Timesheet( // context); // dbadapter_ts.open();
					 * // dbadapter_ts.deletetimesheetdata(Long //
					 * .parseLong(timesheet_user_data // .getKeyrowid())); //
					 * dbadapter_ts.close(); // //
					 * TimesheetfillActivity.timesheet_custom_Array //
					 * .remove(position); // //
					 * TimesheetfillActivity.timesheet_custom_Adapter //
					 * .notifyDataSetChanged();
					 * 
					 * dialog.dismiss(); Toast.makeText( getContext(),
					 * getContext().getResources().getString(
					 * R.string.data_success_delete),
					 * Toast.LENGTH_SHORT).show(); }
					 * 
					 * });
					 */
                    btn_delete_cancel
                            .setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                }
                            });

                    dialog.show();

                }
            });

            holder.btn_ts_edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    System.out.println("--------RowId---------"
                            + Long.parseLong(timesheet_user_data.getKeyrowid()));
                    ((TimesheetfillActivity) context).open_dialog();
                }
            });
        }

        return row;

    }

}
