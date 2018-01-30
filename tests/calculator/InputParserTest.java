package calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {
    InputParser inputParserTest = new InputParser();


    @Test
    void getUserInput() {
        InputParser parseTest = new InputParser("1+2");
        assertEquals("1 + 2",parseTest.getUserInput());
    }

    @Test
    void setUserInput() {
        this.inputParserTest.setUserInput("2+2");
        assertEquals("2 + 2",inputParserTest.getUserInput());
    }

    @Test
    void getCalculation() {
        inputParserTest.setUserInput("1 + 2");
        inputParserTest.setCalculation();
        assertEquals("3.0", inputParserTest.getCalculation());
    }

    @Test
    void testSpacing() {
        inputParserTest.setUserInput("1 +       2");
        inputParserTest.setCalculation();
        assertEquals("1 + 2", inputParserTest.getUserInput());
    }

    @Test
    void testSin() {
        inputParserTest.setUserInput("sin(0)");
        assertEquals("0.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("sin(90)");
        assertEquals("1.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("sin(180)");
        assertEquals("0.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("sin(270)");
        assertEquals("-1.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("sin(360)");
        assertEquals("0.0", inputParserTest.getUserInput());
    }

    @Test
    void testCos() {
        inputParserTest.setUserInput("cos(0)");
        assertEquals("1.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("cos(90)");
        assertEquals("0.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("cos(180)");
        assertEquals("-1.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("cos(270)");
        assertEquals("0.0", inputParserTest.getUserInput());
        inputParserTest.setUserInput("cos(360)");
        assertEquals("1.0", inputParserTest.getUserInput());
    }
}