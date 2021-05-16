package functions;

import java.util.List;

public class sTreeNode {
    private ExpKind kind;//节点类型
    private OPType operator;//运算符类型
    private int val;//常量值
    private StringBuilder name;//ID
    public List<sTreeNode> Children;
    public sTreeNode sibling;

    public enum ExpKind{
        operator,Num,Identifier,expression,IfStmt,Program,declaration,
        fun_declaration,var_declaration,var_num,delimiter,type_spec,
    }

    public enum OPType{
        plus("+"),
        minus("-"),
        mult("*"),
        div("/"),
        EQ("=="),
        NE("!="),
        GT(">"),
        LT("<"),
        GE(">="),
        LE("<="),
        assign("=");

        private String OP;
        OPType(String Op){
            this.OP=Op;
        }

        public String getOP(){
            return OP;
        }
    }

    public sTreeNode(){
    }

    public void setKind(ExpKind kind){
        this.kind=kind;
    }

    public void setOperator(OPType op){
        this.operator=op;
    }

    public void setVal(int val){
        this.val=val;
    }

    public void setName(StringBuilder n){this.name=n;}

    public ExpKind getKind(){
        return this.kind;
    }

    public OPType getOperator(){
        return operator;
    }

    public int getVal(){
        return val;
    }

    public StringBuilder getName(){
        return name;
    }
}
