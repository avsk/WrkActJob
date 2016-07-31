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
public class WrkActJob {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
      public static void main(String... args) throws AS400CommunicatorException{

          WrkActJob_ actJob = new WrkActJob_(args[0], args[1], args[2]);

          try{
              actJob.initJobList();
          }
          catch (Exception e){
              throw new AS400CommunicatorException("Exception initialising job list");
          }

          try {
              actJob.loadJobList();
          }
          catch(Exception e){
              throw new AS400CommunicatorException("Error loading job info");
          }

          try {
              System.out.print(actJob.getJobList("hash"));
          }
          catch(Exception e){
              throw new AS400CommunicatorException("Error getting job list");
          }

      }
}

class WrkActJob_ {

    private JobList joblist;

    private Job job;
    private Job[] jobs;

    TreeSet<Job> sortedJobList;


    WrkActJob_(String sysname, String username, String password) {
        this.joblist = new JobList(new AS400(sysname, username, password));
    }

    public void initJobList() throws PropertyVetoException {

        joblist.clearJobSelectionCriteria();
        joblist.clearJobAttributesToRetrieve();
        joblist.clearJobAttributesToSortOn();

        joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
        joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
        joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

    }

    public void loadJobList() throws InterruptedException, ErrorCompletingRequestException, AS400SecurityException, ObjectDoesNotExistException, IOException {
        joblist.load();
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
            jobs = joblist.getJobs(-1, 0);
        } catch(AS400Exception e){
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
                    // Job Description
                    out.append(i.getJobDescription());
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
            case "hash": {
                char separator = ',';

                for (Job i : jobs) {
                    // Job Name :
                    out.append("Job Name : " + i);
                    out.append(separator);
                    // Job Description
                    out.append("Job Description : " + i.getJobDescription());
                    out.append(separator);
                    // Date job placed on system
                    out.append("Date job placed on system : " + i.getDate());
                    out.append(separator);
                    // Date job entered system
                    out.append("Date job entered system : " + i.getJobActiveDate());
                    out.append(separator);
                    // Queue name
                    out.append("Job Queue : " + i.getQueue());
                    out.append(separator);
                    // Queue priority
                    out.append("Queue Priority : " + i.getQueuePriority());
                    out.append(separator);
                    // Run priority
                    out.append("Run Priority : " + i.getRunPriority());
                    out.append(separator);
                    // Job Status
                    out.append("Job Status : " + i.getStatus());
                    out.append(separator);
                    // Completion Status
                    out.append("Completion Status : " + i.getCompletionStatus());
                    out.append(separator);
                    // CPU used
                    out.append("CPU Used : " + i.getCPUUsed());
                    // out.append(separator);
                    //
                    out.append("\n");
                }
            }
        }

        return out.toString();
    }
}

