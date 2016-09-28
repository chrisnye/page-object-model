package com.optum.bc4.utils;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ResourcePool<T> {

  private LinkedBlockingQueue <T> poolList = new LinkedBlockingQueue<T>();

  public ResourcePool() {
    
  }

  public ResourcePool(List<T> input) {
    poolList.addAll(input);

  }

  public T getResource() {
    synchronized (this) {
      
      T value = null;
      try {
        value = poolList.poll(1, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      if ( value == null ) {
        //TODO: how to handle??
      }
      //return poolList.take();
        
      return value;
    }
  }

  public void returnResource(T value) {
    if ( value != null ) {
      // Never add null objects to the pool
      synchronized (this) {
        poolList.add(value);
      }
    }
    return;
  }
  
  public int size() {
    return poolList.size();
  }
  
  public String toString() {
    
    return poolList.toString();
  }

}
