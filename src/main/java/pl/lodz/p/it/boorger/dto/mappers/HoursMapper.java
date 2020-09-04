package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.HoursDTO;
import pl.lodz.p.it.boorger.entities.Hours;
import pl.lodz.p.it.boorger.utils.DateFormatter;

public class HoursMapper {

    public static HoursDTO mapToDto(Hours hours) {
        return HoursDTO.builder()
                .mondayStart(DateFormatter.timeToString(hours.getMondayStart()))
                .mondayEnd(DateFormatter.timeToString(hours.getMondayEnd()))
                .tuesdayStart(DateFormatter.timeToString(hours.getTuesdayStart()))
                .tuesdayEnd(DateFormatter.timeToString(hours.getTuesdayEnd()))
                .wednesdayStart(DateFormatter.timeToString(hours.getWednesdayStart()))
                .wednesdayEnd(DateFormatter.timeToString(hours.getWednesdayEnd()))
                .thursdayStart(DateFormatter.timeToString(hours.getThursdayStart()))
                .thursdayEnd(DateFormatter.timeToString(hours.getThursdayEnd()))
                .fridayStart(DateFormatter.timeToString(hours.getFridayStart()))
                .fridayEnd(DateFormatter.timeToString(hours.getFridayEnd()))
                .saturdayStart(DateFormatter.timeToString(hours.getSaturdayStart()))
                .saturdayEnd(DateFormatter.timeToString(hours.getSaturdayEnd()))
                .sundayStart(DateFormatter.timeToString(hours.getSundayStart()))
                .sundayEnd(DateFormatter.timeToString(hours.getSundayEnd()))
                .build();
    }

    public static Hours mapFromDto(HoursDTO hoursDTO) {
        return Hours.builder()
                .mondayStart(DateFormatter.StringToTime(hoursDTO.getMondayStart()))
                .mondayEnd(DateFormatter.StringToTime(hoursDTO.getMondayEnd()))
                .tuesdayStart(DateFormatter.StringToTime(hoursDTO.getTuesdayStart()))
                .tuesdayEnd(DateFormatter.StringToTime(hoursDTO.getTuesdayEnd()))
                .wednesdayStart(DateFormatter.StringToTime(hoursDTO.getWednesdayStart()))
                .wednesdayEnd(DateFormatter.StringToTime(hoursDTO.getWednesdayEnd()))
                .thursdayStart(DateFormatter.StringToTime(hoursDTO.getThursdayStart()))
                .thursdayEnd(DateFormatter.StringToTime(hoursDTO.getThursdayEnd()))
                .fridayStart(DateFormatter.StringToTime(hoursDTO.getFridayStart()))
                .fridayEnd(DateFormatter.StringToTime(hoursDTO.getFridayEnd()))
                .saturdayStart(DateFormatter.StringToTime(hoursDTO.getSaturdayStart()))
                .saturdayEnd(DateFormatter.StringToTime(hoursDTO.getSaturdayEnd()))
                .sundayStart(DateFormatter.StringToTime(hoursDTO.getSundayStart()))
                .sundayEnd(DateFormatter.StringToTime(hoursDTO.getSundayEnd()))
                .build();
    }
}
