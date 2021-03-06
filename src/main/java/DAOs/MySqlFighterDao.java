package DAOs;

import DTOs.Fighter;
import Exceptions.DaoException;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MySqlFighterDao extends MySqlDao implements FighterDaoInterface
{

    @Override
    public List<Fighter> findAllFighters() throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Fighter> fightersList = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM fighters";
            ps = connection.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                String name = resultSet.getString("name");
                int wins = resultSet.getInt("wins");
                int losses = resultSet.getInt("losses");
                Fighter f = new Fighter(name,wins,losses);
               fightersList.add(f);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllFighterResultSet() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllFighters() " + e.getMessage());
            }
        }
        return fightersList;     // may be empty
    }


    @Override
    public Fighter findFighterByID(int id) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Fighter fighter = null;
        try
        {
            connection = this.getConnection();

            String query = "SELECT * FROM fighters WHERE id = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);


            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                String name = resultSet.getString("name");
                int wins = resultSet.getInt("wins");
                int losses = resultSet.getInt("losses");
                fighter = new Fighter(name,wins,losses);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findFighterByID " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findUserByUsernamePassword() " + e.getMessage());
            }
        }
        return fighter;     // reference to User object, or null value
    }

    @Override
    public boolean deleteFighterByID(int id) throws DaoException {

            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try
            {
                connection = this.getConnection();


                Fighter fighter = findFighterByID(id);

                if (fighter ==null){
                    System.out.println("Fighter not found");
                    return false;
                }


                String query = "DELETE  FROM fighters WHERE id = ? ";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, id);

                preparedStatement.executeUpdate();
                return true;

            } catch (SQLException e)
            {
                throw new DaoException("deleteFighterByID " + e.getMessage());
            } finally
            {
                try
                {

                    if (preparedStatement != null)
                    {
                        preparedStatement.close();
                    }
                    if (connection != null)
                    {
                        freeConnection(connection);
                    }
                } catch (SQLException e)
                {
                    throw new DaoException("findUserByUsernamePassword() " + e.getMessage());
                }
            }
        }

    @Override
    public void addFighterToDB(String name,int wins,int losses) throws DaoException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.getConnection();

            String query = "INSERT INTO fighters VALUES (null,?,?,?) ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2,wins);
            preparedStatement.setInt(3,losses);

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public List<Fighter> findAllPositiveRatio() throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Fighter> fightersList = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM fighters WHERE wins >= losses";
            ps = connection.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                String name = resultSet.getString("name");
                int wins = resultSet.getInt("wins");
                int losses = resultSet.getInt("losses");
                Fighter f = new Fighter(name,wins,losses);
                fightersList.add(f);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllFighterResultSet() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllFighters() " + e.getMessage());
            }
        }
        return fightersList;     // may be empty
    }

    @Override
    public String findAllAsJSON() throws DaoException {
        List<Fighter> fightersList  = findAllFighters();

        Gson gsonParser = new Gson();
        String fightersJSON = gsonParser.toJson(fightersList);
        return  fightersJSON;
    }


    @Override
    public String findByIdAsJSON(int id) throws DaoException{

       Fighter fighter =  findFighterByID(id);
       Gson gsonParser = new Gson();

       String jsonFighter = gsonParser.toJson(fighter);

        return jsonFighter;

    }


}


