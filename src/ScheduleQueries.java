
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marqu
 */
public class ScheduleQueries {
    private static Connection connection;
    private static ArrayList<ScheduleEntry> scheduleList = new ArrayList<ScheduleEntry>();
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentsByCourse; 
    private static PreparedStatement getWaitlistedStudentsByCourse;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement updateScheduleEntry;
    private static ResultSet resultSet;
    
    public static void addScheduleEntry(ScheduleEntry entry)
    {
        connection = DBConnection.getConnection();
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        
        try
        {
            addScheduleEntry = connection.prepareStatement("insert into app.schedule(semester, studentID, courseCode, status, timeStamp) VALUES (?,?,?,?,?)");
            addScheduleEntry.setString(1, entry.getSemester());
            addScheduleEntry.setString(3, entry.getStudentID());
            addScheduleEntry.setString(2, entry.getCourseCode());
            addScheduleEntry.setString(4, entry.getStatus());
            addScheduleEntry.setTimestamp(5, timeStamp);
            addScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID) 
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> scheduleList = new ArrayList<ScheduleEntry>(); 
        
        try
        {
            getScheduleByStudent = connection.prepareStatement("select * from app.schedule where semester = ? and studentID = ?");
            getScheduleByStudent.setString(1, semester);
            getScheduleByStudent.setString(2, studentID);
            resultSet = getScheduleByStudent.executeQuery();
            
            while (resultSet.next())
            {
                scheduleList.add(new ScheduleEntry(resultSet.getString("semester"), resultSet.getString("studentID"), 
                        resultSet.getString("courseCode"), resultSet.getString("status"), resultSet.getTimestamp("timestamp")));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return scheduleList;
    }
    
    public static int getScheduleStudentCount(String currentSemester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int numStudents = 0;
        
        try
        {
            getScheduleByStudent = connection.prepareStatement("select count(studentID) from app.schedule where semester = ? and courseCode = ?");
            getScheduleByStudent.setString(1, currentSemester);
            getScheduleByStudent.setString(2, courseCode);
            resultSet = getScheduleByStudent.executeQuery();
            
            while (resultSet.next())
            {
                numStudents = resultSet.getInt(1); 
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return numStudents;
    }
    public static ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester,String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> studentSchedule = new ArrayList<ScheduleEntry>();
        
        try
        {
            getScheduledStudentsByCourse = connection.prepareStatement("select * from app.schedule where semester = ? and courseCode = ? and status = ?");
            getScheduledStudentsByCourse.setString(1, semester);
            getScheduledStudentsByCourse.setString(2, courseCode);
            getScheduledStudentsByCourse.setString(3, "S");
            
            resultSet = getScheduledStudentsByCourse.executeQuery();
            while(resultSet.next())
            {
               studentSchedule.add(new ScheduleEntry(resultSet.getString("semester"),
                       resultSet.getString("studentID"),resultSet.getString("courseCode"),
                       resultSet.getString("status"), resultSet.getTimestamp("timeStamp")) );
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentSchedule;
    }
    
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode) 
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> waitlistedSchedule = new ArrayList<ScheduleEntry>();
        
        try
        {
            getWaitlistedStudentsByCourse = connection.prepareStatement("select * from app.schedule where semester = ? and coursecode = ? and status = ? ORDER BY TIMESTAMP ASC");
            getWaitlistedStudentsByCourse.setString(1, semester);
            getWaitlistedStudentsByCourse.setString(2, courseCode);
            getWaitlistedStudentsByCourse.setString(3, "W"); 
            
            resultSet = getWaitlistedStudentsByCourse.executeQuery();
            while(resultSet.next())
            {
                waitlistedSchedule.add(new ScheduleEntry(resultSet.getString("Semester"),
                        resultSet.getString("studentID"),resultSet.getString("courseCode"),
                        resultSet.getString("status"),resultSet.getTimestamp(5)) );
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlistedSchedule;
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode)
    {
        connection = DBConnection.getConnection();
        
        try
        {
            dropStudentScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? and studentId = ? and courseCode = ?");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, studentID);
            dropStudentScheduleByCourse.setString(3, courseCode);
            dropStudentScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }   
    }
    
    public static void dropScheduleByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        
        try
        {
            dropScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? and courseCode = ?");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, courseCode);
            dropScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }   
    }
    
    public static void updateScheduleEntry(String semester, ScheduleEntry entry)
    {
        connection = DBConnection.getConnection();
        
        try
        {
            updateScheduleEntry = connection.prepareStatement("update app.schedule set status = ?" + "where semester = ? and studentID = ? and courseCode = ?" );
            updateScheduleEntry.setString(1, entry.getStatus()); 
            updateScheduleEntry.setString(2, semester);
            updateScheduleEntry.setString(3, entry.getStudentID());
            updateScheduleEntry.setString(4, entry.getCourseCode());
            updateScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }   
    }
}