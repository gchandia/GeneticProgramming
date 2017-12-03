package tree;

public abstract class TerminalNode extends Node {
  public TerminalNode(int number, String rep) {
    super(number, null, null, rep);
  }
  
  public boolean isTerminal() {
    return true;
  }
  
  public int operate() {
    return this.number;
  }
  
  public String printContent() {
    return this.rep;
  }
}