package com.hanyc.demo.util;

import java.util.*;

/**
 * @author ：zjx
 * @description：
 * @date ：2024/9/24 14:07
 */
public class AhoCorasickAutomation {

    private Node root;
    private List<String> target;
    private Map<String, List<Integer>> result;

    private static class Node {
        String str;
        Map<Character, Node> table;
        Node fail;

        public Node() {
            table = new HashMap<>();
            fail = null;
        }

        public boolean isWord() {
            return str != null;
        }
    }

    public AhoCorasickAutomation(List<String> target) {
        root = new Node();
        this.target = target;
        buildTrieTree();
        buildFailPointer();
    }

    private void buildTrieTree() {
        for (String targetStr : target) {
            Node curr = root;
            for (char ch : targetStr.toCharArray()) {
                curr = curr.table.computeIfAbsent(ch, c -> new Node());
            }
            curr.str = targetStr;
        }
    }

    private void buildFailPointer() {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();

            for (Map.Entry<Character, Node> entry : curr.table.entrySet()) {
                Node child = entry.getValue();
                char ch = entry.getKey();

                Node fail = curr.fail;
                while (fail != null && !fail.table.containsKey(ch)) {
                    fail = fail.fail;
                }

                if (fail == null) {
                    child.fail = root;
                } else {
                    Node target = fail.table.get(ch);
                    child.fail = (target != null) ? target : root;
                }

                queue.offer(child);
            }
        }
    }

    public Map<String, List<Integer>> find(String text) {
        result = new HashMap<>();
        for (String s : target) {
            result.put(s, new ArrayList<>());
        }

        Node curr = root;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            while (curr != null && !curr.table.containsKey(ch)) {
                curr = curr.fail;
            }
            if (curr == null) {
                curr = root;
            } else {
                curr = curr.table.get(ch);
                if (curr != null) {
                    if (curr.isWord()) {
                        result.get(curr.str).add(i - curr.str.length() + 1);
                    }
                    Node fail = curr.fail;
                    if (fail != null && fail.isWord()) {
                        result.get(fail.str).add(i - fail.str.length() + 1);
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> target = new ArrayList<>();
        target.add("你好");
        target.add("好人");
        target.add("是");
        target.add("我");
        target.add("个好");
        target.add("你好人");

        String text = "你是一个好人";

        AhoCorasickAutomation aca = new AhoCorasickAutomation(target);
        Map<String, List<Integer>> stringListMap = aca.find(text);

        System.out.println(text);
        for (Map.Entry<String, List<Integer>> entry : stringListMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}