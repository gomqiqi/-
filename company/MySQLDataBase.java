package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 根据所给出的用户名和密码，连接本地数据库
 * 数据库名称  ： study
 * 数据表名称  ： emp
 * 端口号     ： 3306
 * 用户名     ： root
 * 密码       ： root
 */
public class MySQLDataBase {
    //声明connection对象，存储连接数据库的引用
    private Connection con = null;;
    //存储用户名和密码信息
    private String username = null;
    private String password = null;

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Connection getCon() {
        return con;
    }
    /**
     * 创建数据库连接
     * @param username
     * @param password
     * @return
     */
    public Connection createConnector(String username,String password)
    {
        Connection conn = null;
        //数据库连接驱动名：针对不同的数据库，驱动名称不同，但是同一种类型的数据库改字符串相同
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库study,useSSL用来指明是否需要使用SSL协议，如果不指明将会报错
        String url = "jdbc:mysql://localhost:3306/study?useSSL=true";
        try
        {
            //加载驱动程序
            Class.forName(driver);
            //连接数据库
            conn = DriverManager.getConnection(url, username, password);
            //检查数据库连接是否成功
            if(!conn.isClosed())
            {
                System.out.println("Successed connecting to the Database!");
                //存储当前连接的用户的用户名和密码
                this.username = username;
                this.password = password;
                this.con = conn;
            }

        }catch (ClassNotFoundException e) {
            System.out.println("Sorry, can't find the Driver!");
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
    /**
     * 断开数据库连接
     * @param conn
     * @return
     */
    public boolean close(Connection conn)
    {
        try {
            if(!conn.isClosed())
            {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 执行数据库操作语句
     * @param conn
     * @param query
     */
    public void query(Connection conn,String query)
    {
        //创建Statement对象，用来执行SQL语句
        Statement statement;
        try {
            statement = conn.createStatement();
            //需要执行的数据库操作语句
            String sql = query;
            //执行数据库操作语句并返回结果
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("-------------------");
            System.out.println("---执行查询结果如下所示---");
            System.out.println("-------------------");
            System.out.println("姓名"+"\t"+"年龄"+"\t"+"工资");

            String name = null;
            int age,sal;
            //打印查询结果
            while(rs.next())
            {
                name = rs.getString("ename");
                age = rs.getInt("age");
                sal = rs.getInt("sal");
                System.out.println(name + "\t" + age + "\t" + sal);
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 增加一条数据到指定数据库的指定表中
     * @param conn
     * @param command
     */
    public boolean insert(Connection conn, String command)
    {
        Statement statement;
        try {
            statement = conn.createStatement();
            String sql = command;
            /**
             * Statement接口中的excuteUpdate()方法执行给定的
             * SQL语句，该语句可以是INSERT,UPDATE,DELETE语句
             */
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Connection conn, String command)
    {
        Statement statement;
        try {
            statement = conn.createStatement();
            String sql = command;
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 更新数据库操作
     * @param conn
     * @param command
     * @return
     */
    public boolean updates(Connection conn, String command)
    {
        Statement statement;
        try {
            statement = con.createStatement();
            String sql = command;
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}