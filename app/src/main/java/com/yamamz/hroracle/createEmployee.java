package com.yamamz.hroracle;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.yamamz.hroracle.model.Emp;
import com.yamamz.hroracle.retrofitAPI.ServiceGenerator;
import com.yamamz.hroracle.retrofitAPI.apiServices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class createEmployee extends Fragment implements DatePickerDialogFragment.DatePickerDialogHandler {

    private ProgressDialog pDialog;
    private String username;
    private String password;

    private TextInputLayout layoutName,layoutId,layoutJob,layoutSalary,layoutManager,layoutDatehire,layoutCommission,layoutDeptno;
    private EditText inputName, inputId, inputJob, inputSalary, inputManager, inputDatehire, inputCommission, inputDeptno;
    private  long timestamp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_create_employee, container, false);


        layoutId=(TextInputLayout) rv.findViewById(R.id.input_layout_id);
        layoutName=(TextInputLayout) rv.findViewById(R.id.input_layout_name);
        layoutJob=(TextInputLayout) rv.findViewById(R.id.input_layout_job);
        layoutSalary=(TextInputLayout) rv.findViewById(R.id.input_layout_sal);
        layoutManager=(TextInputLayout) rv.findViewById(R.id.input_layout_mgr);
        layoutDatehire=(TextInputLayout) rv.findViewById(R.id.input_layout_datehire);
        layoutCommission=(TextInputLayout) rv.findViewById(R.id.input_layout_comm);
        layoutDeptno=(TextInputLayout) rv.findViewById(R.id.input_layout_deptno);

        inputId=(EditText) rv.findViewById(R.id.input_id);
        inputName=(EditText) rv.findViewById(R.id.input_name);
        inputJob=(EditText) rv.findViewById(R.id.input_job);
        inputSalary=(EditText) rv.findViewById(R.id.input_sal);
        inputManager=(EditText) rv.findViewById(R.id.input_mgr);
        inputDatehire=(EditText) rv.findViewById(R.id.input_datehire);
        inputCommission=(EditText) rv.findViewById(R.id.input_comm);
        inputDeptno=(EditText) rv.findViewById(R.id.input_deptno);


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

        FloatingActionButton fab = (FloatingActionButton) rv.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postEmp();


            }
        });

        return rv;
    }

    void postEmp(){
      getCredendialsInprefs();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();


        apiServices service = ServiceGenerator.createService(apiServices.class, username, password);


        try {

            Emp user = new Emp(Integer.valueOf(inputId.getText().toString()), inputName.getText().toString(),
                    inputJob.getText().toString(), Integer.valueOf(inputManager.getText().toString()),
                    String.valueOf(timestamp), Integer.valueOf(inputSalary.getText().toString()),
                    Integer.valueOf(inputCommission.getText().toString()), Integer.valueOf(inputDeptno.getText().toString()));


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
                        inputJob.setText("");
                        inputName.setText("");
                        inputManager.setText("");
                        inputCommission.setText("");
                        inputDeptno.setText("");

                    } else {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                    }

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





    void getCredendialsInprefs(){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = sharedPrefs.getString("username", "");
        password = sharedPrefs.getString("password", "");
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {

        String date=(monthOfYear+1)+"/"+dayOfMonth+"/"+year;

        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = null;
        try {
            date1 = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date1.getTime()/1000L;
        String str=Long.toString(output);
        timestamp = Long.parseLong(str) * 1000;

        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dateHired = new Date(timestamp);
        inputDatehire.setText(String.valueOf(dt1.format(dateHired)));


    }
}
