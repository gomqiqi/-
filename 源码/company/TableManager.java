package com.company;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.IOException;
import java.util.regex.Pattern;

public class TableManager {
    private static final Pattern PATTERN = Pattern.compile("0|([-]?[1-9][0-9]*)");
    public static void main(String[] args) {

        while(true){
            System.out.print("miDB>");
        Scanner sc=new Scanner(System.in);
        String conmand=sc.nextLine();
        Operate(conmand);

}


    }
    public static void Operate(String conmand)
    {
        if(judge_Insert(conmand.toLowerCase()))
        {

            String[] Conmand1=conmand.split(" ");
            String filename=Conmand1[2];
            if(search_same(filename)==true)
            {
                System.out.println("抱歉，输入文件名称不存在！");
            }
            else
            {
                String[] Conmand2=conmand.substring(conmand.indexOf("(")+1,conmand.indexOf(")")).split(", ");
                insert(filename,Conmand2[0],Conmand2[1],Conmand2[2]);
            }


        }else if(judge_Create(conmand.toLowerCase()))
        {
            String[] Conmand1=conmand.split(" ");
            Conmand1[2]=Conmand1[2].substring(0,Conmand1[2].indexOf("("));
            String filename=Conmand1[2];
            if(!search_same(filename))
            {
                System.out.println("抱歉，输入文件已存在！");
            }
            else
            {
                int len1,len2,len3;
                String[] Conmand2=conmand.substring(conmand.indexOf("(")+1,conmand.length()-1).split(", ");
                String[] CloA=Conmand2[0].split(" ");
                if(CloA[1].indexOf("(")==-1)
                {
                    len1=1;
                }else
                {
                    len1=Integer.parseInt(CloA[1].substring(CloA[1].indexOf("(")+1,CloA[1].length()-1));

                    CloA[1]=CloA[1].substring(0,CloA[1].indexOf("("));

                }
                String[] CloB=Conmand2[1].split(" ");
                if(CloB[1].indexOf("(")==-1)
                {
                    len2=1;
                }else
                {
                    len2=Integer.parseInt(CloB[1].substring(CloB[1].indexOf("(")+1,CloB[1].length()-1));

                    CloB[1]=CloB[1].substring(0,CloB[1].indexOf("("));

                }
                String[] CloC=Conmand2[2].split(" ");
                if(CloC[1].indexOf("(")==-1)
                {
                    len3=1;
                }else
                {
                    len3=Integer.parseInt(CloC[1].substring(CloC[1].indexOf("(")+1,CloC[1].length()-1));
                    CloC[1]=CloC[1].substring(0,CloC[1].indexOf("("));


                }
                try {
                    create(filename,CloA[0],CloA[1],CloB[0],CloB[1],CloC[0],CloC[1],len1,len2,len3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // insert(filename,Conmand2[0],Conmand2[1],Conmand2[2]);
            }

        }else if(judge_Desc(conmand.toLowerCase()))
        {
            String[] Conmand1=conmand.split(" ");
            String filename=Conmand1[1];
            if(search_same(filename))
            {
                System.out.println("抱歉，输入文件名称不存在！");
            }
            else
            {
                desc(filename);
            }

        }else if(judge_show(conmand.toLowerCase()))
        {
            show();
        }else if(judge_drop(conmand.toLowerCase()))
        {
            String[] Conmand1=conmand.split(" ");
            String filename=Conmand1[2];
            if(search_same(filename))
            {
                System.out.println("抱歉，输入文件名称不存在！");
            }
            else
            {
                drop(filename);
            }
        }else if(judge_Update(conmand.toLowerCase()))
        {
            String[] Conmand1=conmand.split(" ");
            String filename=Conmand1[1];
            if(search_same(filename))
            {
                System.out.println("抱歉，输入文件名称不存在！");
            }
            else
            {

                try {
                    update(filename,Conmand1[3],Conmand1[5],Conmand1[7],Conmand1[8],Conmand1[9]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(judge_Select1(conmand.toLowerCase()))
        {
            String[] Conmand1=conmand.split(" ");
            String filename=Conmand1[3];

            if(search_same(filename))
            {
                System.out.println("抱歉，输入文件名称不存在！");
            }
            else
            {
                if(Conmand1.length==4)
                selete(filename);
                else
                {
                    if(conmand.toLowerCase().indexOf("where")!=-1)
                    {
                        String[][] type=getstruct(filename);
                        String[]date=new String[3];
                        for(int i=0;i<3;i++)
                        {
                            date[i]=type[i][0];
                        }
                        if(conmand.toLowerCase().indexOf("order by")!=-1) {
                            if(Conmand1.length==12) {
                                try {
                                    selete(filename, date, Conmand1[5], Conmand1[6], Conmand1[7], Conmand1[10], "asc");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else
                            {
                                try {
                                    selete(filename, date, Conmand1[5], Conmand1[6], Conmand1[7], Conmand1[10], Conmand1[11]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else
                        {
                            try {
                                selete(filename,date,Conmand1[5],Conmand1[6], Conmand1[7]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }else if(judge_Select2(conmand.toLowerCase()))
        {
            String Conmand1=conmand.substring(7,conmand.indexOf(" from"));
            String[]data=Conmand1.split(", ");
            String Conmand2=conmand.substring(conmand.indexOf("from")+5,conmand.length());
            int flag=1;
            if(!Conmand2.contains("order by"))//不排序
            {
                flag=2;
            }
            String[]Conmand3=Conmand2.split(" ");
            String filename=Conmand3[0];

            if(search_same(filename))
            {
                System.out.println("抱歉，输入文件名称不存在！");
            }
            else
            {

                if(flag==1) {
                    if (Conmand3.length == 9) {
                        try {
                            selete(filename, data, Conmand3[2], Conmand3[3], Conmand3[4], Conmand3[7], Conmand3[8]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try {
                            selete(filename, data, Conmand3[2], Conmand3[3], Conmand3[4], Conmand3[7],"asc");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(flag==2)
                {
                    try {
                        selete(filename, data, Conmand3[2], Conmand3[3], Conmand3[4]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }else if(judge_delete(conmand.toLowerCase()))
        {
            String[]Conmand1=conmand.split(" ");
            String filename=Conmand1[2];
            if(search_same(filename))
            {
                System.out.println("抱歉，输入文件名称不存在！");
            }
            else
            {
                try {
                    delete(filename,Conmand1[4],Conmand1[5],Conmand1[6]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(judge_quit(conmand.toLowerCase()))
        {
            System.exit(0);
        }else if(judge_help(conmand.toLowerCase()))
        {
            System.out.println("quit ---- 退出迷你数据库管理系统。");
            System.out.println("help ---- 显示所有的指令。");
            System.out.println("show tables ---- 显示目前所有数据表。");
            System.out.println("desc table XXX ---- 显示数据表XXX中的表结构。");
            System.out.println("create table XXX(columnA varchar(10), columnB int, columnC decimal) ---- 创建一个3列的名称为XXX的表格,列名称分别为columnA、columnB、columnC,其类型分别为10个以内的字符、整型数和小数。");
            System.out.println("drop table XXX ---- 删除表格XXX。");
            System.out.println("select colX, colY, colX from XXX where colZ > 1.5 order by colZ desc ---- 从数据表XXX中选取三列，colX，colY,colX，每一个记录必须满足colZ的值大于1.5 且显示时按照colZ这一列的降序排序。");
            System.out.println("select * from XXX where colA <> '北林信息' ---- 从数据表XXX中选取所有的列，但记录要满足列colA不是北林信息。");
            System.out.println("delete from XXX where colB = 10 ---- 向数据表XXX中colB列的值是10的记录全部删除。");
            System.out.println("update XXX set colD = '计算机科学与技术' where colA = '北林信息' ---- 显示数据表XXX中的表结构。");
        }
        else {
            System.out.println("格式不正确");
        }
    }
    public static boolean judge_help(String comand)
    {
        try {
            String regex = "help";
            if (comand.matches(regex))
                return true;
        } catch (StringIndexOutOfBoundsException s) {
        }
        return false;

    }
    public static boolean judge_quit(String comand)
    {

            try {
                String regex = "quit";
                if (comand.matches(regex))
                    return true;
            } catch (StringIndexOutOfBoundsException s) {
            }
            return false;

    }

    public static boolean judge_delete(String comand)
    {
            if(comand.indexOf(" where")!=-1) {
                comand=comand.substring(0,comand.indexOf(" where"));
                try {
                    String regex = "delete\\sfrom\\s\\w*";
                    if (comand.matches(regex))
                        return true;
                } catch (StringIndexOutOfBoundsException s) {
                }
            }
        return false;
    }

    public static boolean judge_Select1(String comand)
    {
        try {
            String regex="select\\s\\*\\sfrom\\s\\w*";
            if(comand.matches(regex))
                return true;
        }catch (StringIndexOutOfBoundsException s) {
        }
        return false;
    }
    public static boolean judge_Select2(String comand)
    {
        if(comand.indexOf("select")!=-1&&comand.indexOf("from")!=-1&&comand.indexOf("where")!=-1)
            return true;
        return false;
    }
    public static boolean judge_Update(String comand)
    {
        if(comand.indexOf(" set")!=-1) {
            try {
                comand = comand.substring(0, comand.indexOf(" set"));
                String regex = "update\\s\\w*";
                if (comand.matches(regex))
                    return true;
            } catch (StringIndexOutOfBoundsException s) {
            }
        }
        return false;
    }

    public static boolean judge_drop(String comand)
    {
        try {
            String regex="drop\\stable\\s\\w*";
            if(comand.matches(regex))
                return true;
        }catch (StringIndexOutOfBoundsException s) {
        }
        return false;
    }
    public static boolean judge_show(String comand)
    {
        try {
            String regex="show\\stables";
            if(comand.matches(regex))
                return true;
        }catch (StringIndexOutOfBoundsException s) {
        }
        return false;
    }
    public static boolean judge_Desc(String comand)
    {
        try {
            String regex="desc\\s\\w*";
            if(comand.matches(regex))
                return true;
        }catch (StringIndexOutOfBoundsException s) {
        }
        return false;
    }
    public static boolean judge_Insert(String comand)
    {
        try {
        String regex="insert\\sinto\\s\\w*\\svalues";
        if(comand.substring(0,comand.indexOf("(")).matches(regex))
            return true;
    }catch (StringIndexOutOfBoundsException s) {
    }
       return false;
    }
    public static boolean judge_Create(String comand)
    {
        try {
            String regex="create\\stable\\s\\w*";
            if(comand.substring(0,comand.indexOf("(")).matches(regex))
                return true;
        }catch (StringIndexOutOfBoundsException s) {
        }
        return false;
    }
    private static final String readDbHome_data() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("Config.properties"));
            String dbHomePath = properties.getProperty("dbHome_data");
            return dbHomePath;
        } catch (IOException ioe) {
            return null;
        }
    }
    private static final String readDbHome_struct() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("Config.properties"));
            String dbHomePath = properties.getProperty("dbHome_struct");
            return dbHomePath;
        } catch (IOException ioe) {
            return null;
        }
    }
    public  static  void create(String filename,String data1,String data1_wid,String data2,String data2_wid,String data3,String data3_wid,int len1,int len2,int len3) throws IOException
    {


        String filepath1=readDbHome_data()+"/"+filename+".midb";
        String filepath2=readDbHome_struct()+"/"+filename+".li";
        File file1=new File(filepath1);
        File file2=new File(filepath2);

        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!file1.exists())
            file1.createNewFile();

//        String str0 = data1+","+data2+","+data3+"\n";
//        byte[] bytes0=str0.getBytes(StandardCharsets.UTF_8);
//        raf0.write(bytes0);

        RandomAccessFile raf = new RandomAccessFile(file2,"rw");
        String str = data1+","+data1_wid+","+len1+"\n"+data2+","+data2_wid+","+len2+"\n"+data3+","+data3_wid+","+len3+"\n";
        byte[] bytes=str.getBytes(StandardCharsets.UTF_8);
        raf.write(bytes);
        System.out.println("创建"+filename+"数据表成功");
        raf.close();
    }

    public static void drop(String filename)
    {
        //删除files文件夹下文件名为name.txt的文件
        String path_data=readDbHome_data();
        String path_struct=readDbHome_struct();


        File folder_data = new File(path_data);
        File[] files_data = folder_data.listFiles();
        File folder_struct = new File(path_struct);
        File[] files_struct = folder_struct.listFiles();
        for(File file:files_data){
            if(file.getName().equals(filename+".midb")){
                file.delete();
            }
        }
        for(File file:files_struct){
            if(file.getName().equals(filename+".li")){
                file.delete();
            }
        }
        System.out.println("删除成功！");

    }
    public static void desc(String filename)////把名称为XXX的数据表的结构显示出来
    {   String path_struct=readDbHome_struct()+"/"+filename+".li";
        RandomAccessFile raf2 = null;
        try {
            raf2 = new RandomAccessFile(path_struct,"rw");
            byte[] buf2 = new byte[1024];
            int len = 0;
            System.out.println("列名称，列类型，列宽度");
            while((len = raf2.read(buf2))>0) {
                byte[] buf22=new byte[len];
                for(int i=0;i<len;i++) {
                    buf22[i] = buf2[i];
                }
                String words=new String(buf22,StandardCharsets.UTF_8);
                System.out.println(words);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//
//            BufferedReader br=new BufferedReader(new FileReader(path_struct));
//            String struct= br.readLine();
//
//            while(struct!=null)
//            {
//                System.out.println(struct);
//                struct = br.readLine();
//            }
//            br.close();
//        }catch(IOException e){
//            e.printStackTrace();
//        }

        try {
            raf2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void show()
    {
        String path=readDbHome_data();
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        int num=0;
        System.out.println("数据表");
        for(int i=0;i<array.length;i++)
        {
            if(array[i].isFile())//如果是文件
            {
                num++;
                System.out.println(array[i].getName());
            }
        }
        System.out.println("共有"+num+"个数据表");
    }

    public static void insert(String filename,String v1,String v2,String v3)
    {

        String [][]Struct=new String[4][3];
        for(int i=0;i<4;i++)
        {
            Struct[i][0]="";
            Struct[i][1]="";
            Struct[i][2]="";
        }
        String words=null;
        String path_struct=readDbHome_struct()+"/"+filename+".li";
        RandomAccessFile raf2 = null;
        try {
            raf2 = new RandomAccessFile(path_struct,"rw");
            byte[] buf2 = new byte[1024];
            int len = 0;

            int k;
            while((len = raf2.read(buf2))>0) {
                byte[] buf22=new byte[len];
                for(int i=0;i<len;i++) {

                    buf22[i] = buf2[i];
                }
                words=new String(buf22,StandardCharsets.UTF_8);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String count[]=words.split("\n");
        String colA[]=count[0].split(",");
        String colB[]=count[1].split(",");
        String colC[]=count[2].split(",");
        String[][] type=new String[3][3];
        String[] v=new String[3];
        for(int i=0;i<3;i++)
        {
            v[0]=v1;
            type[0][i]=colA[i];
        }
        for(int i=0;i<3;i++)
        {
            v[1]=v2;
            type[1][i]=colB[i];
        }
        for(int i=0;i<3;i++)
        {
            v[2]=v3;
            type[2][i]=colC[i];
        }

        int flag=0;
        for(int i=0;i<3;i++)
        {
            if(type[i][1].equals("int"))
            {
                if(!PATTERN.matcher(v[i]).matches())
                {
                    flag=1;
                    break;
                }
            }
            if(type[i][1].equals("decimal"))
            {

                if(!isDouble(v[i]))
                {
                    flag=2;
                    break;
                }

            }
            if (type[i][1].equals("varchar"))
            {
                if(v[i].charAt(0)!='\''||v[i].charAt(v[i].length()-1)!='\'')
                {
                    flag=3;
                    break;
                }
            }
        }
        if(flag==0)
        {
            File f=new File(readDbHome_data()+"/"+filename+".midb");
            try {
                RandomAccessFile randomAccessFile1=new RandomAccessFile(f,"rw");
                randomAccessFile1.seek(randomAccessFile1.length());
                for(int i=0;i<3;i++)
                {
                    if(type[i][1].equals("int"))
                    {
                        int Int=Integer.parseInt(v[i]);

                        randomAccessFile1.write(Transformer.intToBytes(Int));

                    }
                    if(type[i][1].equals("decimal"))
                    {
                        double Dou=0.0;
                        int j;
                        for(j=0;j<v[i].length();j++)
                        {
                            if(v[i].charAt(j)=='.')
                            {
                              break;
                            }
                        }
                        if(j==v[i].length())
                        {
                            j=Integer.parseInt(v[i]);
                            BigDecimal decimal=new BigDecimal(j);
                            Dou=decimal.setScale(1).doubleValue();
                        }else
                        {
                            Dou=Double.parseDouble(v[i]);
                        }

                        randomAccessFile1.write(Transformer.doubleToBytes(Dou));


                    }
                    if (type[i][1].equals("varchar"))
                    {
                        String str=v[i].substring(1,v[i].length()-1);
                        int len=Integer.parseInt(type[i][2])*3;
                        byte[] Byte=new byte[len];
                        while(true)
                        {
                            if(str.getBytes(StandardCharsets.UTF_8).length<len)
                                str+="0";
                            else break;
                        }

                        Byte=str.getBytes(StandardCharsets.UTF_8);
                        randomAccessFile1.write(Byte);
                    }

                }
                randomAccessFile1.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("向 "+filename+" 数据表中插入一条数据");

        }else if(flag==1)
        {
            System.out.println("抱歉，int类型输入错误");
        }else if(flag==2)
        {
            System.out.println("抱歉，decimal类型输入错误");
        }else  if(flag==3)
        {
            System.out.println("抱歉，varchar数据需要用单引号包围");
        }
//        Object V1=judge_struct(v1,colA[1],Integer.parseInt(colA[2]));
//        Object V2=judge_struct(v2,colB[1],Integer.parseInt(colB[2]));
//        Object V3=judge_struct(v3,colC[1],Integer.parseInt(colC[2]));
//
//
//        System.out.println(V1);
//        System.out.println(V2);
//        System.out.println(V3);

        try {
            raf2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object judge_struct(String v,String struct,int len)
    {
        if(struct.equals("int"))
        {
            if(PATTERN.matcher(v).matches())
            {     Integer Int;
                Int=Integer.parseInt(v);
                return Int;
            }
        }
        if(struct.equals("decimal"))
        {
            Double flo=0.0;
            flo=Double.parseDouble(v);
            return flo;
        }
        if (struct.equals("varchar"))
        {
            if(v.charAt(0)!='\''||v.charAt(v.length()-1)!='\'')
            {
                return 0;
            }
            int Len=v.length();
            String Varchar=v.substring(1,Len-1);
            return Varchar;
        }
        return -1;
    }

    public static boolean search_same(String filename)          //判断是否已经存在文件
    {
                 String path=readDbHome_data();
                File file = new File(path);
                // 获得该文件夹内的所有文件
                File[] array = file.listFiles();
                filename+=".midb";
                int flag=0;
                for(int i=0;i<array.length;i++)
                {
                    if(array[i].isFile())//如果是文件
                    {

                        if(array[i].getName().equals(filename))
                        {
                            return false;
                        }
                    }
                }
                return true;

    }
    public static void selete(String filename,String[] date,String chose_data,String operate,String chose_num,String oder_data,String cmd)throws IOException {
        int asc = 1;          //升序标志
        boolean flagAscend=true;
        if (cmd.equals("desc")) {
            flagAscend=false;
            asc = 0;
        }
        String[][] type = getstruct(filename);
        int []date_at=new int[date.length];
        for(int i=0;i<date.length;i++)
        {
            for(int j=0;j<3;j++)
            {

                if(date[i].equals(type[j][0]))
                {
                    date_at[i]=j+1;
                    break;
                }
            }
        }



        String path_data = readDbHome_data() + "/" + filename + ".midb";
        String path_struct = readDbHome_struct() + "/" + filename + ".li";
        RandomAccessFile randomAccessFile = new RandomAccessFile(path_data, "rw");
        int part_len = 0;
        int[] order_len = new int[4];   //按顺序存三个数据每个的长度
        int where = 0;
        int oder_where=0;
        String struct = "";
        for (int i = 0; i < 3; i++) {
            if (type[i][0].equals(chose_data)) {
                where = i + 1;
                struct = type[i][1];
            }
            if (type[i][0].equals(oder_data)) {
                oder_where = i + 1;
            }
            if (type[i][1].equals("int")) {
                part_len += 4;
                order_len[i] = 4;
            }
            if (type[i][1].equals("decimal")) {
                part_len += 8;
                order_len[i] = 8;
            }
            if (type[i][1].equals("varchar")) {
                part_len += 3 * Integer.parseInt(type[i][2]);
                order_len[i] = 3 * Integer.parseInt(type[i][2]);
            }
        }
        if (operate.equals("=")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int == Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou == Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.equals(chose_num))
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                        total_len-=part_len;
                        line++;
                }
            }


            Object[] byobjects=new Object[actually_chose];
            for(int i=0;i<actually_chose;i++)
            {
                byobjects[i]=objects[i][oder_where-1];
            }
            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }
            Objects=sortedDisplay(Objects,byobjects,flagAscend);


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals(">")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int > Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou > Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)>0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }


            Object[] byobjects=new Object[actually_chose];
            for(int i=0;i<actually_chose;i++)
            {
                byobjects[i]=objects[i][oder_where-1];
            }
            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }
            Objects=sortedDisplay(Objects,byobjects,flagAscend);


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals(">=")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int >= Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou >= Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)>=0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }


            Object[] byobjects=new Object[actually_chose];
            for(int i=0;i<actually_chose;i++)
            {
                byobjects[i]=objects[i][oder_where-1];
            }
            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }
            Objects=sortedDisplay(Objects,byobjects,flagAscend);


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals("<")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int < Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou < Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)<0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }


            Object[] byobjects=new Object[actually_chose];
            for(int i=0;i<actually_chose;i++)
            {
                byobjects[i]=objects[i][oder_where-1];
            }
            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }
            Objects=sortedDisplay(Objects,byobjects,flagAscend);


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals("<=")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int <= Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou <= Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)<=0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }


            Object[] byobjects=new Object[actually_chose];
            for(int i=0;i<actually_chose;i++)
            {
                byobjects[i]=objects[i][oder_where-1];
            }
            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }
            Objects=sortedDisplay(Objects,byobjects,flagAscend);


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals("<>")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int != Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou != Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)!=0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }


            Object[] byobjects=new Object[actually_chose];
            for(int i=0;i<actually_chose;i++)
            {
                byobjects[i]=objects[i][oder_where-1];
            }
            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }
            Objects=sortedDisplay(Objects,byobjects,flagAscend);


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        randomAccessFile.close();
    }


    public static void selete(String filename,String[] date,String chose_data,String operate,String chose_num)throws IOException {


        String[][] type = getstruct(filename);
        int []date_at=new int[date.length];
        for(int i=0;i<date.length;i++)
        {
            for(int j=0;j<3;j++)
            {

                if(date[i].equals(type[j][0]))
                {
                    date_at[i]=j+1;
                    break;
                }
            }
        }



        String path_data = readDbHome_data() + "/" + filename + ".midb";
        String path_struct = readDbHome_struct() + "/" + filename + ".li";
        RandomAccessFile randomAccessFile = new RandomAccessFile(path_data, "rw");
        int part_len = 0;
        int[] order_len = new int[4];   //按顺序存三个数据每个的长度
        int where = 0;
        String struct = "";
        for (int i = 0; i < 3; i++) {
            if (type[i][0].equals(chose_data)) {
                where = i + 1;
                struct = type[i][1];
            }

            if (type[i][1].equals("int")) {
                part_len += 4;
                order_len[i] = 4;
            }
            if (type[i][1].equals("decimal")) {
                part_len += 8;
                order_len[i] = 8;
            }
            if (type[i][1].equals("varchar")) {
                part_len += 3 * Integer.parseInt(type[i][2]);
                order_len[i] = 3 * Integer.parseInt(type[i][2]);
            }
        }
        if (operate.equals("=")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int == Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou == Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.equals(chose_num))
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }



            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }

            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals(">")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int > Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou > Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)>0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }



            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }



            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals(">=")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int >= Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou >= Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)>=0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }



            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals("<")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int < Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou < Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)<0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }



            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals("<=")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int <= Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou <= Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)<=0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }



            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }


            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        if (operate.equals("<>")) {
            int count = 0;
            long total_len = randomAccessFile.length();
            int line = 0;
            int line_start = 0;       //记录每一行的开头位置
            int total_num = (int) total_len / part_len;
            Object[][] objects = new Object[total_num][3];
            int actually_chose = 0;
            while (total_len > 0) {
                int chosen = 0;
                line_start = line * part_len;
                for (int i = 0; i < where - 1; i++) {

                    randomAccessFile.skipBytes(order_len[i]);
                }
                byte[] byte_judge = new byte[order_len[where - 1]];
                randomAccessFile.read(byte_judge);
                if (struct.equals("int")) {
                    int Int = Transformer.bytesToInt(byte_judge);
                    if (Int != Integer.parseInt(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("decimal")) {
                    double Dou = Transformer.bytesToDouble(byte_judge);
                    if (Dou != Double.parseDouble(chose_num))
                        chosen = 1;
                }
                else if (struct.equals("varchar")) {
                    String str = new String(byte_judge, StandardCharsets.UTF_8);
                    int w = 0;
                    for (w = 0; w < order_len[where - 1]; w++) {
                        if (str.charAt(w) == '0')
                            break;
                    }
                    str = str.substring(0, w);
                    if (str.compareTo(chose_num)!=0)
                        chosen = 1;
                }
                for(int i=where;i<3;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                }

                if (chosen == 1) {
                    randomAccessFile.seek(line_start);
                    for (int i = 0; i < 3; i++) {
                        byte[] byte_in = new byte[order_len[i]];
                        randomAccessFile.read(byte_in);
                        if (type[i][1].equals( "int")) {
                            int Int = Transformer.bytesToInt(byte_in);
                            objects[actually_chose][i] = Int;
                        }
                        if (type[i][1].equals("decimal")) {
                            double Dou = Transformer.bytesToDouble(byte_in);
                            objects[actually_chose][i] = Dou;
                        }
                        if (type[i][1].equals("varchar")) {
                            String str = new String(byte_in, StandardCharsets.UTF_8);
                            int w = 0;
                            for (w = 0; w < order_len[where - 1]; w++) {
                                if (str.charAt(w) == '0')
                                    break;
                            }
                            str = str.substring(0, w);
                            objects[actually_chose][i] = str;
                        }
                    }
                    actually_chose++;
                    line++;
                    total_len -= part_len;
                }
                else {
                    total_len-=part_len;
                    line++;
                }
            }



            Object[][] Objects=new Object[actually_chose][3];
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<3;j++)
                {
                    Objects[i][j]=objects[i][j];
                }
            }



            for (int i=0;i<date.length-1;i++)
                System.out.print(date[i]+",");
            System.out.println(date[date.length-1]);
            for(int i=0;i<actually_chose;i++)
            {
                for (int j=0;j<date.length-1;j++)
                    System.out.print(Objects[i][date_at[j]-1]+",");
                System.out.println(Objects[i][date_at[date.length-1]-1]);
            }
            System.out.println("共有"+actually_chose+"条记录");

        }
        randomAccessFile.close();
    }



    public static void selete(String filename)
    {
        String path_data=readDbHome_data()+"/"+filename+".midb";
        String path_struct=readDbHome_struct()+"/"+filename+".li";
        String type[][]=getstruct(filename);
        RandomAccessFile raf2 = null;
        try {
            raf2 = new RandomAccessFile(path_data,"rw");
            byte[] buf2 = new byte[1024];
            long len = 0;
            len=raf2.length();
            int total=0;
            for(int k=0;k<2;k++)
            {
                System.out.print(type[k][0]+",");
            }
            System.out.println(type[2][0]);
                while(len>0) {

                    for (int i = 0; i < 3; i++) {
                        if (type[i][1].equals("int")) {
                            byte[] byte222 = new byte[4];
                            len-=4;
                            raf2.read(byte222);
                            int Int = Transformer.bytesToInt(byte222);
                            System.out.print(Int);
                            if (i == 2) {
                                System.out.print("\n");
                                total++;
                            } else {
                                System.out.print(",");
                            }
                        }
                        if (type[i][1].equals("decimal")) {
                            byte[] byte222 = new byte[8];
                            len-=8;
                            raf2.read(byte222);

                            Double dou = Transformer.bytesToDouble(byte222);
                            System.out.print(dou);
                            if (i == 2) {
                                System.out.print("\n");
                                total++;
                            } else {
                                System.out.print(",");
                            }
                        }
                        if (type[i][1].equals("varchar")) {
                            int Len = Integer.parseInt(type[i][2]) * 3;
                            len-=Len;
                            byte[] byte222 = new byte[Len];
                            raf2.read(byte222);
                            String Out = new String(byte222, StandardCharsets.UTF_8);
                            int w=0;
                            for(w=0;w<Len;w++)
                            {
                                if(Out.charAt(w)=='0')
                                    break;
                            }
                            Out=Out.substring(0,w);
                            System.out.print(Out);
                            if (i == 2) {
                                System.out.print("\n");
                                total++;
                            } else {
                                System.out.print(",");
                            }
                        }

                    }

                }

            System.out.println("共输出"+total+"行数据");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            raf2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String filename,String dataname,String operate,String num) throws  IOException
    {

        String path_data=readDbHome_data()+"/"+filename+".midb";
        String path_struct=readDbHome_struct()+"/"+filename+".li";
        RandomAccessFile randomAccessFile=new RandomAccessFile(path_data,"rw");

        String[][] type=getstruct(filename);
        int where=0;
        String struct="";
        int part_len=0;
        int[] order_len=new int[4];   //按顺序存三个数据每个的长度

        for(int i=0;i<3;i++)
        {
            if(type[i][0].equals(dataname))
            {
                where=i+1;
                struct=type[i][1];
            }
            if(type[i][1].equals("int"))
            {
                part_len+=4;
                order_len[i]=4;
            }
            if(type[i][1].equals("decimal"))
            {
                part_len+=8;
                order_len[i]=8;
            }
            if(type[i][1].equals("varchar"))
            {
                part_len+=3*Integer.parseInt(type[i][2]);
                order_len[i]=3*Integer.parseInt(type[i][2]);
            }
        }
        if(operate.equals(">"))
        {

            int count=0;
            long total_len=randomAccessFile.length();
            int line=0;
            int line_start=0;       //记录每一行的开头位置
            int total_num=(int)total_len/part_len;
            while(total_len>0)
            {
                line_start=line*part_len;
                int Delete=0;
                for(int i=0;i<where-1;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);
                    System.out.println("指针1："+randomAccessFile.getFilePointer());
                }
                if(struct.equals("int"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    int Int=Transformer.bytesToInt(bytes);
                    System.out.println("指针2："+randomAccessFile.getFilePointer());
                    if(Int>Integer.parseInt(num))
                        Delete=1;
                }else if(struct.equals("decimal"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    Double Dou=Transformer.bytesToDouble(bytes);
                    if(Dou>Double.parseDouble(num))
                        Delete=1;
                }

                if(Delete==0)
                {
                    for(int i=where;i<3;i++)
                    randomAccessFile.skipBytes(order_len[i]);
                    total_len-=part_len;
                    line++;
                }else
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    byte[] bytes=new byte[(total_num-line-1)*part_len];
                    randomAccessFile.read(bytes);
                    randomAccessFile.seek(line_start);
                    randomAccessFile.write(bytes);
                   randomAccessFile.setLength(randomAccessFile.length()-part_len);
                    randomAccessFile.seek(line_start);
                   total_len-=part_len;
                   total_num--;
                   count++;
                }
            }
            System.out.println("共删除"+count+"条记录");
        }
        if(operate.equals("<"))
        {
            int count=0;
            long total_len=randomAccessFile.length();
            int line=0;
            int line_start=0;       //记录每一行的开头位置
            int total_num=(int)total_len/part_len;
            while(total_len>0)
            {
                line_start=line*part_len;
                int Delete=0;
                for(int i=0;i<where-1;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);

                }
                if(struct.equals("int"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    int Int=Transformer.bytesToInt(bytes);

                    if(Int<Integer.parseInt(num))
                        Delete=1;
                }else if(struct.equals("decimal"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    Double Dou=Transformer.bytesToDouble(bytes);
                    if(Dou<Double.parseDouble(num))
                        Delete=1;
                }

                if(Delete==0)
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    total_len-=part_len;
                    line++;
                }else
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    byte[] bytes=new byte[(total_num-line-1)*part_len];
                    randomAccessFile.read(bytes);
                    randomAccessFile.seek(line_start);
                    randomAccessFile.write(bytes);
                    randomAccessFile.setLength(randomAccessFile.length()-part_len);
                    randomAccessFile.seek(line_start);
                    total_len-=part_len;
                    total_num--;
                    count++;
                }
            }
            System.out.println("共删除"+count+"条记录");
        }
        if(operate.equals("="))
        {
            int count=0;
            long total_len=randomAccessFile.length();
            int line=0;
            int line_start=0;       //记录每一行的开头位置
            int total_num=(int)total_len/part_len;
            while(total_len>0)
            {
                line_start=line*part_len;
                int Delete=0;
                for(int i=0;i<where-1;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);

                }
                if(struct.equals("int"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    int Int=Transformer.bytesToInt(bytes);

                    if(Int==Integer.parseInt(num))
                        Delete=1;
                }else if(struct.equals("decimal"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    Double Dou=Transformer.bytesToDouble(bytes);
                    if(Dou==Double.parseDouble(num))
                        Delete=1;
                }

                if(Delete==0)
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    total_len-=part_len;
                    line++;
                }else
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    byte[] bytes=new byte[(total_num-line-1)*part_len];
                    randomAccessFile.read(bytes);
                    randomAccessFile.seek(line_start);
                    randomAccessFile.write(bytes);
                    randomAccessFile.setLength(randomAccessFile.length()-part_len);
                    randomAccessFile.seek(line_start);
                    total_len-=part_len;
                    total_num--;
                    count++;
                }
            }
            System.out.println("共删除"+count+"条记录");
        }
        if(operate.equals("<="))
        {
            int count=0;
            long total_len=randomAccessFile.length();
            int line=0;
            int line_start=0;       //记录每一行的开头位置
            int total_num=(int)total_len/part_len;
            while(total_len>0)
            {
                line_start=line*part_len;
                int Delete=0;
                for(int i=0;i<where-1;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);

                }
                if(struct.equals("int"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    int Int=Transformer.bytesToInt(bytes);

                    if(Int<=Integer.parseInt(num))
                        Delete=1;
                }else if(struct.equals("decimal"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    Double Dou=Transformer.bytesToDouble(bytes);
                    if(Dou<=Double.parseDouble(num))
                        Delete=1;
                }

                if(Delete==0)
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    total_len-=part_len;
                    line++;
                }else
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    byte[] bytes=new byte[(total_num-line-1)*part_len];
                    randomAccessFile.read(bytes);
                    randomAccessFile.seek(line_start);
                    randomAccessFile.write(bytes);
                    randomAccessFile.setLength(randomAccessFile.length()-part_len);
                    randomAccessFile.seek(line_start);
                    total_len-=part_len;
                    total_num--;
                    count++;
                }
            }
            System.out.println("共删除"+count+"条记录");
        }
        if(operate.equals(">="))
        {
            int count=0;
            long total_len=randomAccessFile.length();
            int line=0;
            int line_start=0;       //记录每一行的开头位置
            int total_num=(int)total_len/part_len;
            while(total_len>0)
            {
                line_start=line*part_len;
                int Delete=0;
                for(int i=0;i<where-1;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);

                }
                if(struct.equals("int"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    int Int=Transformer.bytesToInt(bytes);

                    if(Int>=Integer.parseInt(num))
                        Delete=1;
                }else if(struct.equals("decimal"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    Double Dou=Transformer.bytesToDouble(bytes);
                    if(Dou>=Double.parseDouble(num))
                        Delete=1;
                }

                if(Delete==0)
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    total_len-=part_len;
                    line++;
                }else
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    byte[] bytes=new byte[(total_num-line-1)*part_len];
                    randomAccessFile.read(bytes);
                    randomAccessFile.seek(line_start);
                    randomAccessFile.write(bytes);
                    randomAccessFile.setLength(randomAccessFile.length()-part_len);
                    randomAccessFile.seek(line_start);
                    total_len-=part_len;
                    total_num--;
                    count++;
                }
            }
            System.out.println("共删除"+count+"条记录");
        }
        if(operate.equals("<>"))
        {
            int count=0;
            long total_len=randomAccessFile.length();
            int line=0;
            int line_start=0;       //记录每一行的开头位置
            int total_num=(int)total_len/part_len;
            while(total_len>0)
            {
                line_start=line*part_len;
                int Delete=0;
                for(int i=0;i<where-1;i++)
                {
                    randomAccessFile.skipBytes(order_len[i]);

                }
                if(struct.equals("int"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    int Int=Transformer.bytesToInt(bytes);

                    if(Int!=Integer.parseInt(num))
                        Delete=1;
                }else if(struct.equals("decimal"))
                {
                    byte[] bytes=new byte[order_len[where-1]];
                    randomAccessFile.read(bytes);
                    Double Dou=Transformer.bytesToDouble(bytes);
                    if(Dou!=Double.parseDouble(num))
                        Delete=1;
                }

                if(Delete==0)
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    total_len-=part_len;
                    line++;
                }else
                {
                    for(int i=where;i<3;i++)
                        randomAccessFile.skipBytes(order_len[i]);

                    byte[] bytes=new byte[(total_num-line-1)*part_len];
                    randomAccessFile.read(bytes);
                    randomAccessFile.seek(line_start);
                    randomAccessFile.write(bytes);
                    randomAccessFile.setLength(randomAccessFile.length()-part_len);
                    randomAccessFile.seek(line_start);
                    total_len-=part_len;
                    total_num--;
                    count++;
                }
            }
            System.out.println("共删除"+count+"条记录");
        }
        randomAccessFile.close();
    }
    public static void update(String filename,String change_date,String change_num,String chose_date,String operate,String chose_num)throws  IOException
    {
        String path_data=readDbHome_data()+"/"+filename+".midb";
        String path_struct=readDbHome_struct()+"/"+filename+".li";
        RandomAccessFile randomAccessFile=new RandomAccessFile(path_data,"rw");
        String[][] type=getstruct(filename);
        int change_at=0;
        int chose_at=0;
        String change_struct="";
        String chose_struct="";

        int part_len=0;
        int[] order_len=new int[4];   //按顺序存三个数据每个的长度
        for(int i=0;i<3;i++)
        {
            if(type[i][0].equals(change_date))
            {
                change_at=i+1;
                change_struct=type[i][1];
            }
            if(type[i][0].equals(chose_date))
            {
                chose_at=i+1;
                chose_struct=type[i][1];
            }
            if(type[i][1].equals("int"))
            {
                part_len+=4;
                order_len[i]=4;
            }
            if(type[i][1].equals("decimal"))
            {
                part_len+=8;
                order_len[i]=8;
            }
            if(type[i][1].equals("varchar"))
            {
                part_len+=3*Integer.parseInt(type[i][2]);
                order_len[i]=3*Integer.parseInt(type[i][2]);
            }
        }
        int flag=1;
        if(type[change_at-1][1].equals("varchar"))
        {
            if(change_num.charAt(0)!='\''||change_num.charAt(change_num.length()-1)!='\'')
            {
                flag=0;
                System.out.println(change_date+"是varchar类型，但未用单引号包裹");
            }else
            {
                change_num=change_num.substring(1,change_num.length()-1);

            }
        }
        if(type[chose_at-1][1].equals("varchar"))
        {
            if(chose_num.charAt(0)!='\''||chose_num.charAt(chose_num.length()-1)!='\'')
            {
                flag=0;
                System.out.println(chose_date+"是varchar类型，但未用单引号包裹"+chose_num.charAt(0)+chose_num.charAt(change_num.length()-1));
            }else
            {
                chose_num=chose_num.substring(1,chose_num.length()-1);
            }
        }
        if(flag==1) {
            if (operate.equals("=")) {
                int count = 0;
                long total_len = randomAccessFile.length();
                long LEN = total_len;
                int line = 0;
                int line_start = 0;       //记录每一行的开头位置
                int total_num = (int) total_len / part_len;
                while (total_len > 0) {
                    line_start = line * part_len;
                    int Update = 0;
                    for (int i = 0; i < chose_at - 1; i++) {
                        randomAccessFile.skipBytes(order_len[i]);
                    }
                    if (chose_struct.equals("int")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        int Int = Transformer.bytesToInt(bytes);

                        if (Int == Integer.parseInt(chose_num))
                            Update = 1;
                    } else if (chose_struct.equals("decimal")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        Double Dou = Transformer.bytesToDouble(bytes);
                        if (Dou == Double.parseDouble(chose_num))
                            Update = 1;


                    } else if (chose_struct.equals("varchar")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        String str = new String(bytes, StandardCharsets.UTF_8);
                        int w = 0;
                        for (w = 0; w < order_len[chose_at - 1]; w++) {
                            if (str.charAt(w) == '0')
                                break;
                        }
                        str = str.substring(0, w);
                        if (str.equals(chose_num))
                            Update = 1;
                    }

                    if (Update == 0) {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        total_len -= part_len;
                        line++;
                    } else {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);

                        byte[] bytes = new byte[(total_num - line - 1) * part_len];
                        randomAccessFile.read(bytes);
                        randomAccessFile.seek(line_start);

                        for (int i = 0; i < change_at; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        int len = 0;
                        for (int i = change_at; i < 3; i++) {
                            len += order_len[i];
                        }
                        byte[] byte_next = null;
                        if (len != 0) {
                            byte_next = new byte[len];
                            randomAccessFile.read(byte_next);
                        }
                        randomAccessFile.seek(line_start);
                        if (type[change_at - 1][1].equals("int")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[4];
                            bytes_1 = Transformer.intToBytes(Integer.parseInt(change_num));
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("decimal")) {
                            double Dou = 0.0;
                            int j;
                            for (j = 0; j < change_num.length(); j++) {
                                if (change_num.charAt(j) == '.') {
                                    break;
                                }
                            }
                            if (j == change_num.length()) {
                                j = Integer.parseInt(change_num);
                                BigDecimal decimal = new BigDecimal(j);
                                Dou = decimal.setScale(1).doubleValue();
                            } else {
                                Dou = Double.parseDouble(change_num);
                            }

                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[8];

                            bytes_1 = Transformer.doubleToBytes(Dou);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("varchar")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            while (true) {
                                if (change_num.getBytes(StandardCharsets.UTF_8).length < order_len[change_at - 1])
                                    change_num += "0";
                                else break;
                            }
                            byte[] bytes_1 = new byte[order_len[change_at - 1]];
                            bytes_1 = change_num.getBytes(StandardCharsets.UTF_8);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }

                    }
                }
                System.out.println("共更新" + count + "条记录");

            }
            if (operate.equals(">")) {
                int count = 0;
                long total_len = randomAccessFile.length();
                long LEN = total_len;
                int line = 0;
                int line_start = 0;       //记录每一行的开头位置
                int total_num = (int) total_len / part_len;
                while (total_len > 0) {
                    line_start = line * part_len;
                    int Update = 0;
                    for (int i = 0; i < chose_at - 1; i++) {
                        randomAccessFile.skipBytes(order_len[i]);
                    }
                    if (chose_struct.equals("int")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        int Int = Transformer.bytesToInt(bytes);

                        if (Int > Integer.parseInt(chose_num))
                            Update = 1;
                    } else if (chose_struct.equals("decimal")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        Double Dou = Transformer.bytesToDouble(bytes);
                        if (Dou > Double.parseDouble(chose_num))
                            Update = 1;


                    } else if (chose_struct.equals("varchar")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        String str = new String(bytes, StandardCharsets.UTF_8);
                        int w = 0;
                        for (w = 0; w < order_len[chose_at - 1]; w++) {
                            if (str.charAt(w) == '0')
                                break;
                        }
                        str = str.substring(0, w);
                        if (str.compareTo(chose_num) > 0)
                            Update = 1;
                    }

                    if (Update == 0) {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        total_len -= part_len;
                        line++;
                    } else {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);

                        byte[] bytes = new byte[(total_num - line - 1) * part_len];
                        randomAccessFile.read(bytes);
                        randomAccessFile.seek(line_start);

                        for (int i = 0; i < change_at; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        int len = 0;
                        for (int i = change_at; i < 3; i++) {
                            len += order_len[i];
                        }
                        byte[] byte_next = null;
                        if (len != 0) {
                            byte_next = new byte[len];
                            randomAccessFile.read(byte_next);
                        }
                        randomAccessFile.seek(line_start);
                        if (type[change_at - 1][1].equals("int")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[4];
                            bytes_1 = Transformer.intToBytes(Integer.parseInt(change_num));
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("decimal")) {
                            double Dou = 0.0;
                            int j;
                            for (j = 0; j < change_num.length(); j++) {
                                if (change_num.charAt(j) == '.') {
                                    break;
                                }
                            }
                            if (j == change_num.length()) {
                                j = Integer.parseInt(change_num);
                                BigDecimal decimal = new BigDecimal(j);
                                Dou = decimal.setScale(1).doubleValue();
                            } else {
                                Dou = Double.parseDouble(change_num);
                            }

                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[8];

                            bytes_1 = Transformer.doubleToBytes(Dou);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("varchar")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            while (true) {
                                if (change_num.getBytes(StandardCharsets.UTF_8).length < order_len[change_at - 1])
                                    change_num += "0";
                                else break;
                            }
                            byte[] bytes_1 = new byte[order_len[change_at - 1]];
                            bytes_1 = change_num.getBytes(StandardCharsets.UTF_8);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }

                    }
                }
                System.out.println("共更新" + count + "条记录");

            }
            if (operate.equals(">=")) {
                int count = 0;
                long total_len = randomAccessFile.length();
                long LEN = total_len;
                int line = 0;
                int line_start = 0;       //记录每一行的开头位置
                int total_num = (int) total_len / part_len;
                while (total_len > 0) {
                    line_start = line * part_len;
                    int Update = 0;
                    for (int i = 0; i < chose_at - 1; i++) {
                        randomAccessFile.skipBytes(order_len[i]);
                    }
                    if (chose_struct.equals("int")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        int Int = Transformer.bytesToInt(bytes);

                        if (Int >= Integer.parseInt(chose_num))
                            Update = 1;
                    } else if (chose_struct.equals("decimal")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        Double Dou = Transformer.bytesToDouble(bytes);
                        if (Dou >= Double.parseDouble(chose_num))
                            Update = 1;


                    } else if (chose_struct.equals("varchar")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        String str = new String(bytes, StandardCharsets.UTF_8);
                        int w = 0;
                        for (w = 0; w < order_len[chose_at - 1]; w++) {
                            if (str.charAt(w) == '0')
                                break;
                        }
                        str = str.substring(0, w);
                        if (str.compareTo(chose_num) >= 0)
                            Update = 1;
                    }

                    if (Update == 0) {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        total_len -= part_len;
                        line++;
                    } else {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);

                        byte[] bytes = new byte[(total_num - line - 1) * part_len];
                        randomAccessFile.read(bytes);
                        randomAccessFile.seek(line_start);

                        for (int i = 0; i < change_at; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        int len = 0;
                        for (int i = change_at; i < 3; i++) {
                            len += order_len[i];
                        }
                        byte[] byte_next = null;
                        if (len != 0) {
                            byte_next = new byte[len];
                            randomAccessFile.read(byte_next);
                        }
                        randomAccessFile.seek(line_start);
                        if (type[change_at - 1][1].equals("int")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[4];
                            bytes_1 = Transformer.intToBytes(Integer.parseInt(change_num));
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("decimal")) {
                            double Dou = 0.0;
                            int j;
                            for (j = 0; j < change_num.length(); j++) {
                                if (change_num.charAt(j) == '.') {
                                    break;
                                }
                            }
                            if (j == change_num.length()) {
                                j = Integer.parseInt(change_num);
                                BigDecimal decimal = new BigDecimal(j);
                                Dou = decimal.setScale(1).doubleValue();
                            } else {
                                Dou = Double.parseDouble(change_num);
                            }

                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[8];

                            bytes_1 = Transformer.doubleToBytes(Dou);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("varchar")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            while (true) {
                                if (change_num.getBytes(StandardCharsets.UTF_8).length < order_len[change_at - 1])
                                    change_num += "0";
                                else break;
                            }
                            byte[] bytes_1 = new byte[order_len[change_at - 1]];
                            bytes_1 = change_num.getBytes(StandardCharsets.UTF_8);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }

                    }
                }
                System.out.println("共更新" + count + "条记录");

            }
            if (operate.equals("<")) {
                int count = 0;
                long total_len = randomAccessFile.length();
                long LEN = total_len;
                int line = 0;
                int line_start = 0;       //记录每一行的开头位置
                int total_num = (int) total_len / part_len;
                while (total_len > 0) {
                    line_start = line * part_len;
                    int Update = 0;
                    for (int i = 0; i < chose_at - 1; i++) {
                        randomAccessFile.skipBytes(order_len[i]);
                    }
                    if (chose_struct.equals("int")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        int Int = Transformer.bytesToInt(bytes);

                        if (Int < Integer.parseInt(chose_num))
                            Update = 1;
                    } else if (chose_struct.equals("decimal")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        Double Dou = Transformer.bytesToDouble(bytes);
                        if (Dou < Double.parseDouble(chose_num))
                            Update = 1;


                    } else if (chose_struct.equals("varchar")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        String str = new String(bytes, StandardCharsets.UTF_8);
                        int w = 0;
                        for (w = 0; w < order_len[chose_at - 1]; w++) {
                            if (str.charAt(w) == '0')
                                break;
                        }
                        str = str.substring(0, w);
                        if (str.compareTo(chose_num) < 0)
                            Update = 1;
                    }

                    if (Update == 0) {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        total_len -= part_len;
                        line++;
                    } else {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);

                        byte[] bytes = new byte[(total_num - line - 1) * part_len];
                        randomAccessFile.read(bytes);
                        randomAccessFile.seek(line_start);

                        for (int i = 0; i < change_at; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        int len = 0;
                        for (int i = change_at; i < 3; i++) {
                            len += order_len[i];
                        }
                        byte[] byte_next = null;
                        if (len != 0) {
                            byte_next = new byte[len];
                            randomAccessFile.read(byte_next);
                        }
                        randomAccessFile.seek(line_start);
                        if (type[change_at - 1][1].equals("int")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[4];
                            bytes_1 = Transformer.intToBytes(Integer.parseInt(change_num));
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("decimal")) {
                            double Dou = 0.0;
                            int j;
                            for (j = 0; j < change_num.length(); j++) {
                                if (change_num.charAt(j) == '.') {
                                    break;
                                }
                            }
                            if (j == change_num.length()) {
                                j = Integer.parseInt(change_num);
                                BigDecimal decimal = new BigDecimal(j);
                                Dou = decimal.setScale(1).doubleValue();
                            } else {
                                Dou = Double.parseDouble(change_num);
                            }

                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[8];

                            bytes_1 = Transformer.doubleToBytes(Dou);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("varchar")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            while (true) {
                                if (change_num.getBytes(StandardCharsets.UTF_8).length < order_len[change_at - 1])
                                    change_num += "0";
                                else break;
                            }
                            byte[] bytes_1 = new byte[order_len[change_at - 1]];
                            bytes_1 = change_num.getBytes(StandardCharsets.UTF_8);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }

                    }
                }
                System.out.println("共更新" + count + "条记录");

            }
            if (operate.equals("<=")) {
                int count = 0;
                long total_len = randomAccessFile.length();
                long LEN = total_len;
                int line = 0;
                int line_start = 0;       //记录每一行的开头位置
                int total_num = (int) total_len / part_len;
                while (total_len > 0) {
                    line_start = line * part_len;
                    int Update = 0;
                    for (int i = 0; i < chose_at - 1; i++) {
                        randomAccessFile.skipBytes(order_len[i]);
                    }
                    if (chose_struct.equals("int")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        int Int = Transformer.bytesToInt(bytes);

                        if (Int <= Integer.parseInt(chose_num))
                            Update = 1;
                    } else if (chose_struct.equals("decimal")) {
                        byte[] bytes = new byte[8];
                        System.out.println("change"+randomAccessFile.getFilePointer());
                        randomAccessFile.read(bytes);

                        Double Dou = Transformer.bytesToDouble(bytes);
                        System.out.println("change"+randomAccessFile.getFilePointer()+" "+Dou);
                        if (Dou <= Double.parseDouble(chose_num))
                            Update = 1;


                    } else if (chose_struct.equals("varchar")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        String str = new String(bytes, StandardCharsets.UTF_8);
                        int w = 0;
                        for (w = 0; w < order_len[chose_at - 1]; w++) {
                            if (str.charAt(w) == '0')
                                break;
                        }
                        str = str.substring(0, w);
                        if (str.compareTo(chose_num) <= 0)
                            Update = 1;
                    }

                    if (Update == 0) {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        total_len -= part_len;
                        line++;
                    }
                    else {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);

                        byte[] bytes = new byte[(total_num - line - 1) * part_len];
                        randomAccessFile.read(bytes);
                        randomAccessFile.seek(line_start);

                        for (int i = 0; i < change_at; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        int len = 0;
                        for (int i = change_at; i < 3; i++) {
                            len += order_len[i];
                        }
                        byte[] byte_next = null;
                        if (len != 0) {
                            byte_next = new byte[len];
                            randomAccessFile.read(byte_next);
                        }
                        randomAccessFile.seek(line_start);
                        if (type[change_at - 1][1].equals("int")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[4];

                            bytes_1 = Transformer.intToBytes(Integer.parseInt(change_num));
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }
                        else if (type[change_at - 1][1].equals("decimal")) {
                            double Dou = 0.0;
                            int j;
                            for (j = 0; j < change_num.length(); j++) {
                                if (change_num.charAt(j) == '.') {
                                    break;
                                }
                            }
                            if (j == change_num.length()) {
                                j = Integer.parseInt(change_num);
                                BigDecimal decimal = new BigDecimal(j);
                                Dou = decimal.setScale(1).doubleValue();
                            } else {
                                Dou = Double.parseDouble(change_num);
                            }

                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[8];

                            bytes_1 = Transformer.doubleToBytes(Dou);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }
                        else if (type[change_at - 1][1].equals("varchar")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            while (true) {
                                if (change_num.getBytes(StandardCharsets.UTF_8).length < order_len[change_at - 1])
                                    change_num += "0";
                                else break;
                            }
                            byte[] bytes_1 = new byte[order_len[change_at - 1]];
                            bytes_1 = change_num.getBytes(StandardCharsets.UTF_8);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }

                    }
                }
                System.out.println("共更新" + count + "条记录");

            }
            if (operate.equals("<>")) {
                int count = 0;
                long total_len = randomAccessFile.length();
                long LEN = total_len;
                int line = 0;
                int line_start = 0;       //记录每一行的开头位置
                int total_num = (int) total_len / part_len;
                while (total_len > 0) {
                    line_start = line * part_len;
                    int Update = 0;
                    for (int i = 0; i < chose_at - 1; i++) {
                        randomAccessFile.skipBytes(order_len[i]);
                    }
                    if (chose_struct.equals("int")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        int Int = Transformer.bytesToInt(bytes);

                        if (Int != Integer.parseInt(chose_num))
                            Update = 1;
                    } else if (chose_struct.equals("decimal")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        Double Dou = Transformer.bytesToDouble(bytes);
                        if (Dou != Double.parseDouble(chose_num))
                            Update = 1;


                    } else if (chose_struct.equals("varchar")) {
                        byte[] bytes = new byte[order_len[chose_at - 1]];
                        randomAccessFile.read(bytes);
                        String str = new String(bytes, StandardCharsets.UTF_8);
                        int w = 0;
                        for (w = 0; w < order_len[chose_at - 1]; w++) {
                            if (str.charAt(w) == '0')
                                break;
                        }
                        str = str.substring(0, w);
                        if (str.compareTo(chose_num) != 0)
                            Update = 1;
                    }

                    if (Update == 0) {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        total_len -= part_len;
                        line++;
                    } else {
                        for (int i = chose_at; i < 3; i++)
                            randomAccessFile.skipBytes(order_len[i]);

                        byte[] bytes = new byte[(total_num - line - 1) * part_len];
                        randomAccessFile.read(bytes);
                        randomAccessFile.seek(line_start);

                        for (int i = 0; i < change_at; i++)
                            randomAccessFile.skipBytes(order_len[i]);
                        int len = 0;
                        for (int i = change_at; i < 3; i++) {
                            len += order_len[i];
                        }
                        byte[] byte_next = null;
                        if (len != 0) {
                            byte_next = new byte[len];
                            randomAccessFile.read(byte_next);
                        }
                        randomAccessFile.seek(line_start);
                        if (type[change_at - 1][1].equals("int")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[4];
                            bytes_1 = Transformer.intToBytes(Integer.parseInt(change_num));
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("decimal")) {
                            double Dou = 0.0;
                            int j;
                            for (j = 0; j < change_num.length(); j++) {
                                if (change_num.charAt(j) == '.') {
                                    break;
                                }
                            }
                            if (j == change_num.length()) {
                                j = Integer.parseInt(change_num);
                                BigDecimal decimal = new BigDecimal(j);
                                Dou = decimal.setScale(1).doubleValue();
                            } else {
                                Dou = Double.parseDouble(change_num);
                            }

                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            byte[] bytes_1 = new byte[8];

                            bytes_1 = Transformer.doubleToBytes(Dou);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        } else if (type[change_at - 1][1].equals("varchar")) {
                            for (int i = 0; i < change_at - 1; i++)
                                randomAccessFile.skipBytes(order_len[i]);
                            while (true) {
                                if (change_num.getBytes(StandardCharsets.UTF_8).length < order_len[change_at - 1])
                                    change_num += "0";
                                else break;
                            }
                            byte[] bytes_1 = new byte[order_len[change_at - 1]];
                            bytes_1 = change_num.getBytes(StandardCharsets.UTF_8);
                            randomAccessFile.write(bytes_1);
                            if (len != 0) {
                                randomAccessFile.write(byte_next);
                                randomAccessFile.write(bytes);
                            } else {
                                randomAccessFile.write(bytes);
                            }
                            randomAccessFile.seek(line_start + part_len);
                            total_len -= part_len;
                            randomAccessFile.setLength(LEN);
                            count++;
                            line++;
                        }

                    }
                }
                System.out.println("共更新" + count + "条记录");

            }
        }
        randomAccessFile.close();
    }

    public static String[][] getstruct(String filename)
    {
        String path_struct=readDbHome_struct()+"/"+filename+".li";
        RandomAccessFile raf2 = null;
        String words="";
        try {
            raf2 = new RandomAccessFile(path_struct,"rw");
            byte[] buf2 = new byte[1024];
            int len = 0;

            int k;
            while((len = raf2.read(buf2))>0) {
                byte[] buf22=new byte[len];
                for(int i=0;i<len;i++) {

                    buf22[i] = buf2[i];
                }
                words=new String(buf22,StandardCharsets.UTF_8);
            }
            raf2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String count[]=words.split("\n");
        String colA[]=count[0].split(",");
        String colB[]=count[1].split(",");
        String colC[]=count[2].split(",");
        String[][] type=new String[3][3];
        String[] v=new String[3];
        for(int i=0;i<3;i++)
        {

            type[0][i]=colA[i];
        }
        for(int i=0;i<3;i++)
        {
            type[1][i]=colB[i];
        }
        for(int i=0;i<3;i++)
        {
            type[2][i]=colC[i];
        }
        return type;
    }



    public void sqlResult(String sql) throws MyException
    {
        if(sql.trim().startsWith("create"))
        {

        }
    }
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("([+\\-]?[0-9]+[.]?[\\d]*)");
        return pattern.matcher(str).matches();
    }

    public static Object[][] sortedDisplay(Object[][] objects, Object[] byObjects, boolean flagAscend) {
        // 如果没有排序要求，则直接返回获取的内容：
        if (byObjects == null)
            return objects;
        // 如果一行都没有选中，则返回一个非null的数组。
        if (objects.length == 0)
            return new Object[0][];
        char type = 'i';
        // 这里可以最小限度地使用反射，也可以通过SQL语句判断，而不需要反射：
        if (byObjects[0] instanceof Double)
            type = 'd';
        if (byObjects[0] instanceof String)
            type = 's';
        // 内部类要用到这个类型结果，它必须是final的，或本质上是final的，因此要重新赋值一次：
        char typeFinal = type;
        // 下面要展现内部类的魅力了：
        class LineAndBy implements Comparable<LineAndBy> {
            Object[] line;
            Object byObject;
            LineAndBy(Object[] line, Object byObject) {
                this.line = line;
                this.byObject = byObject;
            }
            public int compareTo(LineAndBy that) {
                if (typeFinal == 'i') {
                    Integer thisInteger = (Integer) this.byObject;
                    Integer thatInteger = (Integer) that.byObject;
                    int compareResult = thisInteger.compareTo(thatInteger);
                    if (flagAscend)
                        return compareResult;
                    else
                        return -compareResult;
                }
                else if (typeFinal == 'd') {
                    Double thisDouble = (Double) this.byObject;
                    Double thatDouble = (Double) that.byObject;
                    int compareResult = thisDouble.compareTo(thatDouble);
                    if (flagAscend)
                        return compareResult;
                    else
                        return -compareResult;
                }
                else {
                    String thisString = (String) this.byObject;
                    String thatString = (String) that.byObject;
                    int compareResult = thisString.compareTo(thatString);
                    if (flagAscend)
                        return compareResult;
                    else
                        return -compareResult;
                }
            }
        }
        int amount = objects.length;
        int columns = objects[0].length;
        LineAndBy[] lineAndBys = new LineAndBy[amount];
        for (int i=0; i<amount; i++)
            lineAndBys[i] = new LineAndBy(objects[i], byObjects[i]);
        java.util.Arrays.sort(lineAndBys);
        Object[][] objectsToDisplay = new Object[amount][];
        for (int i=0; i<amount; i++)
            objectsToDisplay[i] = lineAndBys[i].line;
        return objectsToDisplay;
    }


}




