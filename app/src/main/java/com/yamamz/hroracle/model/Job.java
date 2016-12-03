package com.yamamz.hroracle.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by AMRI on 12/3/2016.
 */

@Generated("org.jsonschema2pojo")
public class Job {

    @SerializedName("jobId")
    private String jobId;
    @SerializedName("jobTitle")
    private String jobTitle;
    @SerializedName("minSalary")
    private Integer minSalary;
    @SerializedName("maxSalary")
    private Integer maxSalary;

    /**
     *
     * @return
     * The jobId
     */
    public String getJobId() {
        return jobId;
    }

    /**
     *
     * @param jobId
     * The jobId
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     *
     * @return
     * The jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     *
     * @param jobTitle
     * The jobTitle
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     *
     * @return
     * The minSalary
     */
    public Integer getMinSalary() {
        return minSalary;
    }

    /**
     *
     * @param minSalary
     * The minSalary
     */
    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    /**
     *
     * @return
     * The maxSalary
     */
    public Integer getMaxSalary() {
        return maxSalary;
    }

    /**
     *
     * @param maxSalary
     * The maxSalary
     */
    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }

}

