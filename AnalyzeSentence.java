package analyzeSentence;

import grammarAnalysis.ConstructTable;
import grammarUtils.GetGrammarPro;
import grammarUtils.GetSymbols;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Stack;

public class AnalyzeSentence {

    private static final String[][] table;
    private static final List<String> grammar;
    private static final List<List<Character>> symbols;
    static {
        try{
            grammar = GetGrammarPro.getGrammar(new File("C:\\Users\\Lzik\\Desktop\\DemoJAVA\\LR0SyntaxAnalysis\\src\\grammar.txt"));
            table = ConstructTable.constructTable();
            symbols = GetSymbols.getSymbol();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void analyzeSentence(String sentence) {
        System.out.println("分析句子：" + sentence + "过程如下：");
        Stack<Character> inputStack = new Stack<>();
        Stack<Integer> statusStack = new Stack<>();
        Stack<Character> symbolStack = new Stack<>();

        // 初始化
        inputStack.push('#');
        statusStack.push(0);
        symbolStack.push('#');
        for(int i = sentence.length() - 1; i >= 0; i--) { /* 输入栈存放句子 */
            inputStack.push(sentence.charAt(i));
        }

        // 输出初始化栈
        System.out.println("statusStack : " + statusStack);
        System.out.println("symbolStack : " + symbolStack);
        System.out.println("inputStack : " + inputStack);
        System.out.println();

        // 分析过程
        while(true) {
            int status = statusStack.peek(); // 当前状态
            char input = inputStack.peek(); // 当前输入
            char symbol = symbolStack.peek(); // 当前符号

            if(symbols.get(0).contains(input)) {
                int index = symbols.get(0).indexOf(input);
                if(table[status][index] == null) {
                    System.out.println("分析失败！句子不符合文法！");
                    return;
                }else if(table[status][index].charAt(0) == 's') { /* 是移进项目，如s1等 */
                    statusStack.push(Integer.parseInt(table[status][index].substring(1))); // 状态栈压入状态
                    symbolStack.push(input); // 符号栈压入符号
                    inputStack.pop(); // 输入栈弹出符号
                    System.out.println("action : " + table[status][index]);
                }else if(table[status][index].charAt(0) == 'r') { /* 是归约项目，如r1等 */
                    System.out.println("action : " + table[status][index]);
                    int indexGrammar = Integer.parseInt(table[status][index - 1].substring(1)); // 要归约的文法的下标
                    String s = grammar.get(indexGrammar); // 要归约的文法
                    int length = s.length() - 3; // 要归约的文法的长度 - 3是因为去除了->
                    for (int i = 0; i < length; i++) { // 弹出状态栈和符号栈的元素
                        statusStack.pop();
                        symbolStack.pop();
                    }
                    status = statusStack.peek(); // 当前状态
                    symbolStack.push(s.charAt(0)); // 符号栈压入归约后的符号
                    int indexSymbol = symbols.get(1).indexOf(s.charAt(0)); // 归约后的符号的下标
                    // int a2 = Integer.parseInt(table[status][indexSymbol + symbols.get(0).size() + 1]);
                    statusStack.push(Integer.parseInt(table[status][indexSymbol + symbols.get(0).size() + 1])); // 状态栈压入归约后的状态
                    System.out.println("GOTO :" + table[status][indexSymbol + symbols.get(0).size()]);
                }
            }

            if (input == '#') {
                // 获取归约文法下标
                String action = table[status][symbols.get(0).size()];
                System.out.println("action: " + action);
                if (action == null) {
                    System.out.println("分析失败！句子不符合文法！");
                    return;
                }
                if (action.equals("acc")) {
                    System.out.println("分析成功！句子符合文法！");
                    return;
                }
                int indexGrammar = Integer.parseInt(action.substring(1));
                String s = grammar.get(indexGrammar);
                int length = s.length() - 3;
                for (int i = 0; i < length; i++) {
                    statusStack.pop();
                    symbolStack.pop();
                }
                status = statusStack.peek(); // 当前状态
                symbolStack.push(s.charAt(0)); // 符号栈压入归约后的符号
                int indexSymbol = symbols.get(1).indexOf(s.charAt(0));
                statusStack.push(Integer.parseInt(table[status][indexSymbol + symbols.get(0).size() + 1]));
                System.out.println("GOTO :" + table[status][indexSymbol + symbols.get(0).size() + 1]);
            }

            System.out.println("当前状态栈：" + statusStack);
            System.out.println("当前符号栈：" + symbolStack);
            System.out.println("当前输入栈：" + inputStack);
            System.out.println();
        }
    }


    public static void main(String[] args) {
        String s = "bccd";
        analyzeSentence(s);
    }
}
