package tree;

public class SubNode extends Node {
  public SubNode(OperatorNode left, OperatorNode right) {
    super(0, left, right, "-");
  }
  
  public int operate() {
    return this.getLeft().operate() - this.getRight().operate();
  }
  
  public boolean isFunction() {
    return true;
  }
  
  public OperatorNode cloneTree() {
    return new SubNode(this.getLeft().cloneTree(), this.getRight().cloneTree());
  }
}