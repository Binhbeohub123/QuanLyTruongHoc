/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import Entity.User;
import java.util.List;

/**
 *
 * @author mphuc
 */
public interface UserDAO {

    void insert(User entity);

    void update(User entity);

    void delete(String username);

    User findById(String username);

    List<User> findAll();
}
