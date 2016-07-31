package com.as400.workjob;

import com.ibm.as400.access.*;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by root on 7/11/16.
 */
public class SortOnCPUTime implements Comparator<Job>{
    public int compare(Job a,Job b) {
        try {
            int aCPUUsed = a.getCPUUsed();
            int bCPUUsed = b.getCPUUsed();

            if (aCPUUsed == bCPUUsed) return 0;
            else return bCPUUsed - aCPUUsed;

        } catch(AS400Exception e0){
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
}
