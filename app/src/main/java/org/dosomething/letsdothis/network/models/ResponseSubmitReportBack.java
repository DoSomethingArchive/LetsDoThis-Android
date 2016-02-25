package org.dosomething.letsdothis.network.models;

/**
 * Data structure of the response from a reportback submission.
 *
 * Created by izzyoji :) on 4/27/15.
 */
public class ResponseSubmitReportBack {
    public Wrapper data;

    public class Wrapper {
        // Reportback ID
        public String id;
    }
}
