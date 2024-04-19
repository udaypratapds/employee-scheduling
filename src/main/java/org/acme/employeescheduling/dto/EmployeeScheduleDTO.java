package org.acme.employeescheduling.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeScheduleDTO {

    private List<ScheduleDTO> schedule;
}
