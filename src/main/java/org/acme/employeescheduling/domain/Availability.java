package org.acme.employeescheduling.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Availability {

    @PlanningId
    private String id;

    private Employee employee;

    private LocalTime startTime;
    private LocalTime endTime;

    private Set<AvailabilityOnDay> availabilityOnDay;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Availability that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
