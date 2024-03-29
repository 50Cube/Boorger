package pl.lodz.p.it.boorger.entities.abstraction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @NotBlank
    @Size(min = 36, max = 36)
    @Setter(lombok.AccessLevel.NONE)
    protected String businessKey;

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@$^&*]+")
    protected String createdBy;

    @NotNull
    protected LocalDateTime creationDate;

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@$^&*]+")
    protected String modifiedBy;
    protected LocalDateTime modificationDate;

    @Version
    @NotNull
    @Setter(lombok.AccessLevel.NONE)
    protected long version;

    public AbstractEntity() {
        this.businessKey = UUID.randomUUID().toString();
        this.creationDate = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void updateModificationDetails() {
        this.modificationDate = LocalDateTime.now();
    }
}
