package calculator;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import java.util.regex.*;

/**
 * Created by Elijah Bloom on 8/17/2017. This class determines how the user's input should be handeled. It first takes the input as
 * a String and splits that  into individual token each of which is added to a list. These tokens are then cross checked
 * against a file of possible keywords, determining which super class definition the equation type belongs to(ie, simple,
 * advances, spacial characters, prob stats, etc.).
 * Note: comments go above the relevant code
 */
public class InputParser {

    private String calculation; //the over all calculation that is to be returned
    private List listInput; //A list containing a parses
    private String userInput;
    private HashSet advancedKeywords = new HashSet();



    public InputParser() {
        listInput = new ArrayList();
        calculation = " ";
        userInput = "";
    }


    /**
     * @param userInput This is for  a user input is defined. The mathematical expression that is entered by the user is split by the white
     *                  space in-between each individual component. Each piece is then stored in the listInput global variable. This is done
     *                  so that the calculator can decide the best route for solving the entered expression.
     */
    public InputParser(String userInput) {

        this.userInput = addWhiteSpace(userInput);
        this.listInput = Arrays.asList(this.userInput.split(" "));
        this.calculation = "0";
    }

    public void setUserInput(String updatedInput){
        this.userInput = addWhiteSpace(updatedInput);
    }
    public String getUserInput(){
        return this.userInput;
    }

    public String getCalculation() {
        return this.calculation;
    }


    /**
     * @return
     * This is the method that provides the main functionality of the expression parsing process. The listed input is cross
     *                     checked against a file to see if it contains any keywords that would make the expression of type advanced(i.e.
     *                     "integral of", "derivative of", etc...); anything that does not contain these keywords  will be deemed as being a
     *                     "simple expression. As of right now this is done via a text file that just contains keywords, however I am not sure
     *                     if this is the best/most efficient route, I may consider converting this over to a SQL database or something of the sort.
     *                     The simple expressions will have all the functionality of a common scientific calculator.
     */
    public void setCalculation() {

        //List advancedKeywords = new ArrayList();//a list holding the keywords that are seperated out from the user input
        BufferedReader listOfKeywords; //this will bring in the fixed file of "advanced" keywords
        int keyWordFlag = 0;


        try {

            //bringing in the list of keywords from a pre-made file in the projects directory
            listOfKeywords = new BufferedReader(new FileReader("src/sample/advancedKeywords"));

            for (String line = listOfKeywords.readLine(); line != null; line = listOfKeywords.readLine()) {
                if (this.listInput.contains(line)) {
                    keyWordFlag++;//if this flag is tripped, then the calculator knows to move to the advanced calculation class
                    advancedKeywords.add(line);//adding the keywords that matched the file keywords and that were extracted from user-input
                }
            }
            //closing the file stream
            listOfKeywords.close();
        } catch (Exception e) {
            System.err.println("Error: Target File Cannot Be Read");
        }

        //this is where the input will be sent tobe processed for  advanced keywords
        if (keyWordFlag > 0) {
            //

        } else if (listInput.size() == 1) {
            this.calculation = this.userInput;
        } else {
            Simple simpleCalculation = new Simple(this.userInput);

            //calling the simple class to find the desired calculation/ the toString should be taken care of the simple class
            this.calculation = simpleCalculation.getCalculation().toString();//this should ultimately be the global calculation variable
            //so that the getters/setters can be used by the gui in order to maintain proper encapsulation
        }
    }


    private String addWhiteSpace(String userInput) {
        HashSet<Character> mathematicalSymbols = new HashSet<>(Arrays.asList('+', '-', '*', '/', '(', ')', '^', '!', '%'));
        String simplifiedUserInput = convertSimpleKeywords(userInput);
        boolean containsMathematicalSymbol = false;

        for (int i = 0; i < simplifiedUserInput.length(); i++) {
            if (mathematicalSymbols.contains(simplifiedUserInput.charAt(i)) && i != 0) {
                containsMathematicalSymbol = true;
                break;
            }
        }
        if (!containsMathematicalSymbol) {
            return simplifiedUserInput;
        }

        //string that will have exactly one whitespace in between operators and operands
        String seperatedInput = "";

        //make sure all whitespace is removed from the user input since a user could use an inconsistent formatting of their input
        String noSpaceInput = simplifiedUserInput.replaceAll("\\s *", "");

        char previousElement = noSpaceInput.charAt(0);

        for (char a : noSpaceInput.toCharArray()) {
            if (mathematicalSymbols.contains(a)) {
                if (mathematicalSymbols.contains(previousElement) && a != '-') {
                    seperatedInput += a + " ";
                } else if ((a == '-') && (mathematicalSymbols.contains(previousElement))) {
                    seperatedInput += a;
                } else {
                    seperatedInput += " " + a + " ";
                }
            } else {
                seperatedInput += a;
            }
            previousElement = a;
        }
        return seperatedInput;
    }


    private void sanitizeInput(String textForSpellcheck) {
       // Spellcheck checkedInput = new Spellcheck(textForSpellcheck);
    }

    private String convertSimpleKeywords(String userInput) {

        StringBuilder userBuilder = new StringBuilder(userInput);
        ArrayList<Integer> indexList = new ArrayList<>();
        HashSet<Character> letters = new HashSet<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w'));
        HashSet<Character> digits = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'));
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<String> operands = new ArrayList<>();
        String operator = "";
        String operand = "";
        int previousIndex = 0;
        boolean first = true;
        boolean inOperator = false;
        for (int i = 0; i < userBuilder.length(); i++) {
            if (letters.contains(userBuilder.charAt(i))) {
                if (first) {
                    indexList.add(i);
                    first = false;
                }
                if (userBuilder.charAt(i) == 'p') {
                    userBuilder.replace(i, i + 2, "3.14159265");
                    return userBuilder.toString();
                }
                operator += userBuilder.charAt(i);
            } else if (userBuilder.charAt(i) == '(' && letters.contains(userBuilder.charAt(previousIndex))) {
                first = true;
                inOperator = true;
                //if(allowedOperators.contains(operator)) {
                operators.add(operator);
                //}
                operator = "";
            } else if (digits.contains(userBuilder.charAt(i)) && inOperator) {
                operand += userInput.charAt(i);
            } else if (userBuilder.charAt(i) == ')' && inOperator) {
                indexList.add(i);
                inOperator = false;
                operands.add(operand);
                operand = "";
            }
            previousIndex = i;
        }

        int i = 0;
        int a = 0;
        while (i < indexList.size()) {
            int indexStart = indexList.get(i);
            operand = operands.get(a);
            operator = operators.get(a);
            int indexEnd = indexList.get(i += 1);
            userBuilder.replace(indexStart, indexEnd + 1, keywordNumberConversion(operand, operator));
            i++;
            a++;
        }
        return userBuilder.toString();
    }

    private String keywordNumberConversion(String operand, String operator) {

        double operandNumber = Double.parseDouble(operand);
        double degrees = Math.toRadians(operandNumber);
        double calculation = 0;

        switch (operator) {
            case ("cos"):
                calculation = Math.cos(degrees);
                calculation = format(calculation);
                break;
            case ("sin"):
                calculation = Math.sin(degrees);
                calculation = format(calculation);
                break;
            case ("tan"):
                calculation = Math.tan(degrees);
                calculation = format(calculation);
                break;
            case ("log"):
                calculation = Math.log10(operandNumber);
                break;
            case ("ln"):
                calculation = Math.log(operandNumber);
                break;
            case ("arcsin"):
                calculation = Math.asin(degrees);
                break;
            case ("arccos"):
                calculation = Math.acos(degrees);
                break;
            case ("arctan"):
                calculation = Math.atan(degrees);
                break;
            case ("sqrt"):
                calculation = Math.sqrt(operandNumber);
                break;
        }
        return Double.toString(calculation);
    }

    private double format(double value) {
        return (double) Math.round(value * 1000000) / 1000000;
    }
}
