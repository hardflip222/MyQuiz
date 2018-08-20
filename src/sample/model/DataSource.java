package sample.model;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource
{

    private static final String DB_NAME = "flags.db";
    private static final String DB_SOURCE = "jdbc:sqlite:C:/Users/biskupsk8/IdeaProjects/MyQuiz/database/";
    private static DataSource dataSourceInstance = new DataSource();
    private static final String SELECT_RANDOM_FLAG = "SELECT * FROM FLAG WHERE flag_id = ?";
    private static final String SELECT_MAX_MIN_FLAG_ID = "SELECT max(flag_id) AS max, min(flag_id) AS min FROM FLAG";
    private static final String SELECT_HIGH_SCORE = "SELECT highScore FROM HighScore";
    private static final String UPDATE_NEW_HIGH_SCORE = "UPDATE HighScore SET highScore = ?";
    private static final String SELECT_ALL_FROM_FLAG = "SELECT * FROM FLAG ORDER BY flag_id";
    private static final String DELETE_ALL_FROM_FLAG = "DELETE FROM FLAG";
    private static final String ADD_NEW_FLAG = "INSERT INTO FLAG (nation) VALUES (?)";
    private static final String SELECT_COUNT_FLAG = "SELECT count(flag_id) as amount FROM FLAG";
    private static final String SELECT_EXIST_FLAG = "SELECT count(flag_id) as amount FROM FLAG WHERE nation = ?";
    private static final String UPDATE_FLAG = "UPDATE FLAG SET nation = ? WHERE nation = ?";


    private Connection conn;

    private DataSource()
    {

    }

    public boolean open()
    {
        try {
            if(conn==null)
            {
                conn = DriverManager.getConnection(DB_SOURCE+DB_NAME);

                return true;
            }
            else
            {
                System.out.println(conn);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean close()
    {
        if(conn==null)
        {
            return false;
        }
        else
        {
            try {
                conn.close();
                return true;
            } catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
    }


    public static DataSource getInstance()
    {
        return dataSourceInstance;
    }


     public Question getRandomQuestion() throws Exception {
          if(conn!= null)
          {

              //max flag index
              Statement maxStatement = conn.createStatement();
              ResultSet maxResultSet = maxStatement.executeQuery(SELECT_MAX_MIN_FLAG_ID);

              int maxFlagId = maxResultSet.getInt("max");
              int minFlagId = maxResultSet.getInt("min");
              maxResultSet.close();


              //select random flag from database
              int randId = 0;
              PreparedStatement statementForRandomFlag = conn.prepareStatement(SELECT_RANDOM_FLAG);
              ResultSet randomFlagResultSet;


              do {
                  randId = (int)(Math.random()*(maxFlagId-minFlagId+1))+minFlagId;
                  System.out.println(randId);
                  statementForRandomFlag.setInt(1,randId);
                  randomFlagResultSet = statementForRandomFlag.executeQuery();


              }while(!randomFlagResultSet.next());





              Flag randomFlag = new Flag(randomFlagResultSet.getInt("flag_id"), randomFlagResultSet.getString("nation"));

              List<Flag> list = new ArrayList<>();

              randomFlagResultSet.close();



             //rand 5 incorrect flags
              for(int i =0;i<5;i++)
              {
                  Flag someFlag = null;

                     do {
                            statementForRandomFlag = conn.prepareStatement(SELECT_RANDOM_FLAG);
                            randId = (int)(Math.random()*(maxFlagId-minFlagId+1))+minFlagId;
                            statementForRandomFlag.setInt(1, randId);
                            randomFlagResultSet = statementForRandomFlag.executeQuery();

                                if(randomFlagResultSet.next())
                                someFlag = new Flag(randomFlagResultSet.getInt("flag_id"), randomFlagResultSet.getString("nation"));

                     }while(someFlag == null || someFlag.equals(randomFlag) || isFlagInList(list,someFlag));

                         list.add(someFlag);
                         randomFlagResultSet.close();

              }


              maxResultSet.close();
              return new Question(randomFlag,list);
          }
          else
          {
              throw new Exception("Connection is closed!!!");
          }
     }


     public int getHighScore()
     {
         Statement statement = null;
         try {
             statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SELECT_HIGH_SCORE);
             int a = rs.getInt("highScore");
             rs.close();
             statement.close();
             return a;
         } catch (SQLException e) {

             e.printStackTrace();
             return -1;
         }

     }

     public void setHighScore(int highScore)
     {
         PreparedStatement preparedStatement = null;
         try {

             preparedStatement = conn.prepareStatement(UPDATE_NEW_HIGH_SCORE);
             preparedStatement.setInt(1,highScore);
             preparedStatement.execute();
             preparedStatement.close();

         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     public List<Flag> getAllFlags()
     {
         try {
             List<Flag> flags = new ArrayList<>();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_FROM_FLAG);
             while(rs.next())
             {
                 Flag flag = new Flag(rs.getInt("flag_id"),rs.getString("nation"));
                 flags.add(flag);
             }

             return flags;

         } catch (SQLException e) {
             e.printStackTrace();
             return null;
         }

     }

     public void saveToDatabase(List<Flag> list)
     {
         try {
             Statement statement = conn.createStatement();
             statement.execute(DELETE_ALL_FROM_FLAG);
             statement.close();

             for(Flag flag : list)
             {
                 PreparedStatement preparedStatement = conn.prepareStatement(ADD_NEW_FLAG);
                 preparedStatement.setString(1,flag.getNation());
                 preparedStatement.execute();
                 preparedStatement.close();
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     public int getAmountOfFlags()
     {
         try {
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(SELECT_COUNT_FLAG);
             int amount = rs.getInt("amount");
             return amount;
         } catch (SQLException e) {

             e.printStackTrace();
             return -1;
         }
     }

     public boolean isFlagExists(String nation)
     {
         PreparedStatement preparedStatement = null;
         ResultSet rs = null;
         try {
             preparedStatement = conn.prepareStatement(SELECT_EXIST_FLAG);
             preparedStatement.setString(1,nation);
             rs = preparedStatement.executeQuery();
             if(rs.next())
             {
                 if(rs.getInt("amount")==1)
                     return true;
                 else
                     return false;

             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
        finally
         {
             try {
                 if (rs != null)
                     rs.close();
                 if (preparedStatement != null)
                     preparedStatement.close();
             }
             catch (SQLException e)
             {
                 e.printStackTrace();
             }
         }
         return false;
     }


     public void addNewFlag(String nation)
     {
         PreparedStatement preparedStatement;
         try {
             preparedStatement = conn.prepareStatement(ADD_NEW_FLAG);
             preparedStatement.setString(1,nation);
             preparedStatement.execute();
             preparedStatement.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     public void updateNation(String oldNation, String newNation)
     {
             PreparedStatement preparedStatement;

         try {
             preparedStatement = conn.prepareStatement(UPDATE_FLAG);
             preparedStatement.setString(1,newNation);
             preparedStatement.setString(2,oldNation);
             preparedStatement.execute();
             preparedStatement.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     private boolean isFlagInList(List<Flag> flags, Flag flag)
     {
         for(Flag f: flags)
         {
             if(f.equals(flag))
                 return true;
         }
         return false;
     }

}
