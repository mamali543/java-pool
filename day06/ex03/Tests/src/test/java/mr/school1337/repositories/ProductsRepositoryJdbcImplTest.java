package test.java.mr.school1337.repositories;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import main.java.mr.school1337.models.Product;
import main.java.mr.school1337.repositories.ProductsRepositorieJdbcImpl;

import static org.junit.jupiter.api.Assertions.*;



public class ProductsRepositoryJdbcImplTest {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = Arrays.asList(
        new Product(0L, "aaa", 10.00),
        new Product(1L, "bbb", 20.00),
        new Product(2L, "ccc", 30.00),
        new Product(3L, "ddd", 40.00),
        new Product(4L, "eee", 50.00)
        
    );
    final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(3L, "ddd", 40.00);
    final Product EXPECTED_UPDATED_PRODUCT = new Product(1L, "rtx", 70.00);

    private DataSource dataSource;
    ProductsRepositorieJdbcImpl productsRepositorieJdbcImpl = new ProductsRepositorieJdbcImpl(dataSource);

    @BeforeEach
    public void init(){
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.HSQL) // HSQLDB embedded database type
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        this.dataSource = db;
        productsRepositorieJdbcImpl = new ProductsRepositorieJdbcImpl(dataSource);
    }

    @Test
    public void testFindAll() {
        System.out.println(ANSI_RED+"\nfindAll Test Method:\n "+ANSI_RESET);
        System.out.println(productsRepositorieJdbcImpl.findAll());
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, productsRepositorieJdbcImpl.findAll());
    }


    @Test
    public void testFindById() {
        System.out.println(ANSI_RED+"\nfindById Test Method:\n "+ANSI_RESET);

        Long id = 3L;
        assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, productsRepositorieJdbcImpl.findById(id).get());
    }


    @Test
    public void checkFindByIdException(){
        System.out.println(ANSI_RED+"\nfindById exception Test Method:\n "+ANSI_RESET);
        assertThrows(RuntimeException.class, () -> productsRepositorieJdbcImpl.findById(25L));
    }

    @Test
    public void testUpdate() throws SQLException {
        System.out.println(ANSI_RED+"\n update Test Method:\n "+ANSI_RESET);

        Product expectedProduct = productsRepositorieJdbcImpl.findById(EXPECTED_UPDATED_PRODUCT.getId()).orElse(null);
        expectedProduct.setPrice(EXPECTED_UPDATED_PRODUCT.getPrice());
        expectedProduct.setName(EXPECTED_UPDATED_PRODUCT.getName());
        productsRepositorieJdbcImpl.update(expectedProduct);
        Product result = productsRepositorieJdbcImpl.findById(expectedProduct.getId()).orElse(null);

        System.out.println(result + "\n" + EXPECTED_UPDATED_PRODUCT);
        assertNotNull(result);
        assertEquals(result, EXPECTED_UPDATED_PRODUCT);
    }

    @Test
    public void testSave(){
        System.out.println(ANSI_RED+"\n save Test Method:\n "+ANSI_RESET);

        Integer countBefore = productsRepositorieJdbcImpl.findAll().size();
        productsRepositorieJdbcImpl.save(new Product("name6", 1210.0));
        System.out.println(ANSI_BLUE+"test save method: \n"+productsRepositorieJdbcImpl.findAll()+ANSI_RESET);
        assertEquals(countBefore, productsRepositorieJdbcImpl.findAll().size() - 1);
    }

    @Test
    public void testDelete(){
        System.out.println(ANSI_RED+"\n delete Test Method:\n "+ANSI_RESET);

        Integer countBefore = productsRepositorieJdbcImpl.findAll().size();
        System.out.println("countBefore: "+countBefore);
        productsRepositorieJdbcImpl.delete(4L);
        System.out.println(productsRepositorieJdbcImpl.findAll());
        assertEquals(countBefore-1, productsRepositorieJdbcImpl.findAll().size());
    }

    @AfterEach
    public void end() {
        ((EmbeddedDatabase) dataSource).shutdown();
    }
}
