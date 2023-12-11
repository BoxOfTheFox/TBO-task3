package com.example.thymeleaf.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.thymeleaf.util.StringUtils.repeat;

class StudentTest {

    private Student student;

    @BeforeEach
    void createStudent() {
        student = new Student();
    }

    @Test
    void correctInputNameTest() {
        List<String> inputs = Arrays.asList(
                "Name",
                repeat("X", 64)

        );
        inputs.forEach(value -> assertDoesNotThrow(() -> student.setName(value)));
    }

    @Test
    void correctInputEmailTest() {
        List<String> inputs = Arrays.asList(
                "correct@email.test",
                "Fistname.Lastname.student@pw.edu.pl",
                "12345678@pw.edu.pl",
                repeat("X", 64) + "@pw.edu.pl"

        );
        inputs.forEach(value -> assertDoesNotThrow(() -> student.setEmail(value)));
    }

    @Test
    void correctInputBirthdayTest() {
        assertDoesNotThrow(() -> student.setBirthday(LocalDate.now()));
    }

    @Test
    void incorrectInputNameTest() {
        List<String> inputs = Arrays.asList(
                null,
                "null",
                "nil",
                "0",
                " ",
                "\t",
                "\n",
                "--",
                "=0@$*^%;<!->,;\\()&#\""
        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setName(value)));
    }

    @Test
    void incorrectInputEmailTest() {
        List<String> inputs = Arrays.asList(
                null,
                "null",
                "nil",
                "0",
                " ",
                "\t",
                "\n",
                "john.doe\n@hospital.com",
                "   @hospital.com",
                "%20@@hospital.com",
                "john.d%20e@hospital.com",
                "john..doe@hospital.com",
                "--",
                "e x a mple@hospital . c o m",
                "=0@$*^%;<!->,;\\()&#\""
        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setEmail(value)));
    }

    @Test
    void incorrectInputBirthdayTest() {
        assertThrows(RuntimeException.class, () -> student.setBirthday(null));
    }

    @Test
    void incorrectInputNameTest_XSS() {
        List<String> inputs = Arrays.asList(
                "\"-prompt(8)-\"",
                "'-prompt(8)-'",
                "<img/src/onerror=prompt(8)>",
                "<script\\x20type=\"text/javascript\">javascript:alert(1);</script>",
                "<`\"><\\x3Cscript>javascript:alert(1)</script>",
                "<script src=1 href=1 onerror=\"javascript:alert(1)\"></script>"
        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setName(value)));
    }

    @Test
    void incorrectInputEmailTest_XSS() {
        List<String> inputs = Arrays.asList(
                "\"-prompt(8)-\"",
                "'-prompt(8)-'",
                "<img/src/onerror=prompt(8)>",
                "<script\\x20type=\"text/javascript\">javascript:alert(1);</script>",
                "<`\"><\\x3Cscript>javascript:alert(1)</script>",
                "<script src=1 href=1 onerror=\"javascript:alert(1)\"></script>"
        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setEmail(value)));
    }

    @Test
    void incorrectInputNameTest_SQLi() {
        List<String> inputs = Arrays.asList(
                "-- or # ",
                "\" OR 1 = 1 -- -",
                "'''''''''''''UNION SELECT '2",
                "1' ORDER BY 1--+",
                "' UNION SELECT sum(columnname ) from tablename --",
                ",(select * from(select(sleep(10)))a)"
        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setName(value)));
    }

    @Test
    void incorrectInputEmailTest_SQLi() {
        List<String> inputs = Arrays.asList(
                "-- or # ",
                "\" OR 1 = 1 -- -",
                "'''''''''''''UNION SELECT '2",
                "1' ORDER BY 1--+",
                "' UNION SELECT sum(columnname ) from tablename --",
                ",(select * from(select(sleep(10)))a)"
        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setEmail(value)));
    }

    @Test
    void extremeInputNameTest() {
        List<String> inputs = Arrays.asList(
                repeat("X", 10000),
                repeat("X", 100000),
                repeat("X", 1000000),
                repeat("X", 2000000),
                repeat("X", 4000000)

        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setName(value)));
    }

    @Test
    void extremeInputEmailTest() {
        List<String> inputs = Arrays.asList(
                repeat("X", 10000),
                repeat("X", 100000),
                repeat("X", 1000000),
                repeat("X", 2000000),
                repeat("X", 4000000)

        );
        inputs.forEach(value -> assertThrows(RuntimeException.class, () -> student.setEmail(value)));
    }

}