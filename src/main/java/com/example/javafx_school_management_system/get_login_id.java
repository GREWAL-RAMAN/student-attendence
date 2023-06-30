package com.example.javafx_school_management_system;

import java.sql.ResultSet;
import java.sql.SQLException;

public class get_login_id {
    public static int id;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {

        ResultSet aa= connection.get_staff_id(id);
        try {
            get_login_id.id=aa.getInt("id");
        } catch (SQLException e) {
            System.out.println("inside get_login_id class ------------>");
            throw new RuntimeException(e);
        }

    }
}
