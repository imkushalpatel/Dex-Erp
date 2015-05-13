package com.dexter.dex_erp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dex.i_erp.adapter.WeekBaseAdapter;
import com.dex.i_erp.data.WeekData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SuppressLint({"SimpleDateFormat", "SdCardPath"})
public class Home_Screen extends ActionBarActivity {

    public static final String TIMESHEET_NO_OF_HOURS = "NoofHours";
    public static final String TIMESHEET_NO_OF_WEEK = "TimesheetWeek";
    public static final String TIMESHEET_NO_OF_MONTH = "TimesheetMonth";
    public String str_month_no, str_year_no, str_month_name;
    public String[] str_spn_month_data;
    public int[] int_spn_month_no;
    int int_weekDay;
    int int_year;
    int int_no_of_weeks;
    String spn_year_count[] = new String[3];
    String str_dayOfTheWeek;
    Spinner spn_month, spn_year;
    TextView txt_month, txt_year;
    ListView lv_week;
    Calendar calendar;
    ArrayList<WeekData> weekResults;
    ActionBar ab;

    // private ContextWrapper myContext;
    boolean spn_m_change = true, spn_y_change = true;

    /* Get Time Sheet Data Week Total Fields */
    String str_query_counthours_timesheetmonth;
    boolean refresh_flag = true;
    // Api Fields
    JSONArray sortlisted_search = null;
    String url = "";
    // Session Manager Class
    SessionManager session;

    /* End Get Time Sheet Data Week Total Fields */
    String UserId = "";
    private ProgressDialog mProgressDialog, pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        session = new SessionManager(getApplicationContext());

        try {
            HashMap<String, String> user = session.getUserDetails();
            UserId = user.get(SessionManager.KEY_ID);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("erro" + e);
        }

        int_spn_month_no = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        str_spn_month_data = new String[]{"January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};

        txt_month = (TextView) findViewById(R.id.txt_month);
        txt_year = (TextView) findViewById(R.id.txt_year);

        spn_month = (Spinner) findViewById(R.id.spn_month);
        spn_year = (Spinner) findViewById(R.id.spn_year);

        lv_week = (ListView) findViewById(R.id.lv_week);

        ab = getSupportActionBar();
        ab.setTitle("TimeSheet");
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        // ab.setHomeButtonEnabled(true);
        ((I_ERP_Application) getApplicationContext())
                .setRefresh_flag(refresh_flag);
        spn_month.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                str_month_name = (String) parent.getItemAtPosition(position);

                ((I_ERP_Application) getApplicationContext())
                        .setMonthname(str_month_name);

                str_month_no = String.valueOf(int_spn_month_no[position]);

                txt_month.setText(str_month_no);

                if (spn_m_change == true) {

                    spn_m_change = false;

                } else {

                    noofweeks();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spn_year.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str_year_no = spn_year.getSelectedItem().toString();

                ((I_ERP_Application) getApplicationContext())
                        .setYearno(str_year_no);

                txt_year.setText(str_year_no);

                if (spn_y_change == true) {

                    spn_y_change = false;

                } else {

                    noofweeks();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lv_week.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Object o = lv_week.getItemAtPosition(position);
                WeekData fullObject = (WeekData) o;
                Toast.makeText(Home_Screen.this,
                        "You have chosen: " + " " + fullObject.getName(),
                        Toast.LENGTH_SHORT).show();
                Intent i_t = new Intent(getApplicationContext(),
                        TimesheetfillActivity.class);
                i_t.putExtra("week", String.valueOf(position + 1));
                i_t.putExtra("month", str_month_no);
                i_t.putExtra("year", str_year_no);
                startActivity(i_t);

            }
        });

        mProgressDialog = ProgressDialog.show(Home_Screen.this, "",
                "Please wait...", true);
        new Thread(new Runnable() {
            public void run() {
                calendar = Calendar.getInstance();
                int_year = calendar.get(Calendar.YEAR) - 1;
                // refresh_flag = false;
                month_year();
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.studentdata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logout) {
            SessionManager ss = new SessionManager(Home_Screen.this);
            finish();
            ss.logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void month_year() {

        for (int i = 0; i < 3; i++) {
            spn_year_count[i] = String.valueOf(int_year++);

        }
        ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(
                Home_Screen.this, android.R.layout.simple_spinner_item,
                str_spn_month_data);
        adapter_month
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_month.setAdapter(adapter_month);

        ArrayAdapter<String> adapter_year = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spn_year_count);
        adapter_year
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_year.setAdapter(adapter_year);

        Date newdate = new Date();
        String monthname = (String) android.text.format.DateFormat.format(
                "MMMM", newdate);
        str_month_no = (String) android.text.format.DateFormat.format("M",
                newdate);

        String yearname = (String) android.text.format.DateFormat.format(
                "yyyy", newdate);
        str_year_no = (String) android.text.format.DateFormat.format("yyyy",
                newdate);

        ArrayAdapter<String> mymonth = (ArrayAdapter) spn_month.getAdapter();
        ArrayAdapter<String> myyear = (ArrayAdapter) spn_year.getAdapter();

        ((I_ERP_Application) getApplicationContext()).setMonthname(monthname);
        ((I_ERP_Application) getApplicationContext()).setYearno(yearname);

        int spinnermonthPosition = mymonth
                .getPosition(((I_ERP_Application) getApplicationContext())
                        .getMonthname());
        int spinneryearPosition = myyear
                .getPosition(((I_ERP_Application) getApplicationContext())
                        .getYearno());

        spn_month.setSelection(spinnermonthPosition);
        spn_year.setSelection(spinneryearPosition);

        runOnUiThread(new Runnable() {
            public void run() {

                mProgressDialog.dismiss();
                noofweeks();
            }
        });
    }

    public void noofweeks() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

        try {
            str_dayOfTheWeek = sdf.format(new SimpleDateFormat("M/dd/yyyy")
                    .parse(str_month_no + "/1/" + str_year_no));

        } catch (ParseException e1) {

            e1.printStackTrace();
        }

        if (str_dayOfTheWeek.equals("Monday")
                || str_dayOfTheWeek.contains("Monday")) {
            int_weekDay = 2;
        } else if (str_dayOfTheWeek.equals("Tuesday")
                || str_dayOfTheWeek.contains("Tuesday")) {
            int_weekDay = 3;
        } else if (str_dayOfTheWeek.equals("Wednesday")
                || str_dayOfTheWeek.contains("Wednesday")) {
            int_weekDay = 4;
        } else if (str_dayOfTheWeek.equals("Thursday")
                || str_dayOfTheWeek.contains("Thursday")) {
            int_weekDay = 5;
        } else if (str_dayOfTheWeek.equals("Friday")
                || str_dayOfTheWeek.contains("Friday")) {
            int_weekDay = 6;
        } else if (str_dayOfTheWeek.equals("Saturday")
                || str_dayOfTheWeek.contains("Saturday")) {
            int_weekDay = 7;
        } else if (str_dayOfTheWeek.equals("Sunday")
                || str_dayOfTheWeek.contains("Sunday")) {
            int_weekDay = 1;
        }

        int_no_of_weeks = getWeeksOfMonth(Integer.parseInt(str_year_no),
                Integer.parseInt(str_month_no), int_weekDay);
        new LoadWeeksData().execute();
    }

    public int getWeeksOfMonth(int year, int month, int weekStart) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month - 1);

        int numOfWeeksInMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

        // Log.d("LOG", "max week number" + numOfWeeksInMonth);

        return numOfWeeksInMonth;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (((I_ERP_Application) getApplicationContext()).refresh_flag) {

            ((I_ERP_Application) getApplicationContext())
                    .setRefresh_flag(false);

        } else {
            new LoadWeeksData().execute();

        }
    }

    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are You Sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    class LoadWeeksData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home_Screen.this);
            pDialog.setMessage("Please wait weeks data loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting song json and parsing
         */
        protected String doInBackground(String... args) {
            WeekData sr1;

            weekResults = new ArrayList<WeekData>();

            int total_hours;

            str_query_counthours_timesheetmonth = ((I_ERP_Application) getApplicationContext())
                    .getMonthname()
                    + "-"
                    + ((I_ERP_Application) getApplicationContext()).getYearno();

            weekResults.clear();

            url = getResources().getString(
                    R.string.Get_Time_Sheet_Data_Week_Total)
                    + str_month_no + "/" + str_year_no + "/" + UserId;
            System.out.println(url);
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
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                json = new JSONArray(strjson);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            for (int i = 1; i <= int_no_of_weeks; i++) {
                String strweekno = "";
                String strweekhour = "";
                Boolean chkFlag = true;
                for (int j = 0; j < json.length(); j++) {
                    try {
                        JSONObject jsonObject = json.getJSONObject(j);
                        strweekno = jsonObject.getString("TimesheetWeek");
                        strweekhour = jsonObject.getString("NoofHours");
                        if (Integer.parseInt(strweekno) == i) {
                            sr1 = new WeekData("Week  -  " + i, ""
                                    + strweekhour);
                            weekResults.add(sr1);
                            chkFlag = false;
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (chkFlag) {

                    sr1 = new WeekData("Week  -  " + i, "0");
                    weekResults.add(sr1);
                    chkFlag = true;

                }
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

                    lv_week.setAdapter(new WeekBaseAdapter(
                            getApplicationContext(), weekResults));

                }
            });

        }
    }

}
