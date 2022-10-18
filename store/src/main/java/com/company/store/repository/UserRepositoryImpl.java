package com.company.store.repository;

import com.company.store.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public User save(User user){
        // encrypt user password entered in the page form
        String encryptedPassword = encoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        if(user.getId() == null){
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return (User) entityManager.createNamedQuery(User.USER_FIND_BY_EMAIL).setParameter("email", email).getResultList().get(0);
    }

    @Override
    public User findById(int Id) {
        return (User) entityManager.createQuery(String.format("SELECT u FROM User u WHERE u.Id = %s", Id)).getResultList().get(0);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createNamedQuery(User.USER_FIND_ALL).getResultList();
    }

    @Override
    public boolean isUserPasswordCorrect(String userEmail, String passwordToCheck) {
        // check if user with entered email exist
        List<String> passwordWithGivenEmail = entityManager.createQuery("SELECT u.password FROM User u WHERE u.email = :email").setParameter("email", userEmail).getResultList();
        if(passwordWithGivenEmail.size() > 0){
            // check if encoded passwords are the same
            String correctPasswordEncoded = passwordWithGivenEmail.get(0);
            // encoder generates different hash for  the same input so String.equals() wouldn't work properly
            return encoder.matches(passwordToCheck, correctPasswordEncoded);
        } else {
            // user with that email doesn't exist
            return false;
        }
    }
}
