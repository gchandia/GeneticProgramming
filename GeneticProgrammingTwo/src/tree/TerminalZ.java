package tree;

public class TerminalZ extends TerminalNode {
  public TerminalZ() {
    super(0, "z");
  }
  
  public TerminalZ(int number) {
    super(number, String.valueOf(number));
  }
  
  public OperatorNode cloneTree() {
    if (this.rep == "z") {
      return new TerminalZ();
    }
    return new TerminalZ(this.number);
  }
}