package com.dexter.dex_erp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dex.i_erp.adapter.TimesheetCustomAdapter;
import com.dex.i_erp.data.ProjectList;
import com.dex.i_erp.data.SubTaskList;
import com.dex.i_erp.data.TaskList;
import com.dex.i_erp.data.TimesheetData;
import com.dexter.timesheetfill.adapter.TimeSheetFillData;
import com.dexter.timesheetfill.adapter.TimeSheetFillListViewAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ShowToast")
public class TimesheetfillActivity extends ActionBarActivity {

    private static final String KEY_ProjectId = "ProjectId";
    private static final String KEY_ProjectName = "ProjectName";
    private static final String KEY_TaskId = "TaskId";
    private static final String KEY_TaskName = "TaskName";
    private static final String KEY_SubTaskId = "SubTaskId";
    private static final String KEY_SubTaskName = "SubTaskName";
    static public ArrayList<TimesheetData> timesheet_custom_Array = new ArrayList<TimesheetData>();
    static public TimesheetCustomAdapter timesheet_custom_Adapter;
    final Context context = this;
    public ProgressDialog pDialog, pDialogBisuness;
    String pref_pts, str_week, str_month, str_year, str_projectname,
            str_projectid, str_projecttaskname, str_projecttaskid,
            str_projectsubtaskid, str_projectsubtaskname, ts_project_id,
            ts_project_name, ts_project_task_id, ts_project_task_name,
            ts_project_subtask_id, ts_project_subtask_name,
            ts_project_comments = null, ts_project_hours = null,
            ts_timesheetweek, ts_timesheetmonth, ts_project_datetime;
    TextView txt_weekname, txt_w_m_y, txt_totalhr;
    Button btn_dialog_ok, btn_dialog_cancel;
    ImageButton btn_add;
    Spinner actv_project, actv_task, actv_subtask;
    double dbltotalhours = 0.0;
    EditText et_comments, et_no_of_hours;
    ListView lv_timesheet;
    SharedPreferences sharedpreferences;
    Editor editor;
    String strProjectId = null, strTaskId = null, strSubTaskId = null;
    ActionBar ab;
    JSONParser jsonParser = new JSONParser();
    AsyncJSONParser myprojectdata, myprojecttaskdata, myprojectsubtaskdata;
    JSONObject projectjson, taskjson, subtaskjson, jsonObject_project,
            jsonObject_project_task, jsonObject_project_subtask;
    JSONArray project_data = null, task_data = null, subtask_data = null;
    ArrayList<String> projectlist;
    ArrayList<ProjectList> project;
    ArrayList<String> tasklist;
    ArrayList<TaskList> task;
    ArrayList<String> subtasklist;
    ArrayList<SubTaskList> subtask;
    // Session Manager Class
    SessionManager session;
    String strUserId = "";
    String url = "";
    //
    ArrayList<TimeSheetFillData> taskname;
    TimeSheetFillListViewAdapter arrayadapter = null;
    private Bundle bnd_extras;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesheetfill_main);

        session = new SessionManager(getApplicationContext());

        try {
            HashMap<String, String> user = session.getUserDetails();
            strUserId = user.get(SessionManager.KEY_ID);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("erro" + e);
        }

        taskname = new ArrayList<TimeSheetFillData>();

        if (savedInstanceState == null) {
            bnd_extras = getIntent().getExtras();
            if (bnd_extras == null) {
                str_week = null;
                str_month = null;
                str_year = null;
            } else {
                str_week = bnd_extras.getString("week");
                str_month = bnd_extras.getString("month");
                str_year = bnd_extras.getString("year");
            }
        } else {
            str_week = (String) savedInstanceState.getSerializable("week");
            str_month = (String) savedInstanceState.getSerializable("month");
            str_year = (String) savedInstanceState.getSerializable("year");
        }

        ab = getSupportActionBar();
        ab.setTitle("Timesheet Entry");

        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        txt_weekname = (TextView) findViewById(R.id.txt_weeknm);

        txt_w_m_y = (TextView) findViewById(R.id.txt_w_m_y);
        txt_totalhr = (TextView) findViewById(R.id.txt_totalhr);

        lv_timesheet = (ListView) findViewById(R.id.lv_timesheet);

        sharedpreferences = getSharedPreferences("Project_Sync_Pref",
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        btn_add = (ImageButton) findViewById(R.id.btn_add);

        // txt_weekname.setText(str_week.toString());
        txt_weekname.setText("Total Hours");

        txt_w_m_y.setText(Html.fromHtml("<i><font color=\"#6ebe45\">Week - "
                + str_week + "</font></i>" + " "
                + "<i><font color=\"#f4981e\">" + "Month  -  " + str_month
                + "</font></i>" + " " + "<i><font color=\"#5390cc\">"
                + "Year  -  " + str_year + "</font></i>"));

        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                open_dialog();
            }
        });
        Refreshdata();

    }

    public String getWeekMonthYear() {
        return str_week + "/" + str_month + "/" + str_year + "/" + strUserId;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void open_dialog() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.timesheetinsert_dialog_main);

        actv_project = (Spinner) dialog.findViewById(R.id.spinnerProject);
        actv_task = (Spinner) dialog.findViewById(R.id.spinnerTask);
        actv_subtask = (Spinner) dialog.findViewById(R.id.spinnerSubTask);
        et_comments = (EditText) dialog.findViewById(R.id.et_comments);
        et_no_of_hours = (EditText) dialog.findViewById(R.id.et_no_of_hours);
        btn_dialog_ok = (Button) dialog.findViewById(R.id.btn_ok);
        btn_dialog_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        new LoadProjectData().execute();

        btn_dialog_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Insertdata().execute();
                dialog.dismiss();
            }
        });
        btn_dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void Refreshdata() {
        new LoadSingleTrack().execute();
    }

    public JSONObject getJsonObject(String url) {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    class LoadSingleTrack extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TimesheetfillActivity.this);
            pDialog.setMessage("Loading Timesheet data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting song json and parsing
         */
        protected String doInBackground(String... args) {
            taskname.clear();
            url = getResources().getString(R.string.url_timesheetfill_get)
                    + str_week + "/" + str_month + "/" + str_year + "/"
                    + strUserId;
            System.out.println(url);

            try {

                JSONArray json = null;
                InputStream is = null;
                String strjson = "";
                try {
                    // defaultHttpClient
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                    Log.i("HTTP RESPONSE", is.toString());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    strjson = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error",
                            "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    json = new JSONArray(strjson);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                String timeSheetsID = null;
                String projectName = null;
                String taskName = null;
                String subTaskName = null;
                String comment = null;
                String hours = null;

                for (int i = 0; i < json.length(); i++) {
                    JSONObject jsonObject = json.getJSONObject(i);

                    try {
                        timeSheetsID = jsonObject.getString("TimeSheetsId");
                        projectName = jsonObject.getString("ProjectName");
                        taskName = jsonObject.getString("TaskName");
                        subTaskName = jsonObject.getString("SubTaskName");
                        comment = jsonObject.getString("Comments");
                        hours = jsonObject.getString("NoofHours");
                        dbltotalhours = dbltotalhours
                                + Double.parseDouble(hours);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    taskname.add(new TimeSheetFillData(timeSheetsID,
                            projectName, taskName, subTaskName, comment, hours));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting song information
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    arrayadapter = new TimeSheetFillListViewAdapter(
                            TimesheetfillActivity.this,
                            R.layout.list_row_timesheet, taskname);
                    lv_timesheet.setAdapter(arrayadapter);
                    txt_totalhr.setText(dbltotalhours + "");
                }
            });
        }
    }

    class LoadProjectData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TimesheetfillActivity.this);
            pDialog.setMessage("Loading Project data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting song json and parsing
         */
        protected String doInBackground(String... args) {

            url = getResources().getString(R.string.url_projectlist);
            System.out.println(url);
            // Locate the WorldPopulation Class
            project = new ArrayList<ProjectList>();
            // Create an array to populate the spinner
            projectlist = new ArrayList<String>();

            try {

                projectjson = getJsonObject(url);
                // Set 0th Element
                ProjectList objTemp = new ProjectList("0", "Select Project");
                project.add(objTemp);
                projectlist.add("Select Project");
                project_data = projectjson.getJSONArray("ProjectList");
                for (int i = 0; i < project_data.length(); i++) {
                    JSONObject jsonObject = project_data.getJSONObject(i);
                    ProjectList objProjectList = new ProjectList(
                            jsonObject.optString(KEY_ProjectId),
                            jsonObject.optString(KEY_ProjectName));
                    project.add(objProjectList);
                    projectlist.add(jsonObject.optString(KEY_ProjectName));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting song information
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

					/*actv_project.setAdapter(new ArrayAdapter<String>(
                            TimesheetfillActivity.this,
							android.R.layout.simple_spinner_dropdown_item,
							projectlist));*/
                    actv_project.setAdapter(new MyAdapter(TimesheetfillActivity.this,
                            R.layout.my_spinner_style_small, projectlist));

                }
            });

            actv_project
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {
                            if (position > 0) {
                                strProjectId = project.get(position)
                                        .getProjectId();
                                new LoadTaskData().execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
        }
    }

    class LoadTaskData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TimesheetfillActivity.this);
            pDialog.setMessage("Loading Task data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting song json and parsing
         */
        protected String doInBackground(String... args) {

            url = getResources().getString(R.string.url_tasklist)
                    + strProjectId;
            System.out.println(url);
            // Locate the WorldPopulation Class
            task = new ArrayList<TaskList>();
            // Create an array to populate the spinner
            tasklist = new ArrayList<String>();

            try {

                taskjson = getJsonObject(url);

                // Set 0th Element
                TaskList objTemp = new TaskList("0", "Select Task");
                task.add(objTemp);
                tasklist.add("Select Task");
                task_data = taskjson.getJSONArray("TaskList");
                for (int i = 0; i < task_data.length(); i++) {
                    JSONObject jsonObject = task_data.getJSONObject(i);
                    TaskList objTaskList = new TaskList(
                            jsonObject.optString(KEY_TaskId),
                            jsonObject.optString(KEY_TaskName));
                    task.add(objTaskList);
                    tasklist.add(jsonObject.optString(KEY_TaskName));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting song information
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

					/*actv_task.setAdapter(new ArrayAdapter<String>(
                            TimesheetfillActivity.this,
							android.R.layout.simple_spinner_dropdown_item,
							tasklist));*/
                    actv_task.setAdapter(new MyAdapter(TimesheetfillActivity.this,
                            R.layout.my_spinner_style_small, tasklist));

                }
            });

            actv_task.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    if (position > 0) {
                        strTaskId = task.get(position).getTaskId();
                        new LoadSubTaskData().execute();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    class LoadSubTaskData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TimesheetfillActivity.this);
            pDialog.setMessage("Loading SubTask data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting song json and parsing
         */
        protected String doInBackground(String... args) {

            url = getResources().getString(R.string.url_subtasklist)
                    + strTaskId;
            System.out.println(url);
            // Locate the WorldPopulation Class
            subtask = new ArrayList<SubTaskList>();
            // Create an array to populate the spinner
            subtasklist = new ArrayList<String>();

            try {

                subtaskjson = getJsonObject(url);
                // Set 0th Element
                // Set 0th Element
                SubTaskList objTemp = new SubTaskList("0", "Select Sub Task");
                subtask.add(objTemp);
                subtasklist.add("Select SubTask");
                subtask_data = subtaskjson.getJSONArray("SubTaskList");
                System.out.println(subtask_data.toString());
                for (int i = 0; i < subtask_data.length(); i++) {
                    JSONObject jsonObject = subtask_data.getJSONObject(i);
                    SubTaskList objSubTaskList = new SubTaskList(
                            jsonObject.optString(KEY_SubTaskId),
                            jsonObject.optString(KEY_SubTaskName));
                    subtask.add(objSubTaskList);
                    subtasklist.add(jsonObject.optString(KEY_SubTaskName));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting song information
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

					/*actv_subtask.setAdapter(new ArrayAdapter<String>(
							TimesheetfillActivity.this,
							android.R.layout.simple_spinner_dropdown_item,
							subtasklist));*/
                    actv_subtask.setAdapter(new MyAdapter(TimesheetfillActivity.this,
                            R.layout.my_spinner_style_small, subtasklist));

                }
            });

            actv_subtask
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {
                            strSubTaskId = subtask.get(position).getSubTaskId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            strSubTaskId = "";
                        }
                    });
        }
    }

    class Insertdata extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TimesheetfillActivity.this);
            pDialog.setMessage("Please Wait Uploading Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("TimeSheetsId", "0"));
            params.add(new BasicNameValuePair("Project", strProjectId));
            params.add(new BasicNameValuePair("Task", strTaskId));
            params.add(new BasicNameValuePair("SubTask", strSubTaskId));
            params.add(new BasicNameValuePair("Comments", et_comments.getText()
                    .toString().trim()));
            params.add(new BasicNameValuePair("NoofHours", et_no_of_hours
                    .getText().toString().trim()));

            url = getResources().getString(R.string.url_timesheet_insert)
                    + str_week + "/" + str_month + "/" + str_year + "/" + "1/"
                    + strUserId;
            System.out.println(url);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            // check log cat from response
            Log.d("Create Response", json.toString());

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Refreshdata();
        }

    }

    public class MyAdapter extends ArrayAdapter<String> {
        ArrayList<String> obj;

        public MyAdapter(Context context, int textViewResourceId,
                         ArrayList<String> subtasklist) {
            super(context, textViewResourceId, subtasklist);
            obj = subtasklist;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.my_spinner_style_small,
                    parent, false);
            TextView label = (TextView) row.findViewById(R.id.txDemoText2);
            //label.setText(obj[position]);
            label.setText(obj.get(position).toString().trim());
            return row;
        }

    }

}
