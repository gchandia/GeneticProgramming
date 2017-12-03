package tree;

public class TerminalNode extends Node {
  public TerminalNode(int number, Node left, Node right) {
    super(number, left, right, String.valueOf(number));
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
  
  public OperatorNode cloneTree() {
    return new TerminalNode(this.number, null, null);
  }
}