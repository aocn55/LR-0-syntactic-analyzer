package grammarAnalysis;

import Entity.Status;
import grammarUtils.GetGrammarPro;
import grammarUtils.GetSymbols;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConstructTable {

    private static final List<Status> DFA = ConstructDFA.constructDFA();
    private static final List<String> grammar;
    private static final List<List<Character>> symbols;

    private static final List<String> itemSet;
    static {
        try {
            grammar = GetGrammarPro.getGrammar(new File("C:\\Users\\Lzik\\Desktop\\DemoJAVA\\LR0SyntaxAnalysis\\src\\grammar.txt"));
            symbols = GetSymbols.getSymbol();
            itemSet = ConstructDFA.itemSet(grammar);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[][] constructTable() {
        {
            // 输出表头
            int terminalSymbolSize = symbols.get(0).size();
            int nonTerminalSymbolSize = symbols.get(1).size();
            System.out.print("状态\t");
            for (int i = 0; i < terminalSymbolSize; i++) {
                System.out.print("\t" + symbols.get(0).get(i));
            }
            System.out.print("\t#\t|");
            for (int i = 1; i < nonTerminalSymbolSize; i++) {
                System.out.print("\t" + symbols.get(1).get(i));
            }
            System.out.println();
        }
        // 输出表格
        String[][] table = new String[DFA.size()][symbols.get(0).size() + symbols.get(1).size() + 1];
        for(int i = 0; i < DFA.size(); i++) { // 根据 DFA 构造文法分析表
            Status status = DFA.get(i);
            Map<Character, Integer> map = status.getStatusMap();
            if(map.size() == 0) { /* 是归约状态 */
                String s = status.getStatusGrammar().get(0).substring(0, status.getStatusGrammar().get(0).indexOf("·"));
                int index = grammar.indexOf(s); /* 是文法的第几个产生式 */
                if(index == 0) { /* 是接受状态 */
                    table[i][symbols.get(0).size()] = "acc";
                }else {
                    for (int j = 0; j <= symbols.get(0).size(); j++) {
                        table[i][j] = "r" + index;
                    }
                }
            }

            for(Map.Entry<Character, Integer> entry : map.entrySet()) {
                Character c = entry.getKey();
                int index = symbols.get(0).indexOf(c);
                if(symbols.get(0).contains(c)) { /* 终结符 */
                    table[i][index] = "s" + entry.getValue();
                }else {
                    // 是非终结符就在右半部加一个数字字符
                    table[i][symbols.get(0).size() + 1 + symbols.get(1).indexOf(c)] = entry.getValue().toString();
                }
            }
        }

        for(int i = 0; i < table.length; i++) {
            System.out.print(i + "\t");
            for(int j = 0; j < table[i].length; j++) {
                if(table[i][j] == null) {
                    System.out.print("\t");
                }else {
                    System.out.print("\t" + table[i][j]);
                }
            }
            System.out.println();
        }
        return table;
    }

    public static void main(String[] args) {
        constructTable();
    }
}
