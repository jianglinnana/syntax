package syntax;

import java.util.ArrayList;
import java.util.HashMap;

public class SyntaxAnalyzer {

    private static ArrayList<String> stack = new ArrayList<>();
    private static ArrayList<Integer> reader = new ArrayList<>();
    private static Production[] productions = new Production[50];
    private static HashMap<Integer, String> map_i2s;
    private static HashMap<String, Integer> map_s2i;

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
    }

    private static void initProductions() {
        productions[0] = new Production("W",
                new String[]{"S", "W"},
                "S->SW");
        productions[1] = new Production("W",
                new String[]{"ε"},
                "W->ε");
        productions[2] = new Production("S",
                new String[]{"B"},
                "S->B");
        productions[3] = new Production("S",
                new String[]{"C"},
                "S->C");
        productions[4] = new Production("S",
                new String[]{"D"},
                "S->D");
        productions[5] = new Production("S",
                new String[]{"G"},
                "S->G");
        productions[6] = new Production("S",
                new String[]{"H"},
                "S->H");
        productions[7] = new Production("S",
                new String[]{"I"},
                "S->I");
        productions[8] = new Production("S",
                new String[]{"U"},
                "S->U");
        productions[9] = new Production("B",
                new String[]{String.valueOf(map_s2i.get("#include")),
                        String.valueOf(map_s2i.get("<")),
                        String.valueOf(map_s2i.get("id")),
                        String.valueOf(map_s2i.get(">"))},
                "B->#include<id>");
        productions[10] = new Production("C",
                new String[]{String.valueOf(map_s2i.get("using")),
                        String.valueOf(map_s2i.get("namespace")),
                        String.valueOf(map_s2i.get("id")),
                        String.valueOf(map_s2i.get(";"))},
                "C->using namespace id;");
        productions[11] = new Production("D",
                new String[]{"A", "L", String.valueOf(map_s2i.get(";"))},
                "D->AL;");
        productions[12] = new Production("G",
                new String[]{String.valueOf(map_s2i.get("id")),
                        String.valueOf(map_s2i.get("=")), "E",
                        String.valueOf(map_s2i.get(";"))},
                "G->id=E;");
        productions[13] = new Production("H",
                new String[]{String.valueOf(map_s2i.get("if")),
                        String.valueOf(map_s2i.get("(")), "X",
                        String.valueOf(map_s2i.get(")")),
                        String.valueOf(map_s2i.get("{")), "S",
                        String.valueOf(map_s2i.get("}")), "Q"},
                "H->if(X){S}Q");
        productions[14] = new Production("I",
                new String[]{String.valueOf(map_s2i.get("while")),
                        String.valueOf(map_s2i.get("(")),"X",
                        String.valueOf(map_s2i.get(")")),
                        String.valueOf(map_s2i.get("{")),"S",
                        String.valueOf(map_s2i.get("}"))},
                "I->while(X){S}");
        productions[15] = new Production("Q",
                new String[]{String.valueOf(map_s2i.get("else")),
                        String.valueOf(map_s2i.get("{")),"S",
                        String.valueOf(map_s2i.get("}"))},
                "Q->else{S}");
        productions[16] = new Production("Q",
                new String[]{"ε"},
                "Q->ε");
        productions[17] = new Production("L",
                new String[]{},
                "");
        productions[18] = new Production("V",
                new String[]{},
                "");
        productions[19] = new Production("V",
                new String[]{},
                "");
        productions[20] = new Production("X",
                new String[]{},
                "");
        productions[21] = new Production("A",
                new String[]{},
                "");
        productions[22] = new Production("A",
                new String[]{},
                "");
        productions[23] = new Production("A",
                new String[]{},
                "");
        productions[24] = new Production("A",
                new String[]{},
                "");
        productions[25] = new Production("A",
                new String[]{},
                "");
        productions[26] = new Production("A",
                new String[]{},
                "");
        productions[27] = new Production("A",
                new String[]{},
                "");
        productions[28] = new Production("R",
                new String[]{},
                "");
        productions[29] = new Production("R",
                new String[]{},
                "");
        productions[30] = new Production("R",
                new String[]{},
                "");
        productions[31] = new Production("R",
                new String[]{},
                "");
        productions[32] = new Production("R",
                new String[]{},
                "");
        productions[33] = new Production("R",
                new String[]{},
                "");
        productions[34] = new Production("E",
                new String[]{},
                "");
        productions[35] = new Production("O",
                new String[]{},
                "");
        productions[36] = new Production("O",
                new String[]{},
                "");
        productions[37] = new Production("M",
                new String[]{},
                "");
        productions[38] = new Production("M",
                new String[]{},
                "");
        productions[39] = new Production("T",
                new String[]{},
                "");
        productions[40] = new Production("P",
                new String[]{},
                "");
        productions[41] = new Production("P",
                new String[]{},
                "");
        productions[42] = new Production("Z",
                new String[]{},
                "");
        productions[43] = new Production("Z",
                new String[]{},
                "");
        productions[44] = new Production("N",
                new String[]{},
                "");
        productions[45] = new Production("N",
                new String[]{},
                "");
        productions[46] = new Production("F",
                new String[]{},
                "");
        productions[47] = new Production("F",
                new String[]{},
                "");
        productions[48] = new Production("F",
                new String[]{},
                "");
        productions[49] = new Production("U",
                new String[]{},
                "");


    }

    private static void initMap() {
        map_s2i = new HashMap<>();
        map_s2i.put("char", 1);
        map_s2i.put("short", 2);
        map_s2i.put("int", 3);
        map_s2i.put("long", 4);
        map_s2i.put("float", 5);
        map_s2i.put("double", 6);
        map_s2i.put("if", 7);
        map_s2i.put("else", 8);
        map_s2i.put("while", 9);
        map_s2i.put("id", 20);// 标识符
        map_s2i.put("num", 30);// 整数
        map_s2i.put("=", 31);
        map_s2i.put("==", 32);
        map_s2i.put(">", 33);
        map_s2i.put("<", 34);
        map_s2i.put(">=", 35);
        map_s2i.put("<=", 36);
        map_s2i.put("+", 37);
        map_s2i.put("-", 38);
        map_s2i.put("*", 39);
        map_s2i.put("/", 40);
        map_s2i.put("(", 41);
        map_s2i.put(")", 42);
        map_s2i.put("{", 43);
        map_s2i.put("}", 44);
        map_s2i.put(",", 45);
        map_s2i.put(";", 46);
        map_s2i.put("!=", 47);
        map_s2i.put("$", 48);
        map_s2i.put("#include", 49);
        map_s2i.put("using", 50);
        map_s2i.put("namespace", 51);
        map_s2i.put("bool", 52);
        map_s2i.put("void main()", 53);

        map_i2s = new HashMap<>();
        map_i2s.put(1, "char");
        map_i2s.put(2, "short");
        map_i2s.put(3, "int");
        map_i2s.put(4, "long");
        map_i2s.put(5, "float");
        map_i2s.put(6, "double");
        map_i2s.put(7, "if");
        map_i2s.put(8, "else");
        map_i2s.put(9, "while");
        map_i2s.put(20, "id");// 标识符
        map_i2s.put(30, "num");// 整数
        map_i2s.put(31, "=");
        map_i2s.put(32, "==");
        map_i2s.put(33, ">");
        map_i2s.put(34, "<");
        map_i2s.put(35, ">=");
        map_i2s.put(36, "<=");
        map_i2s.put(37, "+");
        map_i2s.put(38, "-");
        map_i2s.put(39, "*");
        map_i2s.put(40, "/");
        map_i2s.put(41, "[");
        map_i2s.put(42, "]");
        map_i2s.put(43, "{");
        map_i2s.put(44, "}");
        map_i2s.put(45, ",");
        map_i2s.put(46, ";");
        map_i2s.put(47, "!=");
        map_i2s.put(48, "$");
        map_i2s.put(49, "#include");
        map_i2s.put(50, "using");
        map_i2s.put(51, "namespace");
        map_i2s.put(52, "bool");
        map_i2s.put(53, "void main()");
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
