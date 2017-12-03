package tree;

public class TerminalX extends TerminalNode {
  public TerminalX() {
    super(0, "x");
  }
  
  public TerminalX(int number) {
    super(number, String.valueOf(number));
  }
  
  public OperatorNode cloneTree() {
    if (this.rep == "x") {
      return new TerminalX();
    }
    return new TerminalX(this.number);
  }
}