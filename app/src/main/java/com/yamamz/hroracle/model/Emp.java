package com.yamamz.hroracle.model;

/**
 * Created by AMRI on 11/30/2016.
 */


import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Generated("org.jsonschema2pojo")
public class Emp     extends RealmObject{

    @SerializedName("empno")
    @PrimaryKey
    private Integer empno;
    @SerializedName("ename")
    private String ename;
    @SerializedName("job")
    private String job;
    @SerializedName("mgr")
    private Integer mgr;
    @SerializedName("hiredate")
    private String hiredate;
    @SerializedName("sal")
    private Integer sal;
    @SerializedName("comm")
    private Integer comm;
    @SerializedName("deptno")

    private Integer deptno;

    public Emp(){

    }

    public Emp(Integer empno, String ename, String job, Integer mgr, String hiredate, Integer sal, Integer comm, Integer deptno) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.comm = comm;
        this.deptno = deptno;
    }

    /**
     *
     * @return
     * The empno
     */



    public Integer getEmpno() {
        return empno;
    }

    /**
     *
     * @param empno
     * The empno
     */
    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    /**
     *
     * @return
     * The ename
     */
    public String getEname() {
        return ename;
    }

    /**
     *
     * @param ename
     * The ename
     */
    public void setEname(String ename) {
        this.ename = ename;
    }

    /**
     *
     * @return
     * The job
     */
    public String getJob() {
        return job;
    }

    /**
     *
     * @param job
     * The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     *
     * @return
     * The mgr
     */
    public Integer getMgr() {
        return mgr;
    }

    /**
     *
     * @param mgr
     * The mgr
     */
    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    /**
     *
     * @return
     * The hiredate
     */
    public String getHiredate() {
        return hiredate;
    }

    /**
     *
     * @param hiredate
     * The hiredate
     */
    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    /**
     *
     * @return
     * The sal
     */
    public Integer getSal() {
        return sal;
    }

    /**
     *
     * @param sal
     * The sal
     */
    public void setSal(Integer sal) {
        this.sal = sal;
    }

    /**
     *
     * @return
     * The comm
     */
    public Integer getComm() {
        return comm;
    }

    /**
     *
     * @param comm
     * The comm
     */
    public void setComm(Integer comm) {
        this.comm = comm;
    }

    /**
     *
     * @return
     * The deptno
     */
    public Integer getDeptno() {
        return deptno;
    }

    /**
     *
     * @param deptno
     * The deptno
     */
    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

}

