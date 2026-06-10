package template;

import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import template.infrastructure.adapter.persistence.ItemRepository;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected ItemRepository repository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
        resetSequence();
    }

    private void resetSequence() {
        var transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            long nextSeqValue = repository.count() + 1;
            entityManager.createNativeQuery(String.format("ALTER SEQUENCE ITEM_SEQ RESTART WITH %d", nextSeqValue)).executeUpdate();
        });
    }

}