package org.example;

import java.sql.*;

public class DatabaseManager
{
    static private String url = "jdbc:mysql://localhost:3306/netflixpp_db";
    static private String user = "admin";
    static private String password = "CodeGuy2020.";

    static private Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(url,user,password);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    static public int partialMovieInsertion(String name)
    {
        int id = -1;
        try
        {
            Connection connection = getConnection();
            String sql = "insert into movie_tbl (name) values (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,name);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next())
                id = resultSet.getInt(1);
            connection.close();
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
        return id;
    }

    static public void completeMovieInsertion(int id,String link,Chunk[] chunks)
    {
        try
        {
            Connection connection = getConnection();

            insertChunks(connection,chunks);

            String sql = "update movie_tbl set link=?,status=true where id_movie=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,link);
            preparedStatement.setInt(2,id);
            preparedStatement.execute();

            connection.close();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    static public void insertChunks(Connection connection,Chunk[] chunks)
    {
        try
        {
            for(Chunk chunk:chunks)
            {
                String sql = "insert into chunk_tbl (name,hash,movie) values (?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,chunk.name);
                preparedStatement.setString(2,chunk.hash);
                preparedStatement.setInt(3,chunk.movie);
                preparedStatement.execute();
            }
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }

    }

}
