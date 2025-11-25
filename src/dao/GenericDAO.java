package dao;

import java.util.List;

public interface GenericDAO<T> {
    void save(T t);
    void delete(String id);
    List<T> getAll();
}