package org.acme.employeescheduling.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RequiredShift {

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;
}
