package testeSistema;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class TesteSistemaBase extends FecharJanelaAutomatico{
    public final InputStream originalSystemIn = System.in;
    public final PrintStream originalSystemOut = System.out;

    public ByteArrayOutputStream outputStream;

    public void deleteUser(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU_H2");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        String jpql = "DELETE FROM Usuario u WHERE u.login = :login";

        em.createQuery(jpql).setParameter("login","user2").executeUpdate();
        em.createQuery(jpql).setParameter("login","user3").executeUpdate();

        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        deleteUser();
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
        deleteUser();
    }

    public void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}
