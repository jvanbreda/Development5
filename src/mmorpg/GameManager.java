/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmorpg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import models.Server;
import models.User;

/**
 *
 * @author Jesse
 */
public class GameManager {
    public static GameManager gm = new GameManager();
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MMORPGPUnit");
    private EntityManager em = emf.createEntityManager();
    private EntityTransaction et = em.getTransaction();
    public User loggedInUser;
     
    public void persist(Object object) {
        et.begin();
        try {
            em.persist(object);
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //et.rollback();
        } finally {
            //em.close();
        }
    }
    
    public List<String> checkUserName(String userName){
        Query q = em.createQuery("SELECT u.userName FROM User u WHERE u.userName = :userName");
        q.setParameter("userName", userName);
        List<String> results = q.getResultList();
        return results;
    }
    
    public User getUser(String userName){
        Query q = em.createQuery("SELECT u FROM User u WHERE u.userName = :userName");
        q.setParameter("userName", userName);
        return (User) q.getResultList().get(0);
    }
    
    public boolean validateLogin(String userName, String password){
        Query q = em.createQuery("SELECT u.userName, u.password FROM User u WHERE u.userName = :userName AND u.password = :password");
        q.setParameter("userName", userName);
        q.setParameter("password", password);
        return (!q.getResultList().isEmpty());
    }
    
    public int getUserWallet(){
        Query q = em.createQuery("SELECT u.balance FROM User u WHERE u.userName = :userName");
        q.setParameter("userName", loggedInUser.getUserName());
        int balance = (int) q.getResultList().get(0);
        return balance;
    }

    public void updateUserWallet(Integer amount){
        et.begin();
        Query q1 = em.createQuery("SELECT u.balance FROM User u WHERE u.userName = :userName");
        q1.setParameter("userName", loggedInUser.getUserName());
        List<Integer> result = q1.getResultList();
        
        Integer newBalance = result.get(0) + amount;
        Query q2 = em.createQuery("UPDATE User u SET u.balance = " + newBalance + " WHERE u.userName = :userName");
        q2.setParameter("userName", loggedInUser.getUserName());
        q2.executeUpdate();
        et.commit();
    }
    
    public List<Server> getServerList(){
        Query q = em.createQuery("SELECT s FROM Server s ORDER BY s.address ASC");
        return q.getResultList();
    }
    
    public Object[] getCharacterSlots(){
        Object[] characterSlots = new Object[2];
        Query q1 = em.createQuery("SELECT u.characterSlots FROM User u WHERE u.userName = :userName");
        q1.setParameter("userName", loggedInUser.getUserName());
        characterSlots[0] = (int) q1.getResultList().get(0);
        
        Query q2 = em.createQuery("SELECT COUNT (u.userName) FROM User u JOIN u.characterCollection c WHERE u.userName = :userName");
        q2.setParameter("userName", loggedInUser.getUserName());
        characterSlots[1] = (long) q2.getResultList().get(0);
        
        return characterSlots;
    }
    
    public void updateLastPayment(int months){
        et.begin();
        Query q1 = em.createQuery("SELECT u.monthsPayed FROM User u WHERE u.userName = :userName");
        q1.setParameter("userName", loggedInUser.getUserName());
        List<Integer> result = q1.getResultList();
        
        int newMonthsPayed = months + result.get(0);
        
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateToday = sdf.format(today);
        
        Query q2 = em.createQuery("UPDATE User u SET u.monthsPayed = " + newMonthsPayed + ", u.lastPayment = '" + dateToday + "' WHERE u.userName = :userName");
        q2.setParameter("userName", loggedInUser.getUserName());
        q2.executeUpdate();
        et.commit();
    }
    
    public void updateCharacterSlots(int slots){
        et.begin();
        
        Query q1 = em.createQuery("SELECT u.characterSlots FROM User u WHERE u.userName = :userName");
        q1.setParameter("userName", loggedInUser.getUserName());
        List<Integer> result = q1.getResultList();
        
        int newCharacterSlots = slots + result.get(0);
        
        Query q2 = em.createQuery("UPDATE User u SET u.characterSlots = " + newCharacterSlots + " WHERE u.userName = :userName");
        q2.setParameter("userName", loggedInUser.getUserName());
        q2.executeUpdate();
        et.commit();
    }
    
    public List<String> getCharacters(){     
        Query q = em.createQuery("SELECT c.name FROM User u JOIN u.characterCollection c WHERE u.userName = :userName ORDER BY c.level DESC");
        q.setParameter("userName", loggedInUser.getUserName());
        return q.getResultList();
    }
    
    public models.Character getCharacterInfo(String charName){
        Query q = em.createQuery("SELECT c FROM Character c WHERE c.name = :charName");
        q.setParameter("charName", charName);
        models.Character character = (models.Character)q.getResultList().get(0);
        return character;
    }

    public void linkCharToUser(models.Character character) {
        et.begin();
        loggedInUser.getCharacterCollection().add(character);
        character.getUserCollection().add(loggedInUser);
        em.merge(loggedInUser);
        em.merge(character);
        et.commit();       
    }

    public void linkServerToUser(User u, Integer address) {
        et.begin();
        Query q = em.createQuery("SELECT s FROM Server s WHERE s.address = :address");
        q.setParameter("address", address);
        Server s =  (Server) q.getResultList().get(0);
        
        u.getServerCollection().add(s);
        s.getUserCollection().add(u);
        em.merge(u);
        em.merge(s);
        et.commit();
        
        updateServerConnections(address);
    }
    
    public Integer[] getServerConnections(Integer address){
        Integer[] result = new Integer[2];
        Query q1 = em.createQuery("SELECT s.maxUsers FROM Server s WHERE s.address = :address");
        q1.setParameter("address", address);
        result[0] = (Integer) q1.getResultList().get(0);
        
        Query q2 = em.createQuery("SELECT s.connectedUsers FROM Server s WHERE s.address = :address");
        q2.setParameter("address", address);
        result[1] = (Integer) q2.getResultList().get(0);
        
        return result;
    }
    
    public void updateServerConnections(Integer address){
        Query q1 = em.createQuery("SELECT COUNT (s.address) FROM Server s JOIN s.userCollection c WHERE s.address = :address");
        q1.setParameter("address", address);
        long result = (long) q1.getResultList().get(0);
        
        et.begin();
        Query q2 = em.createQuery("UPDATE Server s SET s.connectedUsers = " + result + " WHERE s.address = :address");
        q2.setParameter("address", address);
        q2.executeUpdate();
        et.commit();
    }
    
    public String createRandomString(){
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String result = "";
        Random r = new Random();
        int times = r.nextInt(16) + 2;
        for (int i = 1; i <= times; i++){
            int index = r.nextInt(letters.length);
            result += letters[index];
        }
        
        return result;
    }
    
    public String[] createRandomRaceClass(){
        String[] races = {"White", "Black", "Asian", "Hispanic"};
        String[] classes = {"Archer", "Wizard", "Warrior"};
        
        Random r = new Random();
        String race = races[r.nextInt(races.length)];
        String classs = classes[r.nextInt(classes.length)];
        
        return new String[]{classs, race};
    }
    
    public void generateData(){
        for (int i = 0; i < 5000; i++){
            String firstName = createRandomString();
            String lastName = createRandomString();
            String iban = createRandomString();
            String userName = createRandomString();
            String password = createRandomString();
            
            User u = new User(firstName, lastName, iban, userName, password);
            
            String name = createRandomString();
            String[] classAndRace = createRandomRaceClass();
            models.Character c = new models.Character(name, classAndRace[0], classAndRace[1]);

            persist(u);
            persist(c);
            
            et.begin();
            u.getCharacterCollection().add(c);
            c.getUserCollection().add(u);
            em.merge(u);
            em.merge(c);
            et.commit();
            
            System.out.println(i);
        }
        
    }
}
    