package zhou.lex;

public class TokenException extends Exception {
    private int rowNumber;
    private String msg;
    public TokenException(int rowNumber,String msg){
        this.rowNumber=rowNumber;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.getClass()+"\n在 "+rowNumber+"行发生错误!\nError message:"+msg;
    }
}
