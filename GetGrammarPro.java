package grammarUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GetGrammarPro {
    public static List<String> getGrammar(File fin) throws FileNotFoundException {
        List<String> res = new ArrayList<>();
        // 实现从文件中读取文法
        FileInputStream fileInputStream = new FileInputStream(fin);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = "";
        try{
            while ((line = bufferedReader.readLine()) != null) {
                res.add(line);
            }
            return res;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> grammar = getGrammar(new File("C:\\Users\\Lzik\\Desktop\\DemoJAVA\\LR0SyntaxAnalysis\\src\\grammar.txt"));
            for (String s : grammar) {
                System.out.println(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
