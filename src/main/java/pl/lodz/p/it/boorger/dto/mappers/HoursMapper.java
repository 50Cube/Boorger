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
                .mondayStart(DateFormatter.stringToTime(hoursDTO.getMondayStart()))
                .mondayEnd(DateFormatter.stringToTime(hoursDTO.getMondayEnd()))
                .tuesdayStart(DateFormatter.stringToTime(hoursDTO.getTuesdayStart()))
                .tuesdayEnd(DateFormatter.stringToTime(hoursDTO.getTuesdayEnd()))
                .wednesdayStart(DateFormatter.stringToTime(hoursDTO.getWednesdayStart()))
                .wednesdayEnd(DateFormatter.stringToTime(hoursDTO.getWednesdayEnd()))
                .thursdayStart(DateFormatter.stringToTime(hoursDTO.getThursdayStart()))
                .thursdayEnd(DateFormatter.stringToTime(hoursDTO.getThursdayEnd()))
                .fridayStart(DateFormatter.stringToTime(hoursDTO.getFridayStart()))
                .fridayEnd(DateFormatter.stringToTime(hoursDTO.getFridayEnd()))
                .saturdayStart(DateFormatter.stringToTime(hoursDTO.getSaturdayStart()))
                .saturdayEnd(DateFormatter.stringToTime(hoursDTO.getSaturdayEnd()))
                .sundayStart(DateFormatter.stringToTime(hoursDTO.getSundayStart()))
                .sundayEnd(DateFormatter.stringToTime(hoursDTO.getSundayEnd()))
                .build();
    }
}
