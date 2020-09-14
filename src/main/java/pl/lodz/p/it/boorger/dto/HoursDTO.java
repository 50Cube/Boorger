package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HoursDTO {

    private String creationDate;

    private String mondayStart;
    private String mondayEnd;
    private String tuesdayStart;
    private String tuesdayEnd;
    private String wednesdayStart;
    private String wednesdayEnd;
    private String thursdayStart;
    private String thursdayEnd;
    private String fridayStart;
    private String fridayEnd;
    private String saturdayStart;
    private String saturdayEnd;
    private String sundayStart;
    private String sundayEnd;
}
