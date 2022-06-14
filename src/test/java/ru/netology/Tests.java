package ru.netology;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.*;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import java.sql.DriverManager;


class Tests {
    @AfterAll
    public static void deleteDb() throws SQLException {
        String delCards = "TRUNCATE cards;";
        String delCardTrans = "TRUNCATE card_transactions;";
        String delAuthCodes = "TRUNCATE auth_codes;";
        String delUsers = "DELETE FROM users;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3307/app", "app", "pass"
                );
                var stmt = conn.createStatement();
        ) {
            stmt.executeUpdate(delCards);
            stmt.executeUpdate(delCardTrans);
            stmt.executeUpdate(delAuthCodes);
            stmt.executeUpdate(delUsers);


        }

    }

    @BeforeEach
    void setUp(){
        open("http://localhost:9999");
        $("[data-test-id='login'] .input__control").setValue("Vasya");
    }

    @Test
    void happyPath() throws SQLException {

        $("[data-test-id='password'] .input__control").setValue("qwerty123");
        $("[data-test-id='action-login']").click();
        String codeSQL = "SELECT code FROM auth_codes WHERE created = CURRENT_TIME();";
        var connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3307/app", "app", "pass"
                );
        Statement stmt = connection.createStatement();

        var secretCode = stmt.executeQuery(codeSQL);
        secretCode.next();
        String code = secretCode.getString("code");
        System.out.println(code);
        connection.close();

        $("[data-test-id='code'] .input__control").setValue(code);
        $("[data-test-id='action-verify']").click();
        $("[data-test-id='dashboard']").shouldBe(visible);


    }
//    @Test
//    void negativePath(){
//        $("[data-test-id='password'] .input__control").setValue("111");
//        for (int i = 0; i < 3; i++)
//            $("[data-test-id='action-login']").click();
//
//
//    }




}