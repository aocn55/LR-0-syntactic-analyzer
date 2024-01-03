package grammarAnalysis;

import grammarUtils.GetGrammarPro;
import grammarUtils.GetSymbols;
import Entity.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ConstructDFA {


     private static List<List<Character>> symbols = GetSymbols.getSymbol();

    private static final List<String> grammar;

    static {
        try {
            grammar = GetGrammarPro.getGrammar(new File("C:\\Users\\Lzik\\Desktop\\DemoJAVA\\LR0SyntaxAnalysis\\src\\grammar.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ConstructDFA() throws FileNotFoundException {
    }

    public static List<Status> constructDFA() {
        List<Status> DFA = new ArrayList<>();
        List<String> itemSet = itemSet(grammar);
        int thisStatusNum = 0;
        // 构造开始状态
        Status startStatus = new Status(thisStatusNum++, null, new HashMap<Character, Integer>(), 0);
        List<String> startStatusGrammar = new ArrayList<>();
        startStatusGrammar.add(itemSet.get(0));
        startStatusGrammar = CLOSORE(startStatusGrammar.get(0));
        startStatus.setStatusGrammar(startStatusGrammar);
        DFA.add(startStatus);
        Queue<Status> queueOfStatus = new LinkedList<>();
        queueOfStatus.offer(startStatus);



        while (!queueOfStatus.isEmpty()) {
            Status thisStatus = queueOfStatus.poll();
            List<Character> nextSymbol = getNextChar(thisStatus.getStatusGrammar());
            a : for(Character c : nextSymbol) { /* 对于每一个下个字符来说 */
                /*
                for(String s : thisStatus.getStatusGrammar()) {
                    int index = s.indexOf("·");
                    if(s.charAt(index + 1) == c) { // 对应
                        List<String> newStatusGrammar = new ArrayList<>(); // 下一个状态对应文法
                        for(String s1 : thisStatus.getStatusGrammar()) {
                            int index1 = s1.indexOf("·");
                            if(index1 + 1 < s1.length() && s1.charAt(index1 + 1) == c) {
                                newStatusGrammar.add(s1.substring(0, index1) + c + "·" + s1.substring(index1 + 2));
                            }
                        }

                        // 检查生成的文法是否已经存在
                        for(Status status : DFA) {
                            if(newStatusGrammar.equals(status.getStatusGrammar())) {
                                int nextNum = status.getStatusNum();
                                Map<Character, Integer> map = thisStatus.getStatusMap();
                                map.put(c, nextNum);
                                continue;
                            }
                        }

                        // 检测文法类型
                        for(String item : newStatusGrammar) {

                        }
                    }
                }
                 */
                    List<String> nextItemSet = new ArrayList<>(); // 获取到所有的下一个字符GOTO之后的项目集
                    for(String s : thisStatus.getStatusGrammar()) {
                        int index = s.indexOf('·');
                        if(index + 1 < s.length() && s.charAt(index + 1) == c) {
                                nextItemSet.add(s.substring(0, index) + c + "·" + s.substring(index + 2));
                        }
                    }

                    for(int i = 0; i < nextItemSet.size(); i++) {
                        List<String> itemAfterCLOUSURE = CLOSORE(nextItemSet.get(i));
                        for(String itemAfter : itemAfterCLOUSURE) {
                            if(!nextItemSet.contains(itemAfter)) {
                                nextItemSet.add(itemAfter);
                            }
                        }
                    }

                    // 检测生成的项目集是否存在
                    for(Status status : DFA) {
                        if(nextItemSet.equals(status.getStatusGrammar())) {
                            Map<Character, Integer> map = thisStatus.getStatusMap();
                            map.put(c, status.getStatusNum());
                            thisStatus.setStatusMap(map);
                            continue a;
                        }
                    }

                    // 生成的文法是新文法， 检测文法的类型
                    // 若有冲突直接退出
                    // 若无冲突则将新文法加入DFA中
                    /* *
                     * 产生式 · 在最后面为归约项目
                     * 产生式 · 后跟终结符为移进项目
                     * 产生式 · 后跟非终结符为待约项目
                     * 一个项目集不能同时包含归约项目和移进项目，和不能同时包含两个归约项目
                     */
                    boolean isReduce = false;
                    boolean isShift = false;
                    // 根据·的位置判断项目类型
                    for (String item : nextItemSet) {
                        int index = item.indexOf("·");
                        if (index == item.length() - 1) { // 归约项目
                            if(isShift) {
                                System.out.println("ERROR: 文法冲突");
                                return null;
                            }else {
                                isReduce = true;
                            }
                        }
                        if(index + 1 < item.length()) { //
                            if(isReduce) {
                                System.out.println("ERROR: 文法冲突");
                                return null;
                            }else {
                                isShift = true;
                            }
                        }
                    }

                    int statusType = isReduce ? 2 : (isShift ? 0 : 1);

                    Status status = new Status(thisStatusNum++, nextItemSet, new HashMap<Character, Integer>(), statusType);
                    Map<Character, Integer> map = thisStatus.getStatusMap();
                    map.put(c, status.getStatusNum());
                    thisStatus.setStatusMap(map);
                    if(!isReduce) {
                        queueOfStatus.add(status);
                    }
                    DFA.add(status);
                }
            }
        return DFA;
    }

    /**
     * 构所有项目集
     * @param grammar
     * @return
     */
    public static List<String> itemSet(List<String> grammar) {
        List<String> res = new ArrayList<>();
        // 在每条文法的右部的所有位置加一个 · ，并将其作为新的文法的右部
        for(String s : grammar) {
            if(s.charAt(3) == 'ε') {
                res.add(s.substring(0, 3) + "·");
                continue;
            }
            String grammarRight = s.substring(3);
            for(int i = 0; i < grammarRight.length(); i++) {
                String newGrammar = s.substring(0, 3) + grammarRight.substring(0, i) + "·" + grammarRight.substring(i);
                if(!res.contains(newGrammar)) {
                    res.add(newGrammar);
                }
            }
            String endGrammar = s.substring(0, 3) + grammarRight + "·"; // 将 · 放在最后并添加
            if(!res.contains(endGrammar)) {
                res.add(endGrammar);
            }
        }
        return res;
    }

    /**
     * 获取闭包
     * @param item
     * @return
     *
     * 有一个大问题？ 左递归怎么处理？
     */
    public static List<String> CLOSORE(String item) { // 出现了左递归
        List<String> res = new ArrayList<>();
        res.add(item);
        List<String> itemSet = itemSet(grammar);
        Queue<String> queue = new LinkedList<>();
        queue.offer(item);
        boolean leftProcessFlag = false; // 左递归处理标志， 左递归的产生式只会添加一次
        // 检测给出的产生式中 · 后面的字符是否为非终结符，
        // 若是则将该非终结符的产生式为左部且 · 在最前面的产生式加入队列中
        // 若不是则不做处理
        while(!queue.isEmpty()) {
            String curItem = queue.poll();
            int index = curItem.indexOf("·"); // 获取 · 的位置

            // 如果在末尾就直接返回本身
            if (index == curItem.length() - 1) {
                if(!res.contains(curItem)) {
                    res.add(curItem);
                    // continue;
                }
                continue;
            }
            char nextChar = curItem.charAt(index + 1);
            if(symbols.get(1).contains(nextChar)) { // · 后面的字符为非终结符
                for(String s : itemSet) {
                    // 将 · 后面的字符为非终结符的产生式为左bu且 · 在最前面的产生式加入队列中
                    if(s.charAt(0) == nextChar && s.charAt(3) == '·') {
                        if(s.charAt(4) == nextChar && ! leftProcessFlag ) {
                            queue.offer(s);
                            leftProcessFlag = true;
                        }else if(s.charAt(4) != nextChar) {
                            queue.offer(s);
                        }

                        if(!res.contains(s)) { // 将 · 后面的字符为非终结符的产生式为左部且 · 在最前面的产生式加入结果集中
                            res.add(s);
                        }
                    }
                }
            }else {
                if(!res.contains(curItem)) {
                    res.add(curItem);
                }
            }
        }
        return res;
    }


    /**
     * 获取 itemSet 中所有 · 后面的字符
     * @param itemSet
     * @return
     */
    public static List<Character> getNextChar(List<String> itemSet) {
        List<Character> res = new ArrayList<>();
        for(String s : itemSet) {
            int index = s.indexOf("·");
            if(index != s.length() - 1) {
                res.add(s.charAt(index + 1));
            }
        }
        return res;
    }




    public static void main(String[] args) {
        try {
             constructDFA();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
