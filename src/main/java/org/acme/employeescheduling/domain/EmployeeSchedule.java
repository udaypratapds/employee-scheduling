package org.acme.employeescheduling.domain;

import java.util.List;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.solver.SolverStatus;
import lombok.*;

@Data
@PlanningSolution
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSchedule {

    @ProblemFactCollectionProperty
    private List<Availability> availabilities;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Employee> employees;

    @PlanningEntityCollectionProperty
    private List<Shift> shifts;

    @PlanningScore
    private HardSoftScore score;

    private ScheduleState scheduleState;

    private SolverStatus solverStatus;

    public EmployeeSchedule(ScheduleState scheduleState, List<Availability> availabilities, List<Employee> employees, List<Shift> shifts) {
        this.scheduleState = scheduleState;
        this.availabilities = availabilities;
        this.employees = employees;
        this.shifts = shifts;
    }

    public EmployeeSchedule(HardSoftScore score, SolverStatus solverStatus) {
        this.score = score;
        this.solverStatus = solverStatus;
    }

    public void setScheduleState(ScheduleState scheduleState) {
        this.scheduleState = scheduleState;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }
}
