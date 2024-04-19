package org.acme.employeescheduling.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RequiredSkill {

    @SerializedName("skill_name")
    private String skillName;

    @SerializedName("minimum_employee_count")
    private Integer minimumEmployeeCount;
}
