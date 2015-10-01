/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jesse
 */
@Entity
@Table(name = "\"user\"")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserName", query = "SELECT u FROM User u WHERE u.userName = :userName"),
    @NamedQuery(name = "User.findByBalance", query = "SELECT u FROM User u WHERE u.balance = :balance"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByIban", query = "SELECT u FROM User u WHERE u.iban = :iban"),
    @NamedQuery(name = "User.findByCharacterSlots", query = "SELECT u FROM User u WHERE u.characterSlots = :characterSlots"),
    @NamedQuery(name = "User.findByLastPayment", query = "SELECT u FROM User u WHERE u.lastPayment = :lastPayment"),
    @NamedQuery(name = "User.findByMonthsPayed", query = "SELECT u FROM User u WHERE u.monthsPayed = :monthsPayed"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByBanned", query = "SELECT u FROM User u WHERE u.banned = :banned")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    @Column(name = "balance")
    private Integer balance;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "iban")
    private String iban;
    @Column(name = "character_slots")
    private Integer characterSlots;
    @Column(name = "last_payment")
    private String lastPayment;
    @Column(name = "months_payed")
    private Integer monthsPayed;
    @Column(name = "password")
    private String password;
    @Column(name = "banned")
    private Boolean banned;
    @ManyToMany(mappedBy = "userCollection", fetch = FetchType.LAZY)
    private Collection<Server> serverCollection;
    @ManyToMany(mappedBy = "userCollection", fetch = FetchType.LAZY)
    private Collection<Character> characterCollection;

    public User() {
    }

    public User(String firstName, String lastName, String iban, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.iban = iban;
        this.userName = userName;
        this.password = password;
        this.balance = 0;
        this.banned = false;
        this.characterSlots = 5;
        this.lastPayment = "01-01-1970";
        this.monthsPayed = 0;
        this.characterCollection = new ArrayList<>();
        this.serverCollection = new ArrayList<>();
        
    }
    
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Integer getCharacterSlots() {
        return characterSlots;
    }

    public void setCharacterSlots(Integer characterSlots) {
        this.characterSlots = characterSlots;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public Integer getMonthsPayed() {
        return monthsPayed;
    }

    public void setMonthsPayed(Integer monthsPayed) {
        this.monthsPayed = monthsPayed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @XmlTransient
    public Collection<Server> getServerCollection() {
        return serverCollection;
    }

    public void setServerCollection(Collection<Server> serverCollection) {
        this.serverCollection = serverCollection;
    }

    public Collection<Character> getCharacterCollection() {
        return characterCollection;
    }

    public void setCharacterCollection(Collection<Character> characterCollection) {
        this.characterCollection = characterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.User[ userName=" + userName + " ]";
    }
    
}
