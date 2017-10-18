package com.maple.fastweb.test;

/**
 * Created by Administrator on 2017/10/18.
 */
public class IdGenTest {
    public static void main(String args[]){
        for (int i = 0; i <100 ; i++) {
            System.out.println(IdGen.get().nextId());
        }
    }
}
