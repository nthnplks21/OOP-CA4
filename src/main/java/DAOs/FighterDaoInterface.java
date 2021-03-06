package DAOs;

import DTOs.Fighter;
import Exceptions.DaoException;

import java.util.List;

public interface FighterDaoInterface {

    public List<Fighter> findAllFighters() throws DaoException;
    public Fighter findFighterByID(int id) throws DaoException;
    boolean deleteFighterByID(int id) throws DaoException;
    void addFighterToDB(String name, int wins, int losses) throws DaoException;
    List<Fighter> findAllPositiveRatio() throws DaoException;
    String findAllAsJSON() throws DaoException;
    String findByIdAsJSON(int id) throws DaoException;



}
