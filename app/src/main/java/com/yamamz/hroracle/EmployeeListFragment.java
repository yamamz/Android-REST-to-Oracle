package com.yamamz.hroracle;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yamamz.hroracle.model.Emp;
import com.yamamz.hroracle.model.Employee;
import com.yamamz.hroracle.retrofitAPI.ServiceGenerator;
import com.yamamz.hroracle.retrofitAPI.apiServices;
import com.yamamz.hroracle.view.DeviderItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmployeeListFragment extends Fragment  {




    private ArrayList<Emp> dataEmp;
    private String username;
    private String password;
    private List<Employee> employeesList = new ArrayList<>();
    private List<Employee> employeeFilterList= new ArrayList<>();
   // private RecyclerView recyclerView;
    private EmployeesAdapter mAdapter;


    ArrayList<HashMap<String, String>> employeeList;
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    private Realm realm;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_employee_list, container, false);

        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();

      //  searchEmployee();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCredendialsInprefs();
                searchEmployee();
            }
        });

        setupRecyclerView();
        loadEmployeeOnDatabase();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void setupRecyclerView() {

        mAdapter = new EmployeesAdapter(recyclerView.getContext(), employeesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DeviderItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.MyViewHolder> {

        private Context context;
        private List<Employee> employeeList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView Name, empID, position,Initial;



            final View mView;

            MyViewHolder(View view) {
                super(view);
                mView = view;
                Name = (TextView) view.findViewById(R.id.Name);
                position = (TextView) view.findViewById(R.id.position);
                empID = (TextView) view.findViewById(R.id.EmpID);
                Initial = (TextView) view.findViewById(R.id.Initial);
            }
        }

        public List<Employee> getEmployeeList() {
            return employeeList;
        }

        public void setEmployeeList(List<Employee> employeeList) {
            this.employeeList = employeeList;
        }

        EmployeesAdapter(Context context, List<Employee> employeesList) {

            this.employeeList = employeesList;
            this.context=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.employee_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            TypedArray circles = context.getResources().obtainTypedArray(R.array.circle_images);

            int choice = (int) (Math.random() * circles.length());
            final Employee employee = employeeList.get(position);
            holder.Name.setText(employee.getName());
            holder.position.setText(employee.getPosition());
            holder.empID.setText(employee.getEmpID());
            String name= holder.Name.getText().toString();
            holder.Initial.setText(name.substring(1,2));
            holder.Initial.setBackgroundResource((circles.getResourceId(choice, R.drawable.circle)));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                    Intent intent = new Intent(getActivity(), EmployeeDetails.class);
                    intent.putExtra("id", employee.getEmpID());
                    intent.putExtra("name",employee.getName());
                    startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return employeeList.size();
        }

        public void setFilter(List<Employee> employees) {
            employeeList = new ArrayList<>();
            employeeList.addAll(employees);
            notifyDataSetChanged();
        }
    }


    void   searchEmployee(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.deleteAll();

            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                employeesList.clear();
                employeeList.clear();
                mAdapter.notifyDataSetChanged();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
                 getJSONDataOnServer();



            }
        });
    }


    void getJSONDataOnServer(){

        apiServices service = ServiceGenerator.createService(apiServices.class, username, password);
        Call<ArrayList<Emp>> call = service.getEmployees();
        call.enqueue(new Callback<ArrayList<Emp>>() {
            @Override
            public void onResponse(Call<ArrayList<Emp>> call, Response<ArrayList<Emp>> response) {
                if(pDialog != null && pDialog.isShowing())
                {
                    pDialog.dismiss();
                }

                try {
                    dataEmp = response.body();

                    SaveTolocal();

                    Toast.makeText(getActivity(), String.valueOf(dataEmp.size()), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < dataEmp.size(); i++) {
                        Employee empl = new Employee("-"+dataEmp.get(i).getEname(), dataEmp.get(i).getJob(), String.valueOf(dataEmp.get(i).getEmpno()));
                        employeesList.add(empl);
                        mAdapter.notifyDataSetChanged();
                    }

                }
                catch (Exception e){


                }



            }

            @Override
            public void onFailure(Call<ArrayList<Emp>> call, Throwable t) {

            }


        });



    }


    void SaveTolocal(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

           bgRealm.copyToRealm(dataEmp);

            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {


            }
        });
    }


    void loadEmployeeOnDatabase(){
        for (Emp emp : realm.where(Emp.class).findAllSorted("ename", Sort.ASCENDING)) {
            Employee empl = new Employee("-"+emp.getEname(),emp.getJob(),String.valueOf(emp.getEmpno()));
            employeesList.add(empl);
        }
        mAdapter.notifyDataSetChanged();
    }

    public EmployeeListFragment() {
        super();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        employeesList.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public void onStart() {
        super.onStart();
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
    }
    @Override
    public void onResume() {
        super.onResume();
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
    }
    void getCredendialsInprefs(){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = sharedPrefs.getString("username", "");
        password = sharedPrefs.getString("password", "");
    }



}
