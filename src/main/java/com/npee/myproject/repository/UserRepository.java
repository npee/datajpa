package com.npee.myproject.repository;

import com.npee.myproject.advice.exception.CustomUserNotExistsException;
import com.npee.myproject.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepository {

    private final EntityManager em;

    public List<User> findAll() {
        return em.createQuery(
                "select u from User u", User.class
        ).getResultList();
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User save(User user) {
        if (user.getId() == null) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        User user = findById(id);
        if (user == null) {
            throw new CustomUserNotExistsException();
        }
        em.remove(user);
    }

}
