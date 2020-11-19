package pl.lodz.p.it.boorger.cache;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.boorger.entities.Dish;
import pl.lodz.p.it.boorger.utils.TranslationService;

@Component
@Transactional(propagation = Propagation.NEVER)
public class CacheService {

    private static final String CACHE_NAME = "boorger";

    private static Cache<String, DishTranslation> cache;

    static {
        EmbeddedCacheManager cacheManager = new DefaultCacheManager();
        cacheManager.defineConfiguration(CACHE_NAME, new ConfigurationBuilder().build());
        cache = cacheManager.getCache(CACHE_NAME);
    }

    public static void addToCache(DishTranslation element) {
        element.setTranslation(TranslationService.translate(element.getTranslation(), element.getSourceLanguage(), element.getLanguage()));
        cache.put(element.getCacheKey(), element);
    }

    public static DishTranslation getFromCache(Dish dish, String language) {
        DishTranslation dishTranslation = cache.get(dish.getBusinessKey().concat(language));
        if(dishTranslation == null) {
            dishTranslation = new DishTranslation(
                    dish.getBusinessKey(),
                    language,
                    dish.getDescriptionLanguage(),
                    TranslationService.translate(dish.getDescription(), dish.getDescriptionLanguage(), language));
            cache.put(dishTranslation.getCacheKey(), dishTranslation);
        }
        return dishTranslation;
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void updateCache() {
        for(DishTranslation translation : cache.values()) {
            translation.setTranslation(TranslationService.translate(translation.getTranslation(),
                    translation.getSourceLanguage(), translation.getLanguage()));
            cache.put(translation.getCacheKey(), translation);
        }
    }
}
