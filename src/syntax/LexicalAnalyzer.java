package syntax;

import java.io.*;
import java.util.Scanner;

public class LexicalAnalyzer {
    //保留字
    public static String[][] keyWord = new String[53][];
    //运算符
    public static String[][] operator = new String[34][];
    //界符
    public static String[][] delimiter = new String[12][];

    public static String[][] constact = new String[100][];
    static int p;
    static Integer q = 0;

    static StringBuffer outputBuffer = new StringBuffer();

    static {
        keyWord[0] = new String[]{"if", "1"};
        keyWord[1] = new String[]{"else", "2"};
        keyWord[2] = new String[]{"while", "3"};
        keyWord[3] = new String[]{"signed", "4"};
        keyWord[4] = new String[]{"throw", "5"};
        keyWord[5] = new String[]{"union", "6"};
        keyWord[6] = new String[]{"this", "7"};
        keyWord[7] = new String[]{"int", "8"};
        keyWord[8] = new String[]{"char", "9"};
        keyWord[9] = new String[]{"double", "10"};
        keyWord[10] = new String[]{"unsigned", "11"};
        keyWord[11] = new String[]{"const", "12"};
        keyWord[12] = new String[]{"goto", "13"};
        keyWord[13] = new String[]{"for", "14"};
        keyWord[14] = new String[]{"float", "15"};
        keyWord[15] = new String[]{"break", "16"};
        keyWord[16] = new String[]{"auto", "17"};
        keyWord[17] = new String[]{"class", "18"};
        keyWord[18] = new String[]{"operator", "19"};
        keyWord[19] = new String[]{"private", "20"};
        keyWord[20] = new String[]{"asm", "21"};
        keyWord[21] = new String[]{"catch", "22"};
        keyWord[22] = new String[]{"public", "23"};
        keyWord[23] = new String[]{"struct", "24"};
        keyWord[24] = new String[]{"using", "25"};
        keyWord[25] = new String[]{"case", "26"};
        keyWord[26] = new String[]{"do", "27"};
        keyWord[27] = new String[]{"long", "28"};
        keyWord[28] = new String[]{"typedef", "29"};
        keyWord[29] = new String[]{"static", "30"};
        keyWord[30] = new String[]{"friend", "31"};
        keyWord[31] = new String[]{"template", "32"};
        keyWord[32] = new String[]{"default", "33"};
        keyWord[33] = new String[]{"new", "34"};
        keyWord[34] = new String[]{"void", "35"};
        keyWord[35] = new String[]{"register", "36"};
        keyWord[36] = new String[]{"extern", "37"};
        keyWord[37] = new String[]{"return", "38"};
        keyWord[38] = new String[]{"enum", "39"};
        keyWord[39] = new String[]{"inline", "40"};
        keyWord[40] = new String[]{"try", "41"};
        keyWord[41] = new String[]{"short", "42"};
        keyWord[42] = new String[]{"continue", "43"};
        keyWord[43] = new String[]{"sizeof", "44"};
        keyWord[44] = new String[]{"switch", "45"};
        keyWord[45] = new String[]{"protected", "46"};
        keyWord[46] = new String[]{"namespace", "47"};
        keyWord[47] = new String[]{"delete", "48"};
        keyWord[48] = new String[]{"volatile", "49"};
        keyWord[49] = new String[]{"include", "50"};
        keyWord[50] = new String[]{"virtual", "51"};
        keyWord[51] = new String[]{"bool", "52"};
        keyWord[52] = new String[]{"main", "53"};
    }

    static {
        operator[0] = new String[]{"+", "201"};
        operator[1] = new String[]{"-", "202"};
        operator[2] = new String[]{"*", "203"};
        operator[3] = new String[]{"/", "204"};
        operator[4] = new String[]{"%", "205"};
        operator[5] = new String[]{"++", "206"};
        operator[6] = new String[]{"--", "207"};
        operator[7] = new String[]{"-=", "208"};
        operator[8] = new String[]{"*=", "209"};
        operator[9] = new String[]{"/=", "210"};
        operator[10] = new String[]{"&", "211"};
        operator[11] = new String[]{"|", "212"};
        operator[12] = new String[]{"^", "213"};
        operator[13] = new String[]{"~", "214"};
        operator[14] = new String[]{"<<", "215"};
        operator[15] = new String[]{">>", "216"};
        operator[16] = new String[]{">>>", "217"};
        operator[17] = new String[]{"==", "218"};
        operator[18] = new String[]{"!=", "219"};
        operator[19] = new String[]{">", "220"};
        operator[20] = new String[]{"<", "221"};
        operator[21] = new String[]{"=", "222"};
        operator[22] = new String[]{">=", "223"};
        operator[23] = new String[]{"<=", "224"};
        operator[24] = new String[]{"&&", "225"};
        operator[25] = new String[]{"||", "226"};
        operator[26] = new String[]{"!", "227"};
        operator[27] = new String[]{".", "228"};
        operator[28] = new String[]{"+=", "229"};
        operator[29] = new String[]{"%=", "230"};
        operator[30] = new String[]{"?", "231"};
        operator[31] = new String[]{":", "232"};
        operator[32] = new String[]{",", "233"};
        operator[33] = new String[]{"//", "234"};
    }

    static {
        delimiter[0] = new String[]{"(", "301"};
        delimiter[1] = new String[]{")", "302"};
        delimiter[2] = new String[]{"{", "303"};
        delimiter[3] = new String[]{"}", "304"};
        delimiter[4] = new String[]{"[", "305"};
        delimiter[5] = new String[]{"/*", "306"};
        delimiter[6] = new String[]{"'", "307"};
        delimiter[7] = new String[]{"#", "308"};
        delimiter[8] = new String[]{";", "309"};
        delimiter[9] = new String[]{"]", "310"};
        delimiter[10] = new String[]{"*/", "311"};
        delimiter[11] = new String[]{"\"", "312"};
    }

    public static StringBuffer getInputText(String str,String outputFilePath) {
        int lines = 1;
        try {
            analyze(str);
            save(outputFilePath);
            return outputBuffer;
        } catch (Exception e) {
            e.printStackTrace();
            return new StringBuffer("词法分析错误");
        }
    }

    public static void save(String outputFilePath) {
        File file = new File(outputFilePath);
        if (file.exists()) {
            file.delete();
        }
        PrintWriter writer = null;
        try {
            file.createNewFile();
            writer = new PrintWriter(new FileOutputStream(file));
            writer.write(outputBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    public static boolean analyze(String str) {
        try {
            p = 0;
            char ch;
            str = str.trim();
            while (p < str.length()) {
                ch = str.charAt(p);
                if (Character.isLetter(ch)) {
                    letterCheck(str);
                } else if (Character.isDigit(ch)) {
                    digitCheck(str);
                } else if (ch == '+' || ch == '-') {
                    plusAndLess(str);
                } else if (ch == '=') {
                    checkEqualAndNonOperator(str);
                } else if (ch == '&') {
                    checkAndOperator(str);
                } else if (ch == '|') {
                    checkOROperator(str);
                } else if (ch == '/') {
                    checkExceptOperator(str);
                } else if (ch == '*') {
                    checkMultiplyOperator(str);
                } else if (ch == '!') {
                    checkEqualAndNonOperator(str);
                } else if (ch == '>') {
                    checkGreaterOperator(str);
                } else if (ch == '<') {
                    checkLessOperator(str);
                } else if (ch == '^' || ch == '~' || ch == '%' || ch == '.' || ch == '?' || ch == ',' || ch == ':') {
                    checkOperatorSingle(str);
                } else if (ch == '(' || ch == ')' || ch == '{' || ch == '}' || ch == '#' || ch == ';' || ch == '\'' || ch == '\"' || ch == '[' || ch == ']') {
                    checkDelimiter(str);
                } else if (ch == ' ') {

                } else {

                }

                p++;

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void checkEqualAndNonOperator(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch = str.charAt(p);
        if (ch == '=') {
            token.append(ch);
        }
        outputBuffer.append(checkOperator(token) + "," + token + "\n");
//        p--;
    }

    public static void checkAndOperator(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch = str.charAt(p);
        if (ch == '&') {
            token.append(ch);
        }
        outputBuffer.append(checkOperator(token) + "," + token + "\n");

    }

    public static void checkOROperator(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch = str.charAt(p);
        if (ch == '|') {
            token.append(ch);
        }
        outputBuffer.append(checkOperator(token) + "," + token + "\n");
    }

    public static void checkExceptOperator(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch = str.charAt(p);
        if (ch == '*') {
            token.append(ch);
            outputBuffer.append(checkDelimiters(token) + "," + token + "\n");
        }
        if (ch == '/' || ch == '=') {
            token.append(ch);
            outputBuffer.append(checkOperator(token) + "," + token + "\n");
        }

    }

    public static void checkMultiplyOperator(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch = str.charAt(p);
        if (ch == '=') {
            token.append(ch);
            outputBuffer.append(checkOperator(token) + "," + token + "\n");
        }
        if (ch == '/') {
            token.append(ch);
            outputBuffer.append(checkDelimiters(token) + "," + token + "\n");
        }

    }

    public static void checkGreaterOperator(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch;
        if (p == str.length()) {
            outputBuffer.append(checkOperator(token) + "," + token + "\n");
            return;
        }
        ch = str.charAt(p);
        if (ch != '>') {
            if (ch == '=') {
                token.append(ch);
                p++;
            }
        } else {
            token.append(ch);
            p++;
            if (str.charAt(p) == '>') {
                token.append(str.charAt(p));
            }
        }
        outputBuffer.append(checkOperator(token) + "," + token + "\n");
        p--;


    }

    public static void checkLessOperator(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch = str.charAt(p);
        if (ch != '<') {
            if (ch == '=') {
                token.append(ch);
                p++;
            }
        } else {
            token.append(ch);
            p++;
        }
        outputBuffer.append(checkOperator(token) + "," + token + "\n");
        p--;
    }

    public static void checkOperatorSingle(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p)));
        outputBuffer.append(checkOperator(token) + "," + token + "\n");
    }

    public static void checkDelimiter(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p)));
        for (int i = 0; i < 12; i++) {
            if (delimiter[i][0].contentEquals(token)) {
                outputBuffer.append(delimiter[i][1] + "," + token + "\n");
            }
        }
    }

    public static String checkDelimiters(StringBuilder str) {
        for (int i = 0; i < 12; i++) {
            if (delimiter[i][0].contentEquals(str)) {
                return delimiter[i][1];
            }
        }
        return "id";
    }

    public static void plusAndLess(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch = str.charAt(p);
        if (ch != '+' && ch != '-' && ch != '=') {
            outputBuffer.append(checkOperator(token) + "," + token + "\n");
        } else if (ch == '=') {
            token.append(ch);
            outputBuffer.append(checkOperator(token) + "," + token + "\n");
        } else if (ch == str.charAt(p - 1) && ch == '+') {
            token.append(ch);
            outputBuffer.append(checkOperator(token) + "," + token + "\n");
        } else if (ch == str.charAt(p - 1) && ch == '-') {
            token.append(ch);
            outputBuffer.append(checkOperator(token) + "," + token + "\n");
        } else {

        }
    }

    public static String checkOperator(StringBuilder str) {
        for (int i = 0; i < 34; i++) {
            if (operator[i][0].contentEquals(str)) {
                return operator[i][1];
            }
        }
        return "id";
    }

    public static void digitCheck(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch;
        String constant;
        while (p < str.length()) {
            ch = str.charAt(p);
            if (!Character.isDigit(ch)) {
                break;
            } else {
                token.append(ch);
            }
            p++;
        }
        Integer integer = Integer.valueOf(String.valueOf(token));
        String s = Integer.toString(integer, 2);
        outputBuffer.append("200," + s + "\n");
        p--;
    }

    public static String checkKeyWord(StringBuilder str) {
        for (int i = 0; i < 53; i++) {
            if (keyWord[i][0].contentEquals(str)) {
                return keyWord[i][1];//关键字的种别编码
            }
        }
        for (int i = 0; i < q; i++) {
            if (constact[i][0].contentEquals(str)) {
                return "100";
            }
        }
        constact[q] = new String[]{String.valueOf(str), q.toString()};
        Integer a = q;
        q++;
        return "100";

    }

    public static void letterCheck(String str) {
        StringBuilder token = new StringBuilder(String.valueOf(str.charAt(p++)));
        char ch;
        while (p < str.length()) {
            ch = str.charAt(p);
            if (!Character.isLetterOrDigit(ch)) {
                break;
            } else {
                token.append(ch);
            }
            p++;
        }
        outputBuffer.append(checkKeyWord(token) + "," + token + "\n");
        p--;
    }

    public static void clear(){
        outputBuffer = new StringBuffer();
    }
}