package pl.lodz.p.it.boorger.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DishTranslation {

    private String businessKey;
    private String language;
    private String translation;

    public String getCacheKey() {
        return businessKey + language;
    }
}
