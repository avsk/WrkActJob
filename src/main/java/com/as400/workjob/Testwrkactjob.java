package com.as400.workjob;


import com.ibm.as400.access.*;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;

/**
 * A Camel Application
 */
public class Testwrkactjob {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception{

        TestWrkactjob_ actJob = new TestWrkactjob_(args[0], args[1], args[2]);

        actJob.initJobList();
        actJob.loadJobList();
        actJob.getJobList("csv");

    }
}

class TestWrkactjob_ {

    private JobList joblist;
    private Job job;
    private Job[] jobs;

    TreeSet<Job> sortedJobList;


    TestWrkactjob_(String sysname, String username, String password) {
        this.joblist = new JobList(new AS400(sysname, username, password));
    }

    public void initJobList()  {

        try {
            joblist.clearJobSelectionCriteria();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        joblist.clearJobAttributesToRetrieve();
        joblist.clearJobAttributesToSortOn();

        try {
            joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
            joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
            joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

    }

    public void loadJobList() {
        try {
            joblist.load();
        } catch (AS400SecurityException e) {
            e.printStackTrace();
        } catch (ErrorCompletingRequestException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ObjectDoesNotExistException e) {
            e.printStackTrace();
        }
        System.out.print("Total active jobs : " + joblist.getLength() + "\n");
    }

    public void setSortOnCPUTime() {
//      this.sortedJobList = new TreeSet<Job>(new SortOnCPUTime());
        this.sortedJobList = new TreeSet<Job>(new Comparator<Job>() {
            @Override
            public int compare(Job a, Job b) {
                try {
                    int aCPUUsed = a.getCPUUsed();
                    int bCPUUsed = b.getCPUUsed();

                    if (aCPUUsed == bCPUUsed) return 0;
                    else return bCPUUsed - aCPUUsed;

                } catch (AS400Exception e0) {
                    e0.printStackTrace();
                } catch (AS400SecurityException e1) {
                    e1.printStackTrace();
                } catch (ErrorCompletingRequestException e2) {
                    e2.printStackTrace();
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                } catch (ObjectDoesNotExistException e5) {
                    e5.printStackTrace();
                }
                return 0;
            }
        });
    }

    public String getJobList(String format) throws InterruptedException, ErrorCompletingRequestException, AS400SecurityException, ObjectDoesNotExistException, IOException {

        StringBuffer out = new StringBuffer();
        try {
            jobs = joblist.getJobs(-1,0);
        } catch (AS400Exception e){
            e.printStackTrace();
        } catch (AS400SecurityException e) {
            e.printStackTrace();
        } catch (ErrorCompletingRequestException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ObjectDoesNotExistException e) {
            e.printStackTrace();
        }

        switch (format) {
            case "csv": {

                char separator = ',';

                    for (Job i : jobs) {
                        // Job Name :
                        out.append(i);
                        out.append(separator);
                        // Date job placed on system
                        out.append(i.getDate());
                        out.append(separator);
                        // Date job entered system
                        out.append(i.getJobActiveDate());
                        out.append(separator);
                        // Queue name
                        out.append(i.getQueue());
                        out.append(separator);
                        // Queue priority
                        out.append(i.getQueuePriority());
                        out.append(separator);
                        // Run priority
                        out.append(i.getRunPriority());
                        out.append(separator);
                        // Job Status
                        out.append(i.getStatus());
                        out.append(separator);
                        // Completion Status
                        out.append(i.getCompletionStatus());
                        out.append(separator);
                        // CPU used
                        out.append(i.getCPUUsed());
                        // out.append(separator);
                        out.append("\n");
                    }
            }


            case "json": {

                char separator = ',';
                // pre processing json
                out.append("{");
                out.append(applyQuotes("jobs"));
                out.append(applyQuotes(":"));
                out.append(applyQuotes("{"));
                out.append(applyQuotes("job"));
                out.append(":");
                out.append("[");


                    for (Job i : jobs) {

                        out.append("{");
                        // Job Name :
                        out.append(applyQuotes("Job Name")).append(":").append(applyQuotes(i.toString()));
                        out.append(separator);
                        // Date job placed on system
                        out.append(applyQuotes("Date Job placed on system")).append(":").append(applyQuotes(i.getDate().toString()));
                        out.append(separator);
                        // Date job entered system
                        out.append(applyQuotes("Date Job entered the system")).append(":").append(applyQuotes(i.getJobActiveDate().toString()));
                        out.append(separator);
                        // Queue name
                        out.append(applyQuotes("Job Queue")).append(":").append(applyQuotes(i.getQueue().toString()));
                        out.append(separator);
                        // Queue priority
                        out.append(applyQuotes("Job Queue Priority")).append(":").append(i.getQueuePriority());
                        out.append(separator);
                        // Run priority
                        out.append(applyQuotes("Run Priority")).append(":").append(i.getRunPriority());
                        out.append(separator);
                        // Job Status
                        out.append(applyQuotes("Job Status")).append(":").append(i.getStatus());
                        out.append(separator);
                        // Completion Status
                        out.append(applyQuotes("Completion Status")).append(":").append(i.getCompletionStatus());
                        out.append(separator);
                        // CPU used
                        out.append(applyQuotes("CPU Used")).append(":").append(i.getCPUUsed());
                        out.append("CPU Used : " + i.getCPUUsed());
                        // out.append(separator);
                        //
                        out.append("}");
                        out.append(",");
                        out.append("\n");
                    }
                //post proccessing json
                out.append("]");
                out.append("}");
                out.append("}");
            }
        }
        return out.toString();
    }

    public static String applyQuotes(String text){
        StringBuffer textBuffer = new StringBuffer();
        return textBuffer.append('"').append(text).append('"').toString();
    }
}
