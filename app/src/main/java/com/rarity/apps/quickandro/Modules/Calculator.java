package com.rarity.apps.quickandro.Modules;

import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import com.rarity.apps.quickandro.MainActivity;
import com.rarity.apps.quickandro.RunBackground;
import com.rarity.apps.quickandro.RunBot;

import java.util.Stack;

public class Calculator {

    private RunBot bot;
    private Context context;
    private String ans;

    public Calculator(Context context){
        this.context = context;
        try {
            this.bot = (MainActivity) context;
        }catch(ClassCastException e){
            this.bot = (RunBackground) context;
        }
    }

    public void calculate(){
        SpeechToText tempSTT = new SpeechToText(context, new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                String result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                bot.updateLayout(" " + result);
                ans = calculate(result);
                bot.updateLayout(ans);
            }

            @Override
            public void onReadyForSpeech(Bundle params) {}
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onRmsChanged(float rmsdB) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onEndOfSpeech() {}
            @Override
            public void onError(int error) {}
            @Override
            public void onPartialResults(Bundle partialResults) {}
            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        bot.updateLayout("Which mathematical expression would you like to calculate?");
        sleep();
        tempSTT.listen();

    }

    public String calculate(String expression) {
        expression = convertToValidInfix(expression);

        try {
            Stack<Double> stack = new Stack<Double>();
            String postfix[] = infixToPostfix(expression).split(" ");

            for (int i = 0; i < postfix.length; i++) {

                if (!isOperator(postfix[i].charAt(0)))
                    stack.push(Double.parseDouble(postfix[i]));
                else
                    stack.push(operation(stack.pop(), stack.pop(), postfix[i].charAt(0)));
            }

            return "The answer is " + stack.pop()+"";
        } catch (Exception e) {
            return "Invalid expression..";
        }
    }

    private String convertToValidInfix(String infix){
        infix = infix.replaceAll("plus", "+");
        infix = infix.replaceAll("minus", "-");
        infix = infix.replaceAll("divided by", "/");
        infix = infix.replaceAll("multiplied by", "*");
        infix = infix.replaceAll("into", "*");
        infix = infix.replaceAll("by", "/");
        infix = infix.replaceAll("x", "*");
        infix = infix.replaceAll("divide", "/");
        infix = infix.replaceAll("multiply", "*");
        infix = infix.replaceAll(" ", "");
        return infix;
    }

    private String infixToPostfix(String infix) {

        Stack<Character> stack = new Stack<Character>();
        String postfix = "";

        for (int i = 0; i < infix.length(); i++) {

            char temp = infix.charAt(i);

            if (isOperator(temp)) {

                postfix += " ";

                if (stack.isEmpty() || priority(stack.peek()) < priority(temp)) {
                    stack.push(temp);
                } else {
                    while (!stack.isEmpty())
                        postfix += stack.pop() + " ";
                    stack.push(temp);
                }

            }
            else {
                postfix += temp;
            }
        }

        while (!stack.isEmpty())
            postfix += " " + stack.pop();

        return postfix;
    }

    private boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/')
            return true;
        else
            return false;
    }

    private int priority(char c) {
        return (c == '*' || c == '/') ? 1 : 0;
    }

    private double operation(double y, double x, char operator) {
        switch (operator) {
            case ('*'):
                return x * y;
            case ('/'):
                return x / y;
            case ('+'):
                return x + y;
            case ('-'):
                return x - y;
        }
        return 0;
    }

    private void sleep(){
        try {
            Thread.sleep(2700);
        } catch (InterruptedException e) {}
    }
}
