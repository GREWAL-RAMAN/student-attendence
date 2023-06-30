package com.example.javafx_school_management_system;

import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class connection {
    public static Connection con;
    public static int UserId,User_Role;


    public static void  connection(){
        connect();
    }

    private static void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

        con = null;
        try
        {

            // create a database connection
            con = DriverManager.getConnection("jdbc:sqlite:D:\\D Sqlite\\newHello.db");
            System.out.println("Success: Database connected successfully");
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }
    public static int checkUserLogin(String username, String password){
        int user_id = 0;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.


            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username='"+ username +"' AND password='"+ password +"' AND status ='Active'");


            if(rs.next()){
                UserId = user_id = rs.getInt("user_id");
                User_Role= rs.getInt("user_role");
            }else{
                UserId = 0;
                User_Role=0;
            }
        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return user_id;
    }
    public  static ResultSet getUserInfo(int ID)
    {
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from users WHERE user_id='"+ ID +"'");

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static void update_userLogin(int id, String username, String pass, String status){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE users SET username='"+ username +"', password='"+ pass +"', status='"+ status +"' WHERE user_id='"+ id +"'";
            statement.executeUpdate(sql);

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }


    public static void closeConn()
    {
        try {
            UserId=0;
            User_Role=0;
            con.close();
            System.out.println("connection closed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet get_classes(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_class " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }

      public static ResultSet get_table_for_combo(String first,String second,String table, String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select "+first +", "+second+"  from  "+table +" WHERE status ='Active' " +
                    " " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage()+"\n inside get_table_for_combo");
        }

        return rs;
    }

    public static ResultSet insert_class(String class_name, int sort_order, String status){
        ResultSet s=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);
            String sql = "INSERT INTO manage_class(class_name, sort_order, status) VALUES ('"+ class_name +"', '"+ sort_order +"', '"+ status +"')";
        statement.executeUpdate(sql);
        statement.close();
       Statement state=con.createStatement();
        s=      state.executeQuery( " SELECT * FROM  manage_class WHERE   id = (SELECT MAX(id)  FROM manage_class)");


        } catch(SQLException e)
        {
                      System.err.println("Error : " + e.getMessage());
        }
        return s;
    }

    public static void update_class(int class_id, String class_name, int sort_order, String status){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE manage_class SET class_name='"+ class_name +"', sort_order='"+ sort_order +"', status='"+ status +"' WHERE id ='"+ class_id +"'";
            if(status.equals("Active")) {
                statement.executeUpdate(sql);
            }
            else {
                statement.executeUpdate(sql);
                Statement state2=con.createStatement();
                String sql2="UPDATE student_class_section SET class_name= '"+class_name+"', status= '"+status+"' WHERE class_id = '"+class_id+"'";
                Statement state3=con.createStatement();
                String sql3="UPDATE teacher_class_section SET class_name= '"+class_name+"',  status= '"+status+"' WHERE class_id = '"+class_id+"'";

                state2.execute(sql2);
                state3.execute(sql3);
            }

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }

    public static void delete_class(int id){
        try
        {
            int class_section_id=-1;
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "DELETE FROM manage_class WHERE id ='"+ id +"'";
            statement.execute(sql);

            Statement state2=con.createStatement();
            String pp3="DELETE FROM teacher_class_section WHERE class_id= '"+id+"'";
            Statement state4=con.createStatement();
            String pp4="DELETE FROM student_class_section WHERE class_id= '"+id+"'";

            state2.execute(pp3);
            state4.execute(pp4);

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

    }
    public static ResultSet get_class(int class_id){
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_class WHERE id='"+ class_id +"'");

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }

    //============================================================================================================
    public static ResultSet get_Sections(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_section " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }


    public static ResultSet insert_Section(String class_name, int sort_order, String status){
        ResultSet s=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);
            String sql = "INSERT INTO manage_section(name, sort_order, status) VALUES ('"+ class_name +"', '"+ sort_order +"', '"+ status +"')";
            statement.executeUpdate(sql);
            statement.close();
            Statement state=con.createStatement();
            s=  state.executeQuery( " SELECT * FROM  manage_section WHERE   id = (SELECT MAX(id)  FROM manage_section)");


        } catch(SQLException e)
        {
            System.err.println("Error : " + e.getMessage());
        }
        return s;
    }


    public static void update_Section(int class_id, String class_name, int sort_order, String status){
        try
        {
            int class_section_id=-1;
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE manage_section SET name='"+ class_name +"', sort_order='"+ sort_order +"', status='"+ status +"' WHERE id='"+ class_id +"'";
            statement.executeUpdate(sql);

            Statement state4=con.createStatement();
            String sql4="SELECT id  FROM class_section WHERE section_ref = '"+class_id+"' ";
            ResultSet rr=state4.executeQuery(sql4);
            class_section_id=rr.getInt("id");
            if(status.equals("Inactive"))
            {
                Statement state = con.createStatement();
                String sql1 = "UPDATE class_section SET section_name= '" + class_name + "' ,  status= '" + status + "' WHERE section_ref = '" + class_id + "'";
                Statement state2 = con.createStatement();
                String sql2 = "UPDATE student_class_section SET section_name= '" + class_name + "',  status= '" + status + "' WHERE class_section_id = '" + class_section_id + "'";
                Statement state3 = con.createStatement();
                String sql3 = "UPDATE teacher_class_section SET section_name= '" + class_name + "' ,  status= '" + status + "' WHERE class_section_id = '" + class_section_id + "'";
                state.execute(sql1);
                state2.execute(sql2);
                state3.execute(sql3);
            }
            else{
                Statement state = con.createStatement();
                String sql1 = "UPDATE class_section SET section_name= '" + class_name + "' WHERE section_ref = '" + class_id + "'";
                Statement state2 = con.createStatement();
                String sql2 = "UPDATE student_class_section SET section_name= '" + class_name + "' WHERE class_section_id = '" + class_section_id + "'";
                Statement state3 = con.createStatement();
                String sql3 = "UPDATE teacher_class_section SET section_name= '" + class_name + "' WHERE class_section_id = '" + class_section_id + "'";
                state.execute(sql1);
                state2.execute(sql2);
                state3.execute(sql3);
            }

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }

    public static void delete_Section(int id){
        try
        {
            int class_section_id =-1;
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "DELETE FROM manage_section WHERE id ='"+ id +"'";
            statement.execute(sql);
            Statement state1=con.createStatement();
            String pp="SELECT id FROM class_section WHERE section_ref= '"+id+"'";
            ResultSet rr=state1.executeQuery(pp);
            class_section_id=rr.getInt("id");
            Statement state=con.createStatement();
            String pp2="DELETE FROM class_section WHERE section_ref= '"+id+"'";
            Statement state2=con.createStatement();
            String pp3="DELETE FROM teacher_class_section WHERE class_section_id= '"+class_section_id+"'";
            Statement state4=con.createStatement();
            String pp4="DELETE FROM student_class_section WHERE class_section_id= '"+class_section_id+"'";
            state.execute(pp2);
            state2.execute(pp3);
            state4.execute(pp4);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

    }
    public static ResultSet get_Section(int class_id){
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_section WHERE id='"+ class_id +"'");

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
  //===================+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++==============================
  //==================================================================================================================
  //  ========================================================================================================
  public static void insert_staff__(String name, int role, String status,String user,String address,long contact,String degree){

      try
      {
          Statement state=con.createStatement();
          state.executeUpdate("INSERT INTO users(username,password,user_role,status) VALUES" +
                  " ('"+user+"','"+"12345"+"','"+role+"','"+status+"')");

          int user_id_=  get_Last_users_id("users","user_id");


          String sql = "INSERT INTO manage_staff(name, address,contact,user,status,degree,password,work,user_id) " +
                  "VALUES ('"+ name +"', '"+ address +"', '"+ contact +"', '"+
                  user +"', '"+ status +"', '"+ degree +"', '"+ "12345" +"', '"+ role +"','"+user_id_+"')";

          Statement statement = con.createStatement();
          statement.executeUpdate(sql);
          int staffId=get_Last_users_id("manage_staff","id");

          Statement aaa=con.createStatement();
          ResultSet ll=aaa.executeQuery("SELECT *  FROM session ");

          String ss="INSERT INTO session  (user_ref,staff_ref,date_start,date_end) " +
                  "VALUES('"+user_id_+"','"+staffId+"','"+ll.getString("date_start")+"','"+ll.getString("date_end")+"')";
          Statement opoo=con.createStatement();
          opoo.executeUpdate(ss);


      } catch(SQLException e)
      {
          System.err.println("Error : fghdfhd " + e.getMessage()
          +" "+e.getLocalizedMessage());
      }

  }
  public static  ResultSet get_session(int userId)
  {
      ResultSet rr=null;
      try{
          Statement state=con.createStatement();
          String sql="SELECT * FROM  session WHERE user_ref ='"+userId+"'";
          rr=state.executeQuery(sql);


      }catch (SQLException e)
      {
          System.out.println(e.getMessage());
      }


      return rr;
  }
  public static int get_count(int teacher_id,String start_date,String End_date,String status)
  {
      int result=-1;
      try{
          Statement aa=con.createStatement();
          String ll="SELECT COUNT(*) as result FROM teachers_attendance WHERE teachers_id='"+teacher_id+"' AND status ='"+status+"'" +
                  "  AND date_recorded <='"+End_date+"' AND date_recorded >='"+start_date+"'";
          ResultSet ss= aa.executeQuery(ll);
          result=ss.getInt("result");

      }catch (SQLException w)
      {
          System.out.println(w.getMessage());
      }

      return result;
  }
    public static int get_count_2(String table)
    {
        int result=-1;
        try{
            Statement aa=con.createStatement();
            String ll="SELECT COUNT(*) as result FROM "+ table ;
            ResultSet ss= aa.executeQuery(ll);
            result=ss.getInt("result");

        }catch (SQLException w)
        {
            System.out.println(w.getMessage());
        }

        return result;
    }

  public static void update_session(String start,String end)
  {
       try{
           Statement state=con.createStatement();
           String Sql="UPDATE session SET date_start='"+start+"',date_end='"+end+"'  WHERE user_ref='"+UserId+"'";
           state.executeUpdate(Sql);
       }catch (SQLException e)
       {
           System.out.println(e.getMessage());
       }
  }


   private static int get_Last_users_id(String table ,String field)
   {
       int i=-1;
       try{
           Statement op=con.createStatement();
           String sql="SELECT *  FROM  "+table +"   WHERE   "+field +" = (SELECT MAX( "+field+" )  FROM  "+table+" )";
           ResultSet rr=op.executeQuery(sql);
           i=rr.getInt(field);

       }catch (SQLException e)
       {
           System.out.println(e.getMessage()+" probalesdf "+e.getErrorCode());
       }

       return i;
   }


    public static void update_staff(int class_id,String name, int role, String status,String address,long contact,String degree){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE manage_staff SET name='"+ name +"', contact='"+ contact +"', status='"+ status +"'" +
                    ", address='"+ address +"', work='"+ role +"' , degree='"+ degree +"' WHERE id='"+ class_id +"'";
             if(status.equals("Active"))
             {
                 statement.executeUpdate(sql);

                 Statement state =con.createStatement();
                 String ss="SELECT user_id FROM manage_staff WHERE id= '"+class_id+"'";
                 ResultSet rs=state.executeQuery(ss);

                 Statement state2=con.createStatement();
                 String sql2="UPDATE users SET user_role ='"+role+"', status='"+ status +"' WHERE  user_id = '"+rs.getInt("user_id")+"'";
                 state2.execute(sql2);

             }
             else
             {
                 statement.executeUpdate(sql);

                 Statement state =con.createStatement();
                 String ss="SELECT user_id FROM manage_staff WHERE id= '"+class_id+"'";
                 ResultSet rs=state.executeQuery(ss);

                 Statement state2=con.createStatement();
                 String sql2="UPDATE users SET user_role ='"+role+"', status='"+ status +"' WHERE  user_id = '"+rs.getInt("user_id")+"'";
                 state2.execute(sql2);

                 Statement state3=con.createStatement();
                 String sql3="UPDATE teacher_class_section SET teacher_name= '"+name+"' , status='"+ status +"' WHERE teacher_id = '"+class_id+"'";
                 state3.execute(sql3);

             }

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }

    public static void delete_staff(int id){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
           String a  = "SELECT user_id FROM manage_staff WHERE id ='"+ id +"'";
          ResultSet m=  statement.executeQuery(a);

            Statement abc=con.createStatement();
            String sql = "DELETE FROM manage_staff WHERE id ='"+ id +"'";
            abc.executeUpdate(sql);

            Statement abcd=con.createStatement();
            String sql2 = "DELETE FROM teacher_class_section WHERE teacher_id ='"+ id +"'";
            abcd.executeUpdate(sql2);

            Statement cba=con.createStatement();
            String lop = "DELETE FROM users WHERE user_id ='"+ m.getInt("user_id") +"'";
            cba.executeUpdate(lop);
           statement.close();
            abc.close();
            cba.close();
            abcd.close();;
        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

    }

    public static ResultSet get_staffs(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_staff " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.out.println("inside getStaffs");
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static ResultSet get_staff(int staff_id){
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_staff WHERE id='"+ staff_id +"'");

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    public static void insert_ClassSection(int classID, int sectionID ,String class_name,String section_name,String status){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);
            String sql = "INSERT INTO" + " class_section" +
                    "(class_name, class_ref, section_name,section_ref,status)" +
                    " VALUES ('"+ class_name +"', '"+ classID +"', '"+ section_name +"', '"+ sectionID +"', '"+ status +"')";
            statement.executeUpdate(sql);

        } catch(SQLException e)
        {
            System.err.println("Error : " + e.getMessage());
        }
    }

    public static ResultSet get_ClassesSections(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from class_section " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static void delete_ClassSection(int id){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "DELETE FROM class_section WHERE id ='"+ id +"'";
            statement.execute(sql);


            Statement state2=con.createStatement();
            String pp3="DELETE FROM teacher_class_section WHERE class_section_id= '"+id+"'";
            Statement state4=con.createStatement();
            String pp4="DELETE FROM student_class_section WHERE class_section_id= '"+id+"'";

            state2.execute(pp3);
            state4.execute(pp4);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }
    public static void update_ClassSection(int id, String class_name, int class_ref,String section_name, int section_ref ,String status){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE class_section SET class_name='"+ class_name +"'," +
                    " class_ref='"+ class_ref +"',section_name='"+ section_name +"',section_ref='"+ section_ref +"'," +
                    " status='"+ status +"' WHERE id='"+ id +"'";

            statement.executeUpdate(sql);
            Statement state2=con.createStatement();
            String sql2="UPDATE student_class_section SET class_name= '"+class_name+"' , section_name = '"+section_name+"', SET status= '"+status+"' WHERE class_section_id = '"+id+"'";
            Statement state3=con.createStatement();
            String sql3="UPDATE teacher_class_section SET class_name= '"+class_name+"' , section_name = '"+section_name+"' , SET status= '"+status+"' WHERE class_section_id = '"+id+"'";

            state2.execute(sql2);
            state3.execute(sql3);

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void insert_TeacherClassSection(String teacher_name,int teacher_ID,int Class_ID,String class_name,String status){
        try
    {
        Statement statement = con.createStatement();
        statement.setQueryTimeout(30);
        String sql = "INSERT INTO" + " teacher_class_section" +
                "(teacher_name,teacher_id,class_id,class_name ,status)" +
                " VALUES ('"+ teacher_name +"', '"+ teacher_ID +"', '"+Class_ID +"', '"+ class_name +"', '"+ status +"')";
        statement.executeUpdate(sql);

    } catch(SQLException e)
    {
        System.err.println("Error : " + e.getMessage());
    }
}

    public static ResultSet get_TeacherClassesSections(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from teacher_class_section " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static void delete_TeacherClassSection(int id){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "DELETE FROM teacher_class_section WHERE id ='"+ id +"'";
            statement.execute(sql);

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }
    public static void update_TeacherClassSection(int id,String teacher_name,int teacher_id,int class_id ,String class_name,String status){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE teacher_class_section SET class_name='"+ class_name +"'," +
                    " teacher_name='"+ teacher_name +"',teacher_id='"+ teacher_id +"'," +
                    " status='"+ status +"',class_id='"+ class_id +"' WHERE id='"+ id +"'";
            if(status.equals("Inactive"))
            {
                statement.executeUpdate(sql);
            }
            else
            {
                statement.executeUpdate(sql);

                Statement pp=con.createStatement();
                String oii="UPDATE manage_class SET status = 'Active'  WHERE id ='"+class_id+"' ";
                pp.executeUpdate(oii);

                Statement mm=con.createStatement();
                String gg="UPDATE manage_staff SET status = 'Active'  WHERE id ='"+teacher_id+"' ";
                mm.executeUpdate(gg);
            }

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void insert_student(String name, String status,String address,long contact,String date,String guardian){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);
            String sql = "INSERT INTO manage_student(name,address,contact,guardian,status,date) " +
                    "VALUES ('"+ name +"', '"+ address +"', '"+ contact +"', '"+
                    guardian +"', '"+ status +"', '"+ date +"')";
            statement.executeUpdate(sql);
            statement.close();
        } catch(SQLException e)
        {
            System.err.println("Error : " + e.getMessage());
        }
    }


    public static void update_student(int id,String name, String status,String address,long contact,String guardian,String date){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE manage_student SET name='"+ name +"', contact='"+ contact +"', status='"+ status +"'" +
                    ", address='"+ address +"', guardian='"+ guardian +"' , date='"+ date +"' WHERE id='"+ id +"'";
          if(status.equals("Active"))
          {
              statement.executeUpdate(sql);
          }
          else
          {
              statement.executeUpdate(sql);

              Statement state2=con.createStatement();
              String sql2="UPDATE student_class_section SET student_name = '"+name+"' , status = '"+status+"' WHERE student_id = '"+id+"'";
              Statement state3=con.createStatement();
              String sql3="UPDATE student_attendance SET student_name= '"+name+"' WHERE student_id = '"+id+"'";

              state2.execute(sql2);
              state3.execute(sql3);
          }

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }

    public static void delete_student(int id){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String a  = "DELETE FROM manage_student WHERE id ='"+ id +"'";
            statement.execute(a);

            Statement state2=con.createStatement();
            String pp3="DELETE FROM student_class_section WHERE student_id= '"+id+"'";

            state2.execute(pp3);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

    }

    public static ResultSet get_students(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_student " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.out.println("inside getStaffs");
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static ResultSet get_student(int id){
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_student WHERE id='"+ id +"'");

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void insert_stu_cla_sec(int student_ID,int Class_ID,String student_name,String class_name,String status){
    try
    {
        Statement statement = con.createStatement();
        statement.setQueryTimeout(30);
        String sql = "INSERT INTO" + " student_class_section" +
                "(student_name,student_id,class_id,class_name ,status)" +
                " VALUES ('"+ student_name +"', '"+ student_ID +"', '"+ Class_ID +"', '"+ class_name +"', '"+ status +"')";
        statement.executeUpdate(sql);

    } catch(SQLException e)
    {
        System.err.println("Error : " + e.getMessage());
    }
}

    public static ResultSet get_StudentClassesSections(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from student_class_section " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static void delete_StudentClassSection(int id){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "DELETE FROM student_class_section WHERE id ='"+ id +"'";
            statement.execute(sql);

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }
    public static void update_stu_cla_sec(int id,int student_id,String student_name,int class_id ,String class_name,String status){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE student_class_section SET class_name= '"+ class_name +"'" +
                    ",student_name='"+ student_name +"'" +
                    ",student_id='"+ student_id +"'," +
                    " status='"+ status +"',class_id='"+ class_id +"' WHERE id='"+ id +"'";
            if(status.equals("Inactive"))
            {
                statement.executeUpdate(sql);
            }
            else
            {
                if (!check_status("manage_class", "id", class_id) || !check_status("manage_student", "id", student_id))
                {
                    if (AlertOption.ConfirmBox("Selected class or student might be inactive, do you want to continue?") == 1) ;
                    {
                        statement.executeUpdate(sql);
                        Statement pp=con.createStatement();
                        String oii="UPDATE manage_class SET status = 'Active'  WHERE id ='"+class_id+"' ";
                        pp.executeUpdate(oii);
                        Statement mm=con.createStatement();
                        String yy="UPDATE manage_student SET status = 'Active'  WHERE id ='"+student_id+"' ";
                        mm.executeUpdate(yy);
                    }
                }
                else{
                    statement.executeUpdate(sql);
                }
            }

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage()+
                    " \n inside update_stu_cla_sec()");
        }
    }

    private static boolean check_status(String table,String id_name,int id_value)
    {
        try{
            Statement kl=con.createStatement();
            String fd="SElECT * from "+table+" WHERE "+id_name+" ='"+id_value+"'";
            ResultSet rr=kl.executeQuery(fd);
            if(rr.getString("status").equals("Active"))
            {
                return true;
            }
        }catch (SQLException e)
        {
            System.out.println("ERROR: "+e.getMessage()+"\n inside check_status()");
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void insert_attendance(int teachers_id,String name,String date ,String week,String status,String table)
    {
      try { Statement statement = con.createStatement();
        statement.setQueryTimeout(30);
        boolean check=checkAttendance(teachers_id,date,table);
        if(check==false) {
            String sql = "INSERT INTO " +  table +
                    " (teachers_id,teacher_name,date_recorded,weekday,status)" +
                    " VALUES ('" + teachers_id + "', '" + name + "','" + date + "','" + week + "', '" + status + "' )";
            statement.executeUpdate(sql);
            statement.close();
        }

    } catch(SQLException e)
    {
        System.err.println("Error : " + e.getMessage());
    }
    }
    public static void insert_attendance_2(int teachers_id,String name,String date ,String week,String status,String table)
    {
        try { Statement statement = con.createStatement();
            statement.setQueryTimeout(30);
            boolean check=checkAttendance(teachers_id,date,table);
          if(check==false)  {
                String sql = "INSERT INTO teachers_attendance(teachers_id,teacher_name,date_recorded,weekday,status)" +
                        " VALUES ('" + teachers_id + "', '" + name + "', '" + date + "','" + week + "', '" + status + "' )";
                statement.executeUpdate(sql);
                statement.close();
            }

        } catch(SQLException e)
        {
            System.err.println("Error : " + e.getMessage());
        }
    }
    public static void delete_attendance(int teachers_id,String date)
    {
        try { Statement statement = con.createStatement();
            statement.setQueryTimeout(30);

           {
                String sql =" DELETE FROM teachers_attendance WHERE teachers_id ='"+teachers_id+"' AND date_recorded ='"+date+"'";
                        statement.executeUpdate(sql);
                statement.close();
            }

        } catch(SQLException e)
        {
            System.err.println("Error : " + e.getMessage());
        }
    }
    private static boolean checkAttendance(int teachers_id,String date,String table)
    {
        ResultSet rs;
        try{
            Statement state =con.createStatement();
            String sql="SELECT * FROM "+ table +" WHERE teachers_id ='"+
                    teachers_id+"'AND date_recorded ='"+date+"'";
             rs=state.executeQuery(sql);
            if(!rs.getString("date_recorded").isBlank())
            {
                System.out.println("true");
                return true;
            }
        }catch (SQLException e)
        {
            System.out.println(e.getErrorCode()+"  "+e.getMessage());
        }
        System.out.println("false");
        return false;
    }
    public static boolean check_holiday(String date)
    {
        ResultSet rs;
        try{
            Statement state =con.createStatement();
            String sql="SELECT holiday  FROM calendar  WHERE  d ='"+date+"'";
            rs=state.executeQuery(sql);
            if(rs.getString("holiday").equals("no")||rs.getString("holiday").isBlank())
            {
                System.out.println("true");
                return true;
            }
        }catch (SQLException e)
        {
            System.out.println(e.getErrorCode()+"  "+e.getMessage());
        }
        System.out.println("false");
        return false;
    }
    public static boolean checkStudentAttendance(int student_id,String date,String table)
    {
        ResultSet rs;
        try{
            Statement state =con.createStatement();
            String sql="SELECT * FROM "+ table +" WHERE student_id ='"+
                    student_id+"'AND date ='"+date+"'";
            rs=state.executeQuery(sql);
            if(!rs.getString("date").isBlank())
            {
                return true;
            }
        }catch (SQLException e)
        {
            System.out.println(e.getErrorCode()+"  "+e.getMessage());
        }
        System.out.println("false");
        return false;
    }
    public static void insert_student_attendance(int teachers_id, int student_id,String name,String date,String day,String present,String excuse)
    {
        try {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);
            boolean check=checkStudentAttendance(student_id,date,"student_attendance");
            if(check==false) {
                String sql = "INSERT INTO " + "student_attendance" +
                        " (teacher_id,student_id,student_name,date,day,status,excuse)" +
                        " VALUES ('" + teachers_id + "', '" + student_id + "', '" + name + "', '" + date + "','" + day + "','" + present + "', '" + excuse + "' )";
                statement.executeUpdate(sql);
                statement.close();
            }

        } catch(SQLException e)
        {
            System.err.println("Error : " + e.getMessage());
        }
    }


    public static ResultSet get_students_attend(int teacher_id)
    {
        ResultSet ss=null;
        try{
            Statement state=con.createStatement();
            String op="SELECT student_class_section.student_id,student_class_section.student_name FROM student_class_section" +
                    " LEFT JOIN teacher_class_section ON student_class_section.class_id=teacher_class_section.class_id" +
                    " WHERE teacher_class_section.teacher_id = ' "+teacher_id+"' ";
            ss=state.executeQuery(op);

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return ss;
    }

    public static ResultSet get_students_attend_2(int teacher_id,String filter)
    {
        if(filter == null){
            filter = " ";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from student_attendance  WHERE teacher_id ='"+teacher_id+"' " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static ResultSet get_students_attend_3(String filter)
    {
        if(filter == null){
            filter = " ";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from student_attendance   " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }

    public static ResultSet get_teacher_attend(String filter)
    {
        if(filter == null){
            filter = " ";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from teachers_attendance  " + filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }


    public static ResultSet get_teacher_attend_2(String filter,int teacher_id,String date)
    {
        if(filter == null){
            filter = " ";
        }
        ResultSet rs=null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from teachers_attendance WHERE teachers_id =' "+teacher_id+"' " +
                    " AND date_recorded >= '"+date+"'  "+ filter);


        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }


    public static ResultSet get_staff_id(int user_id){
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from manage_staff WHERE user_id='"+ user_id +"'");

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }

    public static ResultSet get_UserId(){
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select user_id from users ");

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static ResultSet get_calender(String filter){
        if(filter == null){
            filter = "";
        }
        ResultSet rs = null;
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            rs = statement.executeQuery("select * from calendar "+ filter);

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }

        return rs;
    }
    public static void update_calendar(String date, String holiday, String reason){
        try
        {
            Statement statement = con.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "UPDATE calendar SET holiday='"+ holiday +"', what_holiday='"+reason+" '  WHERE d = '"+ date +"'";
            statement.executeUpdate(sql);

        } catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println("Error : " + e.getMessage());
        }
    }



}

