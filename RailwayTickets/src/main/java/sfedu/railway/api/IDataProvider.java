package sfedu.railway.api;

public interface IDataProvider<T> {
    void saveRecord(T record);
    void deleteRecord(String id);
    T getRecordById(String id);
    void initDataSource();
}
