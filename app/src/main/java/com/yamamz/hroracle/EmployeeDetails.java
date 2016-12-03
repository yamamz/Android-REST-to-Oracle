package com.yamamz.hroracle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yamamz.hroracle.model.Emp;
import com.yamamz.hroracle.retrofitAPI.ServiceGenerator;
import com.yamamz.hroracle.retrofitAPI.apiServices;
import com.yamamz.hroracle.view.RamdomImages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetails extends AppCompatActivity {
     private EditText inputName, inputId, inputJob, inputSalary, inputManager, inputDatehire, inputCommission, inputDeptno;
    private String UnixTime;
    private String username;
    private String password;
    private Emp employee;

    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Employee Info");

        /**
         * initialize realm for local database
         */

        Realm.init(this);
        realm = Realm.getDefaultInstance();


        initLayout();
        loadBackdrop();

    }
    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(RamdomImages.getRandomDrawable()).centerCrop().into(imageView);
    }


    void initLayout(){
        /**
         * kuhaon ang value sa intent nga gipasa gikan sa main activity
         */
        Intent startingIntent = getIntent();
        final String id=startingIntent.getStringExtra("id");

        //initialize views

        inputId=(EditText) findViewById(R.id.input_id);
        inputName=(EditText) findViewById(R.id.input_name);
        inputJob=(EditText) findViewById(R.id.input_job);
        inputSalary=(EditText) findViewById(R.id.input_sal);
        inputManager=(EditText) findViewById(R.id.input_mgr);
        inputDatehire=(EditText) findViewById(R.id.input_datehire);
        inputCommission=(EditText) findViewById(R.id.input_comm);
        inputDeptno=(EditText) findViewById(R.id.input_deptno);

        inputId.setFocusable(false);

        final Button btnUpdate=(Button) findViewById(R.id.btnUpdate);
        Button btnDelete=(Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCredendialsInprefs();
                DeleteEmployee(Integer.valueOf(id));
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnUpdate.getText().toString().equals("Edit")){
                    inputId.setEnabled(true);
                    inputName.setEnabled(true);
                    inputJob.setEnabled(true);
                    inputSalary.setEnabled(true);
                    inputManager.setEnabled(true);
                    inputDatehire.setEnabled(true);
                    inputCommission.setEnabled(true);
                    inputDeptno.setEnabled(true);

                    btnUpdate.setText("Update");

                }

               else {

                    inputId.setEnabled(false);
                    inputName.setEnabled(false);
                    inputJob.setEnabled(false);
                    inputSalary.setEnabled(false);
                    inputManager.setEnabled(false);
                    inputDatehire.setEnabled(false);
                    inputCommission.setEnabled(false);
                    inputDeptno.setEnabled(false);



                    btnUpdate.setText("Edit");
                   getCredendialsInprefs();

                    //The meathod to Edit employee
                    EditEmployee();

                }

            }
        });


        /**
         * query sa Realm nga mo find employee on ID
         */
        final RealmResults<Emp> emp = realm.where(Emp.class).findAll();
        employee = emp.where().equalTo("empno", Integer.valueOf(id)).findFirst();


        inputId.setText(id);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");

        try {
            long timestamp = Long.valueOf(employee.getHiredate());
            Date dateHired = new Date(timestamp);

            inputName.setText(employee.getEname());
            inputSalary.setText(String.valueOf(employee.getSal()));
            inputJob.setText(employee.getJob());
            inputDatehire.setText(dt1.format(dateHired));
            inputManager.setText(String.valueOf(employee.getMgr()));
            inputCommission.setText(String.valueOf(employee.getComm()));
            inputDeptno.setText(String.valueOf(employee.getDeptno()));


        }

        catch (Exception e){
            Toast.makeText(EmployeeDetails.this,String.valueOf(e.toString()),Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    void EditEmployee(){
        //get Authenticated
        apiServices service = ServiceGenerator.createService(apiServices.class, username, password);
        //convert the datehire into unixtime
        converttoUnixtime();

        Emp user = new Emp(Integer.valueOf(inputId.getText().toString()),inputName.getText().toString(),
                inputJob.getText().toString(),Integer.valueOf(inputManager.getText().toString()),
                UnixTime,Integer.valueOf(inputSalary.getText().toString()),
                Integer.valueOf(inputCommission.getText().toString()), Integer.valueOf(inputDeptno.getText().toString()));


        Call<Emp> call=service.editEmployee(Integer.valueOf(inputDeptno.getText().toString()),user);

        call.enqueue(new Callback<Emp>() {
            @Override
            public void onResponse(Call<Emp> call, Response<Emp> response) {

                if(response.code()==204) {
                    Toast.makeText(EmployeeDetails.this, "update Successfull", Toast.LENGTH_LONG).show();
                }

                if(response.code()==403) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
                    long timestamp = Long.valueOf(employee.getHiredate()); //Example -> in ms
                    Date dateHired = new Date(timestamp);
                    Toast.makeText(EmployeeDetails.this, "You are not authorized", Toast.LENGTH_LONG).show();
                    inputName.setText(employee.getEname());
                    inputSalary.setText(String.valueOf(employee.getSal()));
                    inputJob.setText(employee.getJob());
                    inputDatehire.setText(dt1.format(dateHired));
                    inputManager.setText(String.valueOf(employee.getMgr()));
                    inputCommission.setText(String.valueOf(employee.getComm()));
                    inputDeptno.setText(String.valueOf(employee.getDeptno()));

                }


            }

            @Override
            public void onFailure(Call<Emp> call, Throwable t) {

            }
        });
    }

    void converttoUnixtime(){
        String date=inputDatehire.getText().toString();
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = null;
        try {
            date1 = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date1.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        UnixTime=String.valueOf(timestamp);
    }

    void DeleteEmployee(final int PositionID) {

        apiServices service = ServiceGenerator.createService(apiServices.class, username, password);
        Call<ResponseBody> deleteRequest = service.deleteEmployee(PositionID);
        deleteRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 204) {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            RealmResults<Emp> results =bgRealm.where(Emp.class).equalTo("empno", PositionID).findAll();
                            results.deleteAllFromRealm();
                        }
                    }, new Realm.Transaction.OnSuccess() {

                        @Override
                        public void onSuccess() {
                            DeleteEmployee(PositionID);

                            inputSalary.setText("");
                            inputId.setText("");
                            inputDatehire.setText("");
                            inputJob.setText("");
                            inputName.setText("");
                            inputManager.setText("");
                            inputCommission.setText("");
                            inputDeptno.setText("");
                            Toast.makeText(EmployeeDetails.this, "Delete successfully on id " + PositionID, Toast.LENGTH_LONG).show();

                        }
                    });



                }

                if(response.code()==403) {
                    Toast.makeText(EmployeeDetails.this, "You are not authorized", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    void getCredendialsInprefs(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPrefs.getString("username", "");
        password = sharedPrefs.getString("password", "");
    }



}
