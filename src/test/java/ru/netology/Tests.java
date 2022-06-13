package ru.netology;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import java.sql.DriverManager;

class Tests {
//    @AfterAll
//    void deleteDb() throws SQLException {
//        String codeSQL = "DROP TABLE IF EXISTS cards; DROP TABLE IF EXISTS auth_codes;" +
//                " DROP TABLE IF EXISTS card_transactions; DROP TABLE IF EXISTS users;";
//        try (
//                var conn = DriverManager.getConnection(
//                        "jdbc:mysql://localhost:3307/app", "app", "pass"
//                );
//                var stmt = conn.createStatement();
//        ) {
//            stmt.execute(codeSQL);
//
//        }
//
//    }

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
    @Test
    void negativePath(){
        $("[data-test-id='password'] .input__control").setValue("111");
        for (int i = 0; i < 3; i++)
            $("[data-test-id='action-login']").click();
        //все работает, а не должно

    }



}