package zhou.lex;

public class Token {
    public final static int IDS=0;
    public final static String C_IDS="标识符";
    public final static int CNS = 1;//常数
    public final static String C_CNS="常数";
    public final static int KW = 2;//关键字
    public final static String C_KW="关键字";
    public final static int SEMI = 3;// 单界符;
    public final static String C_CEMI="单界符(;)";
    public final static int SLP = 4;//(
    public final static String C_LP="括号";
    public final static int SRP = 5; //)
    public final static int LP = 6;// {
    public final static int RP = 7;//}
    public final static String C_ASSIGN = "赋值符号";
    public final static int INC = 8; //++
    public final static int EQ = 9; // ==
    public final static int NE = 10; // !=
    public final static int DEC = 11; //--
    public final static int PLUS = 12;//+
    public final static int SUB  =13;
    public final static int INCEQ = 14;//+=
    public final static int DECEQ = 15;//-=
    public final static String C_OPER = "运算符";
    public final static String C_COMMON="注释";
    public final static String C_THAN = "比较符";
    public final static String C_SPL = "行语句分隔符";
    public final static int NOT = 16;// !
    public final static int ASSIGN = 17;//=
    public final static int LESSTHAN = 18;//<
    public final static int LESSEQ = 19;// <=
    public final static int THAN = 20;// >
    public final static int THANEQ= 21;//>=
    public final static int MUL = 22;// *
    public final static int MULEQ = 23; //*=
    public final static int DIV = 24; // /
    public final static int DIVEQ = 25;// /=
    public final static int MOD = 26; // %
    public final static int MODEQ = 27; //%=
    public final static int SPL=28;//,
    public final static int ALP = 29;//[
    public final static int ARP = 30; // ]
    public final static String C_RP="数组括号";
    public String value;
    public int type;
    public int line;
    public String common;

    /**
     *
     * @param v 标识符
     * @param t 标识符类型
     * @param common 对此标识符的注释
     */
    public Token(String v,int t,String common){
        this.value=v;
        this.type=t;
        this.common=common;
    }

}
