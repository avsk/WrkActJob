package com.as400.workjob;

import com.ibm.as400.access.Job;

import java.util.Comparator;

/**
 * Created by root on 7/19/16.
 */
public class SortOnJobNumber implements Comparator<Job>{

    @Override
    public int compare(Job one, Job two) {
        return one.getNumber().compareTo(two.getNumber());
    }
}
