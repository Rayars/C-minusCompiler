package functions;

public class sTreeNode {
    private ExpKind kind;//节点类型
    private TokenType op;//运算符类型
    private int val;//常量值
    private StringBuilder name;//ID
    public sTreeNode leftChild,rightChild;

    public enum ExpKind{
        operator,val,name,
    }

    public enum TokenType{
        compare,assign,op
    }

    public sTreeNode(){

    }
    public sTreeNode(ExpKind k,StringBuilder n){
        kind=k;name=n;
    }

    public sTreeNode(ExpKind k,TokenType tt){
        kind=k;op=tt;
    }

    public sTreeNode(ExpKind k,int v){
        kind=k;val=v;
    }
}
