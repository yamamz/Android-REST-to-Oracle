package com.yamamz.hroracle;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.yamamz.hroracle.model.Emp;
import com.yamamz.hroracle.model.Job;
import com.yamamz.hroracle.retrofitAPI.ServiceGenerator;
import com.yamamz.hroracle.retrofitAPI.apiServices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class createEmployee extends Fragment implements DatePickerDialogFragment.DatePickerDialogHandler {

    private ProgressDialog pDialog;
    private String username;
    private String password;
    private EditText inputName, inputId, inputSalary, inputManager, inputDatehire, inputCommission, inputDeptno;
    private  long timestamp;
    private Spinner  spinnerJobs;
     private   String itemJobSelected;



    private ArrayList<Job> jobsList;
private View RootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   RootView = inflater.inflate(R.layout.fragment_create_employee, container, false);



        iniViews();
        return RootView;
    }

    /**
     * initialized views
     */
    void iniViews(){


        inputId=(EditText) RootView.findViewById(R.id.input_id);
        inputName=(EditText) RootView.findViewById(R.id.input_name);
        inputSalary=(EditText) RootView.findViewById(R.id.input_sal);
        inputManager=(EditText) RootView.findViewById(R.id.input_mgr);
        inputDatehire=(EditText) RootView.findViewById(R.id.input_datehire);
        inputCommission=(EditText) RootView.findViewById(R.id.input_comm);
        inputDeptno=(EditText) RootView.findViewById(R.id.input_deptno);
        spinnerJobs = (Spinner) RootView.findViewById(R.id.spinner);

        inputDatehire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setTargetFragment(createEmployee.this);
                dpb.show();

            }
        });

        Button btnSave = (Button) RootView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //call the http meathod Post to data to Oracle
                postEmp();
            }
        });
        getJSONDataOnServer();
    }

    private void initSimpleSpinner() {
        List<String> Jobtitle = new ArrayList<>();
        for (int i = 0; i < jobsList.size(); i++) {
          Jobtitle.add(jobsList.get(i).getJobTitle());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Jobtitle);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobs.setAdapter(dataAdapter);
        spinnerJobs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemJobSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    void postEmp(){
      getCredendialsInprefs();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        /**
             Get the credendials an authenticate
         */
        apiServices service = ServiceGenerator.createService(apiServices.class, username, password);


        try {

    /**
     * Create instance from your object and get the value of each Edit Text
     */
            Emp user = new Emp(Integer.valueOf(inputId.getText().toString()), inputName.getText().toString(),
                    itemJobSelected, Integer.valueOf(inputManager.getText().toString()),
                    String.valueOf(timestamp), Integer.valueOf(inputSalary.getText().toString()),
                    Integer.valueOf(inputCommission.getText().toString()), Integer.valueOf(inputDeptno.getText().toString()));

            /**
             * e call ang imong Interface nga mo map Post Request
             */
            Call<Emp> call = service.createUser(user);
            call.enqueue(new Callback<Emp>() {
                @Override
                public void onResponse(Call<Emp> call, Response<Emp> response) {


                    if (response.code() == 204) {
                        Toast.makeText(getActivity(), "Save Successfully", Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                                pDialog.dismiss();
                                inputSalary.setText("");
                                inputId.setText("");
                                inputDatehire.setText("");
                                inputName.setText("");
                                inputManager.setText("");
                                inputCommission.setText("");
                                inputDeptno.setText("");


                    } else {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                    }

                    if(response.code()==500){
                        Toast.makeText(getActivity(), "Please check your entry Either your Id" +
                                " is already Exist", Toast
                                .LENGTH_LONG)
                                .show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                    /**
                     * kung imo account wala ge authorize ug post Request
                     */
                    if (response.code() == 403) {
                        Toast.makeText(getActivity(), "you are not authorize here", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Emp> call, Throwable t) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    Toast.makeText(getActivity(), "failed to save", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e){
            if (pDialog.isShowing())
                pDialog.dismiss();
            Toast.makeText(getActivity(),e.toString(), Toast.LENGTH_LONG).show();
        }


    }


    /**
     * mukuha sa credentials nga na save sa prefs
     */
    void getCredendialsInprefs(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = sharedPrefs.getString("username", "");
        password = sharedPrefs.getString("password", "");
    }

    /**
     * convert ang input sa unix time gikan sa string
     */
    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        String date=(monthOfYear+1)+"/"+dayOfMonth+"/"+year;

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = null;
        try {
            date1 = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date1.getTime()/1000L;
        String str=Long.toString(output);

        timestamp = Long.parseLong(str) * 1000;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dateHired = new Date(timestamp);
        inputDatehire.setText(String.valueOf(dt1.format(dateHired)));

    }

    void getJSONDataOnServer(){
        getCredendialsInprefs();
        apiServices service = ServiceGenerator.createService(apiServices.class, username, password);
        Call<ArrayList<Job>> call = service.getJobs();
        call.enqueue(new Callback<ArrayList<Job>>() {
            @Override
            public void onResponse(Call<ArrayList<Job>> call, Response<ArrayList<Job>> response) {

                //Store in arrylist to populate in spinner
if(response.code()==200) {
    jobsList = response.body();
    Toast.makeText(getActivity(), String.valueOf(jobsList.size()), Toast.LENGTH_LONG).show();
    initSimpleSpinner();
                        }
            }

            @Override
            public void onFailure(Call<ArrayList<Job>> call, Throwable t) {


            }


        });



    }


}
