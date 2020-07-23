package pl.lodz.p.it.boorger.abstraction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @NotBlank
    protected String businessKey;

    @NotBlank
    protected String createdBy;

    @NotNull
    protected LocalDateTime creationDate;
    protected String modifiedBy;
    protected LocalDateTime modificationDate;

    @Version
    @NotNull
    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    protected long version;
}
