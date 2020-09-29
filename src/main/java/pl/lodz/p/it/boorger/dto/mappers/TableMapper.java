package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.TableDTO;
import pl.lodz.p.it.boorger.entities.Table;

import java.util.stream.Collectors;

public class TableMapper {

    public static TableDTO mapToDto(Table table) {
        return TableDTO.builder()
                .number(table.getNumber())
                .capacity(table.getCapacity())
                .active(table.isActive())
                .reservationDTOs(table.getReservations().stream().map(ReservationMapper::mapToDto).collect(Collectors.toList()))
                .build();
    }

    public static Table mapFromDto(TableDTO tableDTO) {
        return Table.builder()
                .number(tableDTO.getNumber())
                .capacity(tableDTO.getCapacity())
                .active(tableDTO.isActive())
                .build();
    }
}
