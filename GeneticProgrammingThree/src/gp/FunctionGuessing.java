package gp;

import java.util.Random;
import tree.MultNode;
import tree.Node;
import tree.OperatorNode;
import tree.SubNode;
import tree.SumNode;
import tree.TerminalX;

/**
 * Given a function p(x), find h(x) such as p(x) = h(x) for x in [-1,1].
 * Will only test for x = 0, x = 1, x = -1.
 * @author Gabriel Chandia
 *
 */
public class FunctionGuessing {
  private int totalGenerations = 1;
  private Random random = new Random();
  
  //Generate base of the tree, avoids generating a single node
  private OperatorNode generateTree(int depth) {
    int value = random.nextInt(3);
    
    if (value == 0) {
      return new SumNode(generateRecursiveTree(depth), generateRecursiveTree(depth));
    } else if (value == 1) {
      return new SubNode(generateRecursiveTree(depth), generateRecursiveTree(depth));
    } else {
      return new MultNode(generateRecursiveTree(depth), generateRecursiveTree(depth));
    }
  }
  
  //Generate rest of the tree
  private OperatorNode generateRecursiveTree(int depth) {
    if (depth == 0) {
       return new TerminalX();
    }
    
    int value = random.nextInt(5);
    
    if (value == 1) {
      return new SumNode(generateRecursiveTree(depth - 1), generateRecursiveTree(depth - 1));
    } else if (value == 2) {
      return new SubNode(generateRecursiveTree(depth - 1), generateRecursiveTree(depth - 1));
    } else if (value == 3) {
      return new MultNode(generateRecursiveTree(depth - 1), generateRecursiveTree(depth - 1));
    } else {
      return new TerminalX();
    }
  }
  
  private void parse(OperatorNode root, int replacement) {
    if (root.getLeft().isTerminal() && root.getRight().isTerminal()) {
      root.setLeft(new TerminalX(replacement));
      root.setRight(new TerminalX(replacement));
    } else if (root.getLeft().isTerminal() && !root.getRight().isTerminal()) {
      root.setLeft(new TerminalX(replacement));
      parse(root.getRight(), replacement);
    } else if (root.getRight().isTerminal() && !root.getLeft().isTerminal()) {
      root.setRight(new TerminalX(replacement)); 
      parse(root.getLeft(), replacement);
    } else {
      parse(root.getLeft(), replacement);
      parse(root.getRight(), replacement);
    }
  }
  
  private int evaluate(OperatorNode clone, int[] solutions) {
    int evaluation = 0;
    int compare = -1;
    for (int i = 0; i < solutions.length; i++) {
      parse(clone, solutions[i]);
      evaluation += Math.abs(clone.operate() - compare++);
    }
    return evaluation;
  }
  
  private OperatorNode pickStop(OperatorNode root) {
    if(root.isTerminal()) {
      return root;
    }
    
    int decision = random.nextInt(3);
    if (decision == 0) {
      return pickStop(root.getLeft());
    } else if (decision == 1) {
      return root;
    } else {
      return pickStop(root.getRight());
    }
  }
  
  private OperatorNode crossOver(OperatorNode dad, OperatorNode mom, int mutationRate) {
    int mutation = random.nextInt(100), decision = random.nextInt(2);
    OperatorNode cloneDad = dad.cloneTree(), cloneMom = mom.cloneTree();
    OperatorNode dadStopPoint = pickStop(cloneDad), momStopPoint = pickStop(cloneMom);
    
    if (mutation > mutationRate) {
      if (decision == 0) {
        dadStopPoint.setLeft(momStopPoint);
      } else {
        dadStopPoint.setRight(momStopPoint);
      }
    } else {    //Generate random subtree of depth 1
      if (decision == 0) {
        dadStopPoint.setLeft(generateTree(1));
      } else {
        dadStopPoint.setRight(generateTree(1));
      }
    }
    
    return cloneDad;
  }
  
  public OperatorNode[] initiatePopulation(int size) {
    OperatorNode[] population = new Node[size];
    
    for (int i = 0; i < size; i++) {
      population[i] = generateTree(3); //Depth of 3
    }
    
    return population;
  }
  
  public boolean evaluateFitness(OperatorNode[] population, int[] set) {
    boolean sequenceFound = false;
    
    for (int i = 0; i < population.length; i++) {
      OperatorNode clone = population[i].cloneTree();
      int result = evaluate(clone, set);
      if (result == 0) {
        sequenceFound = true;
        System.out.println("Content of tree (in reverse order): " + population[i].printContent());
      }
      population[i].setFitness(Math.abs(result));
    }
    
    return sequenceFound;
  }
  
  //There must be a population of at least 4 for this to work
  public OperatorNode tournamentSelection(OperatorNode[] firstGen) {
    OperatorNode[] tournament = new OperatorNode[4];
    OperatorNode finalistOne, finalistTwo;
    
    for (int i = 0; i < 4; i++) {
      tournament[i] = firstGen[random.nextInt(firstGen.length)];
    }
    
    if (tournament[0].getFitness() < tournament[1].getFitness()) {
      finalistOne = tournament[0];
    } else {
      finalistOne = tournament[1];
    }
    
    if (tournament[2].getFitness() < tournament[3].getFitness()) {
      finalistTwo = tournament[2];
    } else {
      finalistTwo = tournament[3];
    }
    
    if (finalistOne.getFitness() < finalistTwo.getFitness()) {
      return finalistOne;
    } else {
      return finalistTwo;
    }
  }
  
  public OperatorNode[] reproduction(OperatorNode[] fittest, int size) {
    OperatorNode[] breed = new OperatorNode[size];
    OperatorNode dad, mom;
    int filled = 0;
    
    if (fittest[0].getFitness() < fittest[1].getFitness()) {
      dad = fittest[0]; mom = fittest[1];
    } else {
      mom = fittest[0]; dad = fittest[1];
    }
    
    while (filled < size) {
      breed[filled++] = crossOver(dad, mom, 5);
    }
    
    return breed;
  }
  
  private void geneticAlgorithm(int size, int[] set, OperatorNode[] initialPopulation) {
    if (totalGenerations == 50) {
      System.out.println("At 50 gens");
      System.out.println(initialPopulation[0].printContent());
    }
    if (totalGenerations == 100) {
      System.out.println("Solution not found :/");
      System.out.println(initialPopulation[0].printContent());
      return;
    }
    OperatorNode[] population;
    if (initialPopulation == null) population = initiatePopulation(size);
    else population = initialPopulation;
    
    if (evaluateFitness(population, set)) {
      return;
    } else {
      OperatorNode[] fittest = new OperatorNode[2];
      fittest[0] = tournamentSelection(population);
      fittest[1] = tournamentSelection(population);
      OperatorNode[] breed = reproduction(fittest, size);
      ++this.totalGenerations;
      geneticAlgorithm(size, set, breed);
    }
  }
  
  private int getNumberOfGenerations() {
    return this.totalGenerations;
  }  
  
  public static void main(String[] args) {
    FunctionGuessing gp = new FunctionGuessing();
    //Function is p(x) = x^2 + x + 1
    //p(-1) = 1
    //p(0) = 1
    //p(1) = 3
    int[] example = {1, 1, 3};
    
    long startTime = System.currentTimeMillis();
    gp.geneticAlgorithm(4, example, null);
    long stopTime = System.currentTimeMillis();
    
    System.out.println("Elapsed time is: " + (stopTime - startTime));
    System.out.println("Number of generations was: " + gp.getNumberOfGenerations());
  }
}