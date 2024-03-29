package pl.lodz.p.it.boorger.entities;

import lombok.*;
import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hours extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private LocalTime mondayStart;

    @NotNull
    private LocalTime mondayEnd;

    @NotNull
    private LocalTime tuesdayStart;

    @NotNull
    private LocalTime tuesdayEnd;

    @NotNull
    private LocalTime wednesdayStart;

    @NotNull
    private LocalTime wednesdayEnd;

    @NotNull
    private LocalTime thursdayStart;

    @NotNull
    private LocalTime thursdayEnd;

    @NotNull
    private LocalTime fridayStart;

    @NotNull
    private LocalTime fridayEnd;

    @NotNull
    private LocalTime saturdayStart;

    @NotNull
    private LocalTime saturdayEnd;

    @NotNull
    private LocalTime sundayStart;

    @NotNull
    private LocalTime sundayEnd;

    @NotNull
    @OneToOne
    private Restaurant restaurant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hours that = (Hours) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        if (!Objects.equals(mondayStart, that.mondayStart)) return false;
        if (!Objects.equals(mondayEnd, that.mondayEnd)) return false;
        if (!Objects.equals(tuesdayStart, that.tuesdayStart)) return false;
        if (!Objects.equals(tuesdayEnd, that.tuesdayEnd)) return false;
        if (!Objects.equals(wednesdayStart, that.wednesdayStart)) return false;
        if (!Objects.equals(wednesdayEnd, that.wednesdayEnd)) return false;
        if (!Objects.equals(thursdayStart, that.thursdayStart)) return false;
        if (!Objects.equals(thursdayEnd, that.thursdayEnd)) return false;
        if (!Objects.equals(fridayStart, that.fridayStart)) return false;
        if (!Objects.equals(fridayEnd, that.fridayEnd)) return false;
        if (!Objects.equals(saturdayStart, that.saturdayStart)) return false;
        if (!Objects.equals(saturdayEnd, that.saturdayEnd)) return false;
        if (!Objects.equals(sundayStart, that.sundayStart)) return false;
        return Objects.equals(sundayEnd, that.sundayEnd);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (businessKey != null ? businessKey.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        result = 31 * result + (modificationDate != null ? modificationDate.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (mondayStart != null ? mondayStart.hashCode() : 0);
        result = 31 * result + (mondayEnd != null ? mondayEnd.hashCode() : 0);
        result = 31 * result + (tuesdayStart != null ? tuesdayStart.hashCode() : 0);
        result = 31 * result + (tuesdayEnd != null ? tuesdayEnd.hashCode() : 0);
        result = 31 * result + (wednesdayStart != null ? wednesdayStart.hashCode() : 0);
        result = 31 * result + (wednesdayEnd != null ? wednesdayEnd.hashCode() : 0);
        result = 31 * result + (thursdayStart != null ? thursdayStart.hashCode() : 0);
        result = 31 * result + (thursdayEnd != null ? thursdayEnd.hashCode() : 0);
        result = 31 * result + (fridayStart != null ? fridayStart.hashCode() : 0);
        result = 31 * result + (fridayEnd != null ? fridayEnd.hashCode() : 0);
        result = 31 * result + (saturdayStart != null ? saturdayStart.hashCode() : 0);
        result = 31 * result + (saturdayEnd != null ? saturdayEnd.hashCode() : 0);
        result = 31 * result + (sundayStart != null ? sundayStart.hashCode() : 0);
        result = 31 * result + (sundayEnd != null ? sundayEnd.hashCode() : 0);
        return result;
    }
}
