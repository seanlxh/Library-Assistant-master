package library.assistant.service; /**
 * author lixiaohe
 * date 2017-11-10
 */


import library.assistant.model.Node;
import library.assistant.model.Tree;
import library.assistant.model.charNone;
import library.assistant.ui.main.MainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


//分析函数的函数
public class Service {
    public  int countLeft = 0;
    public  int countRight = 0;
    public  ArrayList<String> result = new ArrayList<String>();
    public  ArrayList<String> getFunctionList(int leftNum ,int rightNum, String fileddress){
        countLeft = leftNum;
        countRight = rightNum;
        ArrayList<String> funtions = new ArrayList<String>();
        ArrayList<String> judges = new ArrayList<String>();
        HashMap<String,Integer> mp = new HashMap<String,Integer>();
        Stack<Node> stackIn = new Stack<Node>();
        Stack<Node> stackOut = new Stack<Node>();
        Csv csv = new Csv();
        csv.readeCsv(funtions , judges , mp , fileddress);
        int cur = 1;
        for(int i = 0 ; i < funtions.size() ; i ++) {
            // System.out.println(mp.get(funtions.get(i)));
            if (mp.get(funtions.get(i)) < countLeft * 2 || mp.get(funtions.get(i)) > countRight * 2){
                if(mp.get(funtions.get(i)) < countLeft * 2){
                    MainController.resultCode = "左边界过大";
                }else if(mp.get(funtions.get(i)) > countRight * 2){
                    MainController.resultCode = "右边界过小";
                }
                continue;
            }
            boolean judgeCharNone = false;
            for (int m = 0; m < charNone.array.length; m++) {
                if (funtions.get(i).contains(charNone.array[m])){
                    judgeCharNone = true;
                    break;
                }
            }
//            if(judgeCharNone)
//                continue;
                if (judges.get(i).equals("false")) {
                    Node node = new Node();
                    node.content = funtions.get(i);
                    node.floor = cur++;
                    stackIn.push(node);
                } else if (judges.get(i).equals("true")) {
                    Node tmp = stackIn.pop();
                    stackOut.push(tmp);
                    cur--;
                }
            }
            Tree root = new Tree();
            Tree curTree = root;
            while (!stackOut.empty()) {
                Node tmpnode = stackOut.pop();
                if (tmpnode.floor == curTree.floor + 1) {
                    if (curTree.child == null) {
                        Tree newTree = new Tree();
                        newTree.function = tmpnode.content;
                        newTree.floor = tmpnode.floor;
                        newTree.parent = curTree;
                        curTree.child = newTree;
                        curTree = newTree;
                    } else {
                        Tree tmp = new Tree();
                        Tree newTree = curTree.child;
                        while (newTree != null) {
                            tmp = newTree;
                            newTree = newTree.brother;
                        }
                        newTree = new Tree();
                        newTree.function = tmpnode.content;
                        newTree.floor = tmpnode.floor;
                        newTree.parent = curTree;
                        tmp.brother = newTree;
                        curTree = newTree;
                    }
                } else if (tmpnode.floor == curTree.floor) {
                    Tree tmp = new Tree();
                    Tree newTree = curTree;
                    while (newTree != null) {
                        tmp = newTree;
                        newTree = newTree.brother;
                    }
                    newTree = new Tree();
                    newTree.function = tmpnode.content;
                    newTree.floor = tmpnode.floor;
                    newTree.parent = curTree.parent;
                    tmp.brother = newTree;
                    curTree = newTree;
                } else if (tmpnode.floor < curTree.floor) {
                    while (tmpnode.floor < curTree.floor) {
                        curTree = curTree.parent;
                    }
                    Tree tmp = new Tree();
                    Tree newTree = curTree;
                    while (newTree != null) {
                        tmp = newTree;
                        newTree = newTree.brother;
                    }
                    newTree = new Tree();
                    newTree.function = tmpnode.content;
                    newTree.floor = tmpnode.floor;
                    newTree.parent = curTree.parent;
                    tmp.brother = newTree;
                    curTree = newTree;
                } else {
                    System.out.println("cuowu1");
//TODO
                }
            }
            System.out.println("********");
      Tree search = root.child;
      /*  while(search!=null){
            Tree a = search;
            boolean judge = true;
            for(int m = 0 ; m < count - 1 ; m ++){
                if(a.brother !=  null) {
                    if (!a.equal(a.brother)) {
                        judge = false;
                        break;
                    }
                    a = a.brother;
                }
                else {
                    judge = false;
                    break;
                }
            }
            if(judge){
                search = search.child;
            }
            else{
                break;
            }
        }
        */
        cursive(search);
        /*for(int i = 0 ; i < result.size()/4 ; i ++){
            System.out.println(result.get(i));
        }*/
        return result;
    }
    public void cursive(Tree search){
            Tree cur_floor = search;
            while(cur_floor != null){
                result.add(cur_floor.function+cur_floor.floor);
                if(cur_floor.child != null){
                    cursive(cur_floor.child);
                }
                cur_floor = cur_floor.brother;
            }
        return;
    }
    public void reverseList(ArrayList<String> data , ArrayList<String> reverseData, int target, int ceiling){
        for(int i = data.size() - 1; i >= ceiling ; i --){
            if(Integer.valueOf(data.get(i).split("\\)")[data.get(i).split("\\)").length-1]) == target){
                reverseData.add(data.get(i));
                data.remove(i);
                reverseList(data, reverseData, target+1, i);
            }
        }
    }
}
