package com.company;

import java.sql.Connection;

public class app {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting to connecting database ...");
        long startTime = System.currentTimeMillis();
        MySQLDataBase database = new MySQLDataBase();
        Connection con = database.createConnector("root", "root");
        //查找
        database.query(con, "select * from emp");
        //插入
        database.insert(con, "insert into emp(ename,age,brith,hiredate,sal,deptno) values('Lina',19,'1998-01-01','2015-10-10',5000,3)");
        database.query(con, "select * from emp");
        //修改
        database.updates(con, "update emp set sal=8000 where ename='Lina'");
        database.query(con, "select * from emp");
        //删除
        database.delete(con, "delete from emp where ename='Lina'");
        database.query(con, "select * from emp");

        if (database.close(con)) {
            System.out.println("Data Base connection has been closed!");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time consume : " + (endTime - startTime) + " ms");

    }
}