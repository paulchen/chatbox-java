package at.rueckgr.chatbox.util;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;

public class CustomEntityManager implements EntityManager {
    private final EntityManager delegate;

    public CustomEntityManager(EntityManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public void persist(Object o) {
        delegate.persist(o);
    }

    @Override
    public <T> T merge(T t) {
        return delegate.merge(t);
    }

    @Override
    public void remove(Object o) {
        delegate.remove(o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return delegate.find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return delegate.find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return delegate.find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return delegate.find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return delegate.getReference(aClass, o);
    }

    @Override
    public void flush() {
        delegate.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        delegate.setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return delegate.getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        delegate.lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        delegate.lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        delegate.refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        delegate.refresh(o, map);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public void detach(Object o) {
        delegate.detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return delegate.getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        delegate.setProperty(s, o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return delegate.createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return delegate.createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return delegate.createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return delegate.createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return delegate.createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return delegate.createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return delegate.createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return delegate.createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return delegate.createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return delegate.createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return delegate.createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return delegate.createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return delegate.createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return delegate.createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        delegate.joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return delegate.isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return delegate.unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return delegate.getDelegate();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return delegate.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return delegate.createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return delegate.createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return delegate.getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return delegate.getEntityGraphs(aClass);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        delegate.refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        delegate.refresh(o, lockModeType, map);
    }
}
