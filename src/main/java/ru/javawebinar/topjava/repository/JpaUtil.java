package ru.javawebinar.topjava.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

public class JpaUtil {

    @PersistenceContext
    private EntityManager em;

    public void clear2ndLevelHibernateCache() {
        Session s = (Session) em.getDelegate();
        SessionFactory sf = s.getSessionFactory();
 //       s.evict(User.class);
        sf.getCache().evictEntity(User.class, BaseEntity.START_SEQ);
        sf.getCache().evictEntityRegion(User.class);
        sf.getCache().evictAllRegions();

    }

    public void evict2ndLevelCache() {
        Session s = (Session) em.getDelegate();
        SessionFactory sf = s.getSessionFactory();
        try {
            Map<String, ClassMetadata> classesMetadata = sf.getAllClassMetadata();
            for (String entityName : classesMetadata.keySet()) {
                sf.getCache().evictCollectionRegion(entityName);
            }
        } catch (Exception e) {
        }
    }
}
