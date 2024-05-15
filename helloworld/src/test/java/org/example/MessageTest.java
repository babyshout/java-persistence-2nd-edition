package org.example;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldJPATest {

    @Test
    public void storeLoadMessage() {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("ch02");

        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

            Message message = new Message();
            message.setText("Hello World!!!");

            em.persist(message);

            em.getTransaction().commit();
            // INSERT into MESSAGE (ID, TEXT) value (1, 'Hello World!!!')

            em.getTransaction().begin();

            List<Message> messageList =
                    em.createQuery("select m from Message m", Message.class)
                            .getResultList();
            // SELECT * from MESSAGE

            assertEquals(messageList.get(0).getText(), "Hello World!!!");

            messageList.get(messageList.size() - 1)
                    .setText("Hello World from JPA!");

            em.getTransaction().commit();
            // UPDATE MESSAGE set TEXT = 'Hello World from JPA!' where ID = 1

            assertAll(
                    () -> assertEquals(1, messageList.size()),
                    () -> assertEquals("Hello World from JPA!",
                            messageList.get(0).getText())
            );

            em.close();
        } finally {
            entityManagerFactory.close();
        }
    }

}