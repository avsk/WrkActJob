package com.as400.workjob;

import com.ibm.as400.access.*;

import java.beans.PropertyVetoException;
import java.io.IOException;

/**
 * Created by root on 7/23/16.
 */
public class WrkActJobOneJob {
    public static void main(String... args) throws Exception {

        JobList joblist = new JobList(new AS400(args[0], args[1], args[2]));


        String jobName = args[3];
        String userName = args[4];
        String jobNumber = args[5];

        //joblist.load();

        clearSetSelection(joblist,jobName,userName,jobNumber);
        System.out.print(displayBasic(joblist,','));

    }


    private static void clearSetSelection(JobList joblist,String jobName,String userName,String jobNumber) throws PropertyVetoException {

        joblist.clearJobSelectionCriteria();
        joblist.clearJobAttributesToRetrieve();
        joblist.clearJobAttributesToSortOn();

        joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
        joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
        joblist.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

        joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_NAME,jobName);
        joblist.addJobSelectionCriteria(JobList.SELECTION_USER_NAME,userName);
        joblist.addJobSelectionCriteria(JobList.SELECTION_JOB_NUMBER,jobNumber);

    }

    private static String displayBasic(JobList joblist,Character separator) throws InterruptedException, ErrorCompletingRequestException, AS400SecurityException, ObjectDoesNotExistException, IOException {
        Job[] jobs = joblist.getJobs(-1,0);

        joblist.load();

        StringBuffer out = new StringBuffer();

        out.append("Total active jobs : "+joblist.getLength());

        out.append("\n");
        for (Job i:jobs) {
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
            //
            out.append("\n");
        }
        return out.toString();
    }
}
