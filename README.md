Idea behind this library is to create read and delete key value from file system as per users/client convenience.

IFileSystem is an interface containing all three major implementation method which is implemented by MyFileSystemImpl class.

Brief Explaination of all methods are given below:-

Constructor:- It creates a new file and check if there any such file exists or not and based on that it will create a new file or continue with the existing file and if any such error occured it will throw an exception.

Methods:-

1)public synchronized void create(String key,String value, long timeToLive) :-  Created this as synchronized so that if more than one threads come it will execute one by one make it thread safe. In this method I have checked key and value size which is 32 character and 16KB before adding to file system. And throw an exception if any such error occured.
delete(null) will check time to live condition and if any key value exceeds the time it should get deleted from file.

2) public synchronized void delete :- Created this as synchronized so that if more than one threads come it will execute one by one make it thread safe. If any such key is present in file it will delete there key, value and timeToLive. It take the help of linked list to delete the file. Initially all the required data should be copy in linked list and then copy it into desired file. If any such error occured during the execution it will throw an exception.

3) public synchronized String read(String key) :- Created this as synchronized so that if more than one threads come it will execute one by one make it thread safe. If any such present it will return its value. If any such error occured during the execution it will throw an exception.

4) private boolean ifKeyExist(String key) :- It is a helper function which checks if given key is present in file.

5) private String liveTime(long time) :- It is a helper function which convert time in string.
