package com.as400.workjob;


import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList;

import java.util.TreeSet;

/**
 * A Camel Application
 */
public class WrkActJob001 {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
      public static void main(String... args) throws Exception {

      JobList joblist = new JobList(new AS400(args[0],args[1],args[2]));

          joblist.clearJobSelectionCriteria();
          joblist.clearJobAttributesToRetrieve();
          joblist.clearJobAttributesToSortOn();

          joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
          joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
          joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);


//        String jobName  = args[3];
//        String userName = args[4];
//        String jobNumber= args[5];

        // select a specific job :




        /*
        joblist.addJobAttributeToRetrieve(Job.SUBSYSTEM);
        joblist.addJobAttributeToRetrieve(Job.JOB_TYPE_ENHANCED);
        joblist.addJobAttributeToRetrieve(Job.FUNCTION_TYPE);
        joblist.addJobAttributeToRetrieve(Job.FUNCTION_NAME);
        joblist.addJobAttributeToRetrieve(Job.ACTIVE_JOB_STATUS);
        joblist.addJobAttributeToRetrieve(Job.CPU_TIME_USED);
        */

        //  joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME,JobList.SELECTION_JOB_NAME_ALL);
        //  joblist.addJobSelectionCriteria(JobList.SELECTION_USER_NAME,JobList.SELECTION_USER_NAME_ALL);
        //  joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_NUMBER,JobList.SELECTION_JOB_NUMBER_ALL);

        // Select jobs submitted by given user

        // joblist.addJobSelectionCriteria(JobList.SELECTION_USER_NAME,userName);

        // Select jobs on a given JOBQ

        // joblist.clearJobSelectionCriteria();
        // joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE,Boolean.TRUE);
        // joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_QUEUE,"/QSYS.LIB/QCTL.JOBQ");

//        joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE,Boolean.TRUE);
//        joblist.addJobSelectionCriteria(JobList.SELECTION_ACTIVE_JOB_STATUS,"EVTW");


        joblist.load();

        System.out.println("Total number of jobs selected: "+joblist.getLength());

        TreeSet<Job> sortedJobList = new TreeSet<Job>(new SortOnCPUTime());

//        TreeSet<Job> sortedJobList = new TreeSet<Job>(new SortOnJobNumber());
//        joblist.getJobs(-1,10)
//        Enumeration<Job> list = joblist.getJobs();

        Job[] jobs = joblist.getJobs(-1,0);


        for (Job j:jobs) {
            sortedJobList.add(j);
        }

        /*
        while(list.hasMoreElements()){
            job=(Job)list.nextElement();
            sortedJobList.add(job);
        }
        */

        for (Job i:sortedJobList) {
            System.out.println(i+" - "+i.getCPUUsed());
        }

/*
        for (Job job :joblist.getJobs(-1,10)) {
            System.out.println("Job:    "+job.toString());
            System.out.println("Subsystem:  "+job.getSubsystem());
            System.out.println("Job Queue:  "+job.getQueue());
            System.out.println("CPU Used:   "+job.getCPUUsed());
            System.out.println("Job Status: "+job.getStatus());
            //System.out.println("Job Status in JOBQ: "+job.getJobStatusInJobQueue());
        }
*/
    }

    // default settings :
    //private static void clearSetJobSelectionAttributes(JobList joblist) {
    //}


}

