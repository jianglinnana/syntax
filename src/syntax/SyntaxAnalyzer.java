package syntax;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SyntaxAnalyzer {

    private static ArrayList<String> stack = new ArrayList<>();
    private static ArrayList<Integer> reader = new ArrayList<>();
    private static Production[] productions = new Production[43];
    private static HashMap<Integer, String> map_i2s;
    private static HashMap<String, Integer> map_s2i;


    public static void syntaxAnalyzer(String inputFilePath, String outputFilePath) {
        int stackTop = 1;
        int readerTop = 0;
        int index = 0;
        initMap();
        initProductions();
        stack.add(0, String.valueOf(map_s2i.get("$")));
        stack.add(stackTop, "W");
        StringBuffer outputBuffer = new StringBuffer();
        try {
            readToReader(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        reader.add(map_s2i.get("$"));
        while (stackTop >= 0) {
            outputBuffer.append("第" + index + "步:      当前栈:");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i <= stackTop; i++) {
                String str = null;
                try {
                    str = map_i2s.get(Integer.valueOf(stack.get(i)));
                    if (str != null) {
                        sb.append(str + " ");
                        outputBuffer.append(str + " ");
                    }
                }catch (NumberFormatException e){
                    sb.append(stack.get(i)+" ");
                    outputBuffer.append(stack.get(i)+" ");
                }
            }
            System.out.printf("%-30s", sb.toString());
            System.out.print("待读队列：");
            outputBuffer.append("             待读队列：");
            sb = new StringBuffer();
            for (int i = 0; i < reader.size(); i++) {
                sb.append(map_i2s.get(reader.get(i)) + " ");
                outputBuffer.append(map_i2s.get(reader.get(i)) + " ");
            }
            System.out.printf("%-55s", sb.toString());
            if (match(stackTop, readerTop)) {
                stackTop--;
                System.out.print("\n");
                outputBuffer.append("\n");
            } else {
                int i = ll1_table(stackTop, readerTop);
                stackTop += stackPush(stackTop, productions[i]); // 压栈
                System.out.printf("%-30s", "下一步所用产生式：" + productions[i].prod);
                System.out.println();
                outputBuffer.append("         下一步所用产生式：" + productions[i].prod + "\n");
            }
        }
        if (stackTop == -1) {
            System.out.println("语法分析成功");
            outputBuffer.append("Accept");
        }
        File outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        PrintWriter writer = null;
        try {
            outputFile.createNewFile();
            writer = new PrintWriter(new FileOutputStream(outputFile));
            writer.write(outputBuffer.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    //C:\\Users\\jianglinnana\\Desktop\\syntax\\input.txt
    public static void readToReader(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        line = br.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            int pos = line.indexOf(",");
            reader.add(Integer.valueOf(line.substring(0, pos)));
            line = br.readLine(); // 读取下一行
        }
        br.close();
        is.close();
    }

    private static int stackPush(int stackTop, Production production) {
        int len = production.r_str.length;
        stack.remove(stackTop);
        if ("ε".equals(production.r_str[0])) {
        } else {
            for (int i = len - 1; i >= 0; i--) {
                stack.add(production.r_str[i]);
            }
            return len - 1;
        }
        return -1;
    }

    // 利用LL(1)预测分析表进行分析
    private static int ll1_table(int stackTop, int readerTop) {
        String s = stack.get(stackTop);
        String r = map_i2s.get(reader.get(readerTop));
        switch (s) {
            case "W":
                if ("bool".equals(r) || "short".equals(r) || "int".equals(r) || "long".equals(r) ||
                        "float".equals(r) || "double".equals(r) || "char".equals(r) ||
                        "#".equals(r) || "void".equals(r) || "using".equals(r) || "while".equals(r) ||
                        "if".equals(r) || "id".equals(r)) {
                    return 0;
                } else if ("$".equals(r) || "}".equals(r)) {
                    return 1;
                } else {
                    return -1;
                }
            case "S":
                if ("void".equals(r)) {
                    return 8;
                } else if ("if".equals(r)) {
                    return 6;
                } else if ("bool".equals(r) || "short".equals(r) || "int".equals(r) || "long".equals(r) ||
                        "float".equals(r) || "double".equals(r) || "char".equals(r)) {
                    return 4;
                } else if ("using".equals(r)) {
                    return 3;
                } else if ("while".equals(r)) {
                    return 7;
                } else if ("id".equals(r)) {
                    return 5;
                } else if ("#".equals(r)) {
                    return 2;
                } else {
                    return -1;
                }
            case "Q":
                if ("else".equals(r)) {
                    return 9;
                } else if ("void".equals(r) || "if".equals(r) || "double".equals(r) ||
                        "using".equals(r) || "}".equals(r) || "short".equals(r) ||
                        "long".equals(r) || "int".equals(r) || "while".equals(r) ||
                        "id".equals(r) || "float".equals(r) || "bool".equals(r) ||
                        "char".equals(r) || "#".equals(r) || "$".equals(r)) {
                    return 10;
                } else {
                    return -1;
                }
            case "L":
                if ("id".equals(r)) {
                    return 11;
                } else {
                    return -1;
                }
            case "V":
                if (",".equals(r)) {
                    return 12;
                } else if (";".equals(r)) {
                    return 13;
                } else {
                    return -1;
                }
            case "X":
                if ("num".equals(r) || "id".equals(r) || "(".equals(r) || "-".equals(r)) {
                    return 14;
                } else {
                    return -1;
                }
            case "A":
                if ("double".equals(r)) {
                    return 20;
                } else if ("short".equals(r)) {
                    return 16;
                } else if ("long".equals(r)) {
                    return 18;
                } else if ("int".equals(r)) {
                    return 17;
                } else if ("float".equals(r)) {
                    return 19;
                } else if ("bool".equals(r)) {
                    return 21;
                } else if ("char".equals(r)) {
                    return 15;
                } else {
                    return -1;
                }
            case "E":
                if ("num".equals(r) || "id".equals(r) || "(".equals(r) || "-".equals(r)) {
                    return 28;
                } else {
                    return -1;
                }
            case "O":
                if ("+".equals(r) || "-".equals(r)) {
                    return 29;
                } else if (">".equals(r) || "!=".equals(r) || "<=".equals(r) ||
                        ">=".equals(r) || "<".equals(r) || "==".equals(r) ||
                        ")".equals(r) || ";".equals(r)) {
                    return 30;
                } else {
                    return -1;
                }
            case "R":
                if (">".equals(r)) {
                    return 22;
                } else if ("!=".equals(r)) {
                    return 27;
                } else if ("<=".equals(r)) {
                    return 25;
                } else if (">=".equals(r)) {
                    return 24;
                } else if ("<".equals(r)) {
                    return 23;
                } else if ("==".equals(r)) {
                    return 26;
                } else {
                    return -1;
                }
            case "M":
                if ("+".equals(r)) {
                    return 31;
                } else if ("-".equals(r)) {
                    return 32;
                } else {
                    return -1;
                }
            case "T":
                if ("num".equals(r) || "id".equals(r) || "(".equals(r) || "-".equals(r)) {
                    return 33;
                } else {
                    return -1;
                }
            case "P":
                if ("/".equals(r) || "*".equals(r)) {
                    return 34;
                } else if ("+".equals(r) || "-".equals(r) ||
                        ">".equals(r) || "!=".equals(r) || "<=".equals(r) ||
                        ">=".equals(r) || "<".equals(r) || "==".equals(r) ||
                        ")".equals(r) || ";".equals(r)) {
                    return 35;
                } else {
                    return -1;
                }
            case "Z":
                if ("/".equals(r)) {
                    return 37;
                } else if ("*".equals(r)) {
                    return 36;
                } else {
                    return -1;
                }
            case "N":
                if ("num".equals(r) || "id".equals(r) || "(".equals(r)) {
                    return 38;
                } else if ("-".equals(r)) {
                    return 39;
                } else {
                    return -1;
                }
            case "F":
                if ("num".equals(r)) {
                    return 41;
                } else if ("id".equals(r)) {
                    return 40;
                } else if ("(".equals(r)) {
                    return 42;
                } else {
                    return -1;
                }
            default:
                System.out.println("语法错误");
                return -1;
        }
    }

    private static boolean match(int stackTop, int readerTop) {
        try {
            int stackTopVal = Integer.valueOf(stack.get(stackTop)); // 未抛出异常说明是终结符
            if (stackTopVal == reader.get(0)) {
                stack.remove(stackTop);
                reader.remove(readerTop);
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            // 抛出异常说明是非终结符
            return false;
        }
    }


    private static void initProductions() {
        productions[0] = new Production("W",
                new String[]{"S", "W"},
                "W->SW");
        productions[1] = new Production("W",
                new String[]{"ε"},
                "W->ε");
        productions[2] = new Production("S",
                new String[]{String.valueOf(map_s2i.get("#")),
                        String.valueOf(map_s2i.get("include")),
                        String.valueOf(map_s2i.get("<")),
                        String.valueOf(map_s2i.get("id")),
                        String.valueOf(map_s2i.get(">"))},
                "S -> # include < id >");
        productions[3] = new Production("S",
                new String[]{String.valueOf(map_s2i.get("using")),
                        String.valueOf(map_s2i.get("namespace")),
                        String.valueOf(map_s2i.get("id")),
                        String.valueOf(map_s2i.get(";"))},
                "S -> using namespace id ;");
        productions[4] = new Production("S",
                new String[]{"A", "L", String.valueOf(map_s2i.get(";"))},
                "S -> A L ;");
        productions[5] = new Production("S",
                new String[]{String.valueOf(map_s2i.get("id")),
                        String.valueOf(map_s2i.get("=")), "E",
                        String.valueOf(map_s2i.get(";"))},
                "S -> id = E ;");
        productions[6] = new Production("S",
                new String[]{String.valueOf(map_s2i.get("if")),
                        String.valueOf(map_s2i.get("(")), "X",
                        String.valueOf(map_s2i.get(")")),
                        String.valueOf(map_s2i.get("{")), "W",
                        String.valueOf(map_s2i.get("}")), "Q"},
                "S -> if ( X ) { W } Q");
        productions[7] = new Production("S",
                new String[]{String.valueOf(map_s2i.get("while")),
                        String.valueOf(map_s2i.get("(")), "X",
                        String.valueOf(map_s2i.get(")")),
                        String.valueOf(map_s2i.get("{")), "W",
                        String.valueOf(map_s2i.get("}"))},
                "S -> while ( X ) { W }");
        productions[8] = new Production("S",
                new String[]{String.valueOf(map_s2i.get("void")),
                        String.valueOf(map_s2i.get("main")),
                        String.valueOf(map_s2i.get("(")),
                        String.valueOf(map_s2i.get(")")),
                        String.valueOf(map_s2i.get("{")), "W",
                        String.valueOf(map_s2i.get("}"))},
                "S -> void main ( ) { W }");
        productions[9] = new Production("Q",
                new String[]{String.valueOf(map_s2i.get("else")),
                        String.valueOf(map_s2i.get("{")), "W",
                        String.valueOf(map_s2i.get("}"))},
                "Q -> else { W }");
        productions[10] = new Production("Q",
                new String[]{"ε"},
                "Q->ε");
        productions[11] = new Production("L",
                new String[]{String.valueOf(map_s2i.get("id")), "V"},
                "L->idV");
        productions[12] = new Production("V",
                new String[]{String.valueOf(map_s2i.get(",")),
                        String.valueOf(map_s2i.get("id")), "V"},
                "V->,idV");
        productions[13] = new Production("V",
                new String[]{"ε"},
                "V->ε");
        productions[14] = new Production("X",
                new String[]{"E", "R", "E"},
                "X->ERE");
        productions[15] = new Production("A",
                new String[]{String.valueOf(map_s2i.get("char"))},
                "A->char");
        productions[16] = new Production("A",
                new String[]{String.valueOf(map_s2i.get("short"))},
                "A->short");
        productions[17] = new Production("A",
                new String[]{String.valueOf(map_s2i.get("int"))},
                "A->int");
        productions[18] = new Production("A",
                new String[]{String.valueOf(map_s2i.get("long"))},
                "A->long");
        productions[19] = new Production("A",
                new String[]{String.valueOf(map_s2i.get("float"))},
                "A->float");
        productions[20] = new Production("A",
                new String[]{String.valueOf(map_s2i.get("double"))},
                "A->double");
        productions[21] = new Production("A",
                new String[]{String.valueOf(map_s2i.get("bool"))},
                "A->bool");
        productions[22] = new Production("R",
                new String[]{String.valueOf(map_s2i.get(">"))},
                "R-> >");
        productions[23] = new Production("R",
                new String[]{String.valueOf(map_s2i.get("<"))},
                "R-> <");
        productions[24] = new Production("R",
                new String[]{String.valueOf(map_s2i.get(">="))},
                "R-> >=");
        productions[25] = new Production("R",
                new String[]{String.valueOf(map_s2i.get("<="))},
                "R-> <=");
        productions[26] = new Production("R",
                new String[]{String.valueOf(map_s2i.get("=="))},
                "R-> ==");
        productions[27] = new Production("R",
                new String[]{String.valueOf(map_s2i.get("!="))},
                "R-> !=");
        productions[28] = new Production("E",
                new String[]{"T", "O"},
                "E->TO");
        productions[29] = new Production("O",
                new String[]{"M", "O"},
                "O->MO");
        productions[30] = new Production("O",
                new String[]{"ε"},
                "O->ε");
        productions[31] = new Production("M",
                new String[]{String.valueOf(map_s2i.get("+")), "T"},
                "M->+T");
        productions[32] = new Production("M",
                new String[]{String.valueOf(map_s2i.get("-")), "T"},
                "M->-T");
        productions[33] = new Production("T",
                new String[]{"N", "P"},
                "T->NP");
        productions[34] = new Production("P",
                new String[]{"Z", "P"},
                "P->ZP");
        productions[35] = new Production("P",
                new String[]{"ε"},
                "P->ε");
        productions[36] = new Production("Z",
                new String[]{String.valueOf(map_s2i.get("*")), "N"},
                "Z->*N");
        productions[37] = new Production("Z",
                new String[]{String.valueOf(map_s2i.get("/")), "N"},
                "Z->/N");
        productions[38] = new Production("N",
                new String[]{"F"},
                "N->F");
        productions[39] = new Production("N",
                new String[]{String.valueOf(map_s2i.get("-")), "N"},
                "N->-N");
        productions[40] = new Production("F",
                new String[]{String.valueOf(map_s2i.get("id"))},
                "F->id");
        productions[41] = new Production("F",
                new String[]{String.valueOf(map_s2i.get("num"))},
                "F->num");
        productions[42] = new Production("F",
                new String[]{String.valueOf(map_s2i.get("(")), "E",
                        String.valueOf(map_s2i.get(")"))},
                "F->(E)");


    }

    private static void initMap() {
        map_s2i = new HashMap<>();
        map_s2i.put("char", 9);
        map_s2i.put("short", 42);
        map_s2i.put("int", 8);
        map_s2i.put("long", 28);
        map_s2i.put("float", 15);
        map_s2i.put("double", 10);
        map_s2i.put("if", 1);
        map_s2i.put("else", 2);
        map_s2i.put("while", 3);
        map_s2i.put("id", 100);// 标识符
        map_s2i.put("num", 200);// 整数
        map_s2i.put("=", 222);
        map_s2i.put("==", 218);
        map_s2i.put(">", 220);
        map_s2i.put("<", 221);
        map_s2i.put(">=", 223);
        map_s2i.put("<=", 224);
        map_s2i.put("+", 201);
        map_s2i.put("-", 202);
        map_s2i.put("*", 203);
        map_s2i.put("/", 204);
        map_s2i.put("(", 301);
        map_s2i.put(")", 302);
        map_s2i.put("{", 303);
        map_s2i.put("}", 304);
        map_s2i.put(",", 233);
        map_s2i.put(";", 309);
        map_s2i.put("!=", 219);
        map_s2i.put("$", 48);
        map_s2i.put("include", 50);
        map_s2i.put("using", 25);
        map_s2i.put("namespace", 47);
        map_s2i.put("bool", 52);
        map_s2i.put("void", 35);
        map_s2i.put("#", 308);
        map_s2i.put("main", 53);

        map_i2s = new HashMap<>();
        map_i2s.put(9, "char");
        map_i2s.put(42, "short");
        map_i2s.put(8, "int");
        map_i2s.put(28, "long");
        map_i2s.put(15, "float");
        map_i2s.put(10, "double");
        map_i2s.put(1, "if");
        map_i2s.put(2, "else");
        map_i2s.put(3, "while");
        map_i2s.put(100, "id");// 标识符
        map_i2s.put(200, "num");// 整数
        map_i2s.put(222, "=");
        map_i2s.put(218, "==");
        map_i2s.put(220, ">");
        map_i2s.put(221, "<");
        map_i2s.put(223, ">=");
        map_i2s.put(224, "<=");
        map_i2s.put(201, "+");
        map_i2s.put(202, "-");
        map_i2s.put(203, "*");
        map_i2s.put(204, "/");
        map_i2s.put(301, "(");
        map_i2s.put(302, ")");
        map_i2s.put(303, "{");
        map_i2s.put(304, "}");
        map_i2s.put(233, ",");
        map_i2s.put(309, ";");
        map_i2s.put(219, "!=");
        map_i2s.put(48, "$");
        map_i2s.put(50, "include");
        map_i2s.put(25, "using");
        map_i2s.put(47, "namespace");
        map_i2s.put(52, "bool");
        map_i2s.put(35, "void");
        map_i2s.put(308, "#");
        map_i2s.put(53, "main");
    }

    private static class Production {
        String l_str;
        String[] r_str;
        String prod;

        public Production(String l_str, String[] r_str, String prod) {
            this.l_str = l_str;
            this.r_str = r_str;
            this.prod = prod;
        }
    }
}
