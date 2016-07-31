package com.as400.workjob;

import com.ibm.as400.access.*;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by root on 7/11/16.
 */
public class SortOnSubSys implements Comparator<Job> {
    public int compare(Job one, Job two) {
        try {
            return one.getSubsystem().compareTo(two.getSubsystem());
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
        return 0;
    }
}
