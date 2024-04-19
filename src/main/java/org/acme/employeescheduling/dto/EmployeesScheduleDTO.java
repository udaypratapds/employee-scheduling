package org.acme.employeescheduling.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeesScheduleDTO {

    private String name;
    private String position;
    private String domain;
    private List<String> Skills;
    private List<EmployeeScheduleDTO> schedules;
}
