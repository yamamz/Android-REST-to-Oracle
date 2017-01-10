package com.yamamz.hroracle.retrofitAPI;

import com.yamamz.hroracle.model.Emp;
import com.yamamz.hroracle.model.Job;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Raymundo T. Melecio on 11/30/2016.
 */


public interface apiServices {
    //Save employee to oracle database
    @POST("secureUser/employees")
    Call<Emp> createUser(@Body Emp emp);
    //Edit employee in oracle database
    @PUT("secureUser/employees/{id}")
    Call<Emp> editEmployee(@Path("id") int EmpID, @Body Emp emp);
    //Get all Employees in oracle database
    @GET("secureUser/employees/")
    Call<ArrayList<Emp>> getEmployees();
    //Delete employees in oracle
    @DELETE("secureUser/employees/{id}")
    Call<ResponseBody>
    deleteEmployee(@Path("id") int empID);


    @GET("secureUser/employees/")
    Call<ArrayList<Emp>> getAutheticated();

    @GET("secureUser/jobs/")
    Call<ArrayList<Job>> getJobs();



}
