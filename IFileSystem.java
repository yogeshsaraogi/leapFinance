package com.leapfinance;

public interface IFileSystem {

    public void create(String key,String value, long timeToLive) throws Exception;
    public void  delete(String key) throws Exception;
    public String read(String key) throws Exception;

}
