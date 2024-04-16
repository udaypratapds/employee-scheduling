package org.acme.employeescheduling.domain;

import java.util.*;
public class Schedule {
    private List<Eshift> schedule;

    public List<Eshift> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Eshift> schedule) {
        this.schedule = schedule;
    }
}
