package calculator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by me on 8/22/2017.
 * This is the class to handle all "simple" expressions, i.e. it does not contain any special keywords. This is processed
 * using a linked list in stack orientation. I decided to on this because ut us equally efficient and I just didn't want to
 * use a stack.This class will process +, -, ^, %, *. I will add a subclass or innerclass for processing trig functions
 * (this however might be easier to ad to the advances class instead since there is already a way for processing certain keywords).
 * Might also add word equivs plus the option to go between fraction and decimal, as well as add a negative processing,
 * fractional exponents, answer button, irrationals, and a pi button. I must sanitize the input, this is next top priority after
 * getting the parenthesis working; this should be added to the input parsing class.
 */

public class Simple {

    private LinkedList userInput;
    private String calculation;

    //better variable name than e
    public Simple(String e) {
        calculation = " ";
        userInput = new LinkedList(Arrays.asList(e.split(" ")));
    }


    //should only be doing one thing
    public String getCalculation() {
        double holder = expressionEval();
        return calculation = String.valueOf(holder);
    }


    /**
     * This is where all of the simple expressions will be handled, this method has one auxiliary method to do the simple
     * symbol operations: +, *, (, ), ^, %, !. This method breaks up the expression into either operands and operators, this
     * is implemented with a linked list in a stack style, it might have been easier and cleaner to have just used java's
     * stack but they are the same in terms of efficiency and speed so that's the only trade off really. The order of operations
     * is built into the program via the stack and when the expressions are evaluated.
     */
    private double expressionEval() {

        LinkedList<Double> operands = new LinkedList<>();//holds the operands
        LinkedList<String> operators = new LinkedList<>();//holds operators from expression

        int inputSize = userInput.size();  //size of the expression entered by the user
        String equationPosition = "";

        if(inputSize == 1)
            return (Double)userInput.getFirst();

        //going through the userInput Linked list
        for (int i = 0; i < inputSize; i++) {
            if (i != inputSize) {
                //this takes the individual character that is  currently being processed; this is simply a place holder
                equationPosition = (String) userInput.pollFirst();
            }


            //when the current element being observed from the evaluation is a + or - symbol
            if (equationPosition.equals("+") || equationPosition.equals("-")) {
                // evaluate anything on the stack with higher precedence first, i.e. !, *, ^, %, /, and any other symbol of the
                // same precedence that was already on the stack
                HashSet<String> a = new HashSet<>(Arrays.asList("+", "-", "*", "/", "^", "!", "%"));
                while(!(operators.isEmpty()) && a.contains(operators.getFirst())){
                    processExp(operands, operators);
                }
                operators.addFirst(equationPosition);

                //when the current element being observed from the evaluation is a *, %, or / symbol
            } else if (equationPosition.equals("*") || equationPosition.equals("/")
                    || equationPosition.equals("%")) {
                HashSet<String> b = new HashSet<>(Arrays.asList("*", "/", "^", "%"));
                while(!(operators.isEmpty()) && b.contains(operators.getFirst())) {
                    processExp(operands, operators);
                }
                operators.addFirst(equationPosition);

            } else if (equationPosition.equals("!")) {
                operators.addFirst(equationPosition);

            } else if (equationPosition.equals("^")) {
                operators.addFirst(equationPosition);

            } else if (equationPosition.equals("(")) {
                if(operands.size() != operators.size()){
                    operators.addFirst("*");
//                    if(operators.getFirst() == "-"){
//                        operands.addFirst(1.0);
//                    }
                }
                operators.addFirst("(");

            } else if (equationPosition.equals(")")) {
                //keeps evaluating what is on the stack until the opening parenthesis is found, concluding the parenthesisized
                //internal expression
                while (!operators.getFirst().equals("(") && !operators.isEmpty() && !operands.isEmpty()) {

                    processExp(operands, operators);
                }
                operators.removeFirst();

                //if no recognized symbol it is added to the operand stack as a double
            } else {
                operands.addFirst(Double.parseDouble(equationPosition));
            }
        }

        //empty whatever is left on the stack
        while (!(operators.isEmpty())) {
            processExp(operands, operators);
        }
        //returning the top element on the stack, which if everything was properly evaluated, should be the only element left
        return operands.getFirst();
    }


    /**
     * The auxiliary method to help with the evaluation of mathematical operators, typically deletes two elements off of the
     * operands stack and one off of the operator stack, unless the operator is the factorial symbol, in which case only one
     * operand needs to be popped off of the stack. This method also will add one double value onto the global operand stack,
     * this will be the calculated problem. needs to explain what it does not how it works
     */
    private void processExp(LinkedList operands, LinkedList operators) {

        String operator = (String) operators.removeFirst();
        double operandTwo = 0;
        double operandOne = 0;
        //for the case of the operator being a !, where only one element needs to be popped off
        Integer factorialOperator = 0;
        if (operator.equals("!")) {
            Double holder = (double)operands.removeFirst();
            factorialOperator = holder.intValue();
        } else {
            operandTwo = (Double) operands.removeFirst();
            operandOne = (Double) operands.removeFirst();
        }
        switch (operator) {
            case "+":
                operands.addFirst(operandOne + operandTwo);
                break;
            case "-":
                operands.addFirst(operandOne - operandTwo);
                break;
            case "/":
                if(operandTwo != 0) {
                    operands.addFirst(operandOne / operandTwo);
                }
                break;
            case "*":
                operands.addFirst(operandOne * operandTwo);
                break;
            case "%":
                operands.addFirst(operandOne % operandTwo);
                break;
            case "^":
                operands.addFirst(Math.pow(operandOne, operandTwo));
                break;
            case "!":
                operands.addFirst((double)factorial(factorialOperator));
        }
    }


    //Auxiliary method for the processExp method, for finding the factorial, doesn't need to be a double
    //needs to handle negatives
    private int factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }
}

