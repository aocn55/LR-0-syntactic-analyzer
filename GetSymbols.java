package grammarUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetSymbols {
    // Map<String, List<String>> grammar = GetGrammar.getGrammar(new File("C:\\Users\\Lzik\\Desktop\\DemoJAVA\\LR0SyntaxAnalysis\\src\\grammar.txt"));

    static List<String> grammar;

    static {
        try {
            grammar = GetGrammarPro.getGrammar(new File(
                    "C:\\Users\\Lzik\\Desktop\\DemoJAVA\\LR0SyntaxAnalysis\\src\\grammar.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public GetSymbols() throws FileNotFoundException {
    }

    /**
     * 获取文法中的终结符和非终结符
     * @return
     */
    public static List<List<Character>> getSymbol() {
        List<List<Character>> symbols = new ArrayList<>();
        List<Character> terminalSymbol = new ArrayList<>();
        List<Character> nonTerminalSymbol = new ArrayList<>();
        /* *
         *  获取文法中的终结符
         *  终结符就是所给出的文法中的第一个字符
         *  去重
         */
        for(String s : grammar) {
            char c = s.charAt(0);
            if(!nonTerminalSymbol.contains(c)) {
                nonTerminalSymbol.add(c);
            }
        }
        /* *
         *  获取文法中的非终结符
         *  非终结符文法右部所有字符，去除终结符
         *  去重
         */
        for(String s : grammar) {
            for(int i = 3; i < s.length(); i++) {
                char c = s.charAt(i);
                if(!nonTerminalSymbol.contains(c) && !terminalSymbol.contains(c)) {
                    terminalSymbol.add(c);
                }
            }
        }
        symbols.add(terminalSymbol);
        symbols.add(nonTerminalSymbol);
        return symbols;
    }

    public static void main(String[] args) throws FileNotFoundException {
        GetSymbols getSymbols = new GetSymbols();
        List<List<Character>> symbols = getSymbols.getSymbol();
        for(List<Character> list : symbols) {
            for(Character c : list) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

}
