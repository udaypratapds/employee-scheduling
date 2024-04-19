package org.acme.employeescheduling.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeesScheduleDTO {

    private String name;
    private String position;
    private String domain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setSkills(List<String> skills) {
        Skills = skills;
    }

    public void setSchedules(List<EmployeeScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    public String getPosition() {
        return position;
    }

    public String getDomain() {
        return domain;
    }

    public List<String> getSkills() {
        return Skills;
    }

    public List<EmployeeScheduleDTO> getSchedules() {
        return schedules;
    }

    public EmployeesScheduleDTO(String name, String position, String domain, List<String> skills, List<EmployeeScheduleDTO> schedules) {
        this.name = name;
        this.position = position;
        this.domain = domain;
        Skills = skills;
        this.schedules = schedules;
    }

    private List<String> Skills;
    private List<EmployeeScheduleDTO> schedules;
}
