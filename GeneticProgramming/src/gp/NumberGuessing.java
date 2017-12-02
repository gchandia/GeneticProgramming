package gp;

import java.util.Random;
import tree.MultNode;
import tree.Node;
import tree.OperatorNode;
import tree.SubNode;
import tree.SumNode;
import tree.TerminalNode;

public class NumberGuessing {
  private int totalGenerations = 1;
  private Random random = new Random();
  
  //Generate base of the tree, avoids generating a single node
  private OperatorNode generateTree(int cap, int depth) {
    int value = random.nextInt(3);
    
    if (value == 0) {
      return new SumNode(generateRecursiveTree(cap, depth), generateRecursiveTree(cap, depth));
    } else if (value == 1) {
      return new SubNode(generateRecursiveTree(cap, depth), generateRecursiveTree(cap, depth));
    } else {
      return new MultNode(generateRecursiveTree(cap, depth), generateRecursiveTree(cap, depth));
    }
  }
  
  //Generate rest of the tree
  private OperatorNode generateRecursiveTree(int cap, int depth) {
    if (depth == 0) {
      return new TerminalNode(random.nextInt(cap), null, null);
    }
    
    int value = random.nextInt(5);
    
    if (value == 1) {
      return new SumNode(generateRecursiveTree(cap, depth - 1), generateRecursiveTree(cap, depth - 1));
    } else if (value == 2) {
      return new SubNode(generateRecursiveTree(cap, depth - 1), generateRecursiveTree(cap, depth - 1));
    } else if (value == 3) {
      return new MultNode(generateRecursiveTree(cap, depth - 1), generateRecursiveTree(cap, depth - 1));
    } else {
      return new TerminalNode(random.nextInt(cap), null, null);
    }
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
  
  private OperatorNode crossOver(OperatorNode dad, OperatorNode mom, int mutationRate, int solution) {
    int mutation = random.nextInt(100), decision = random.nextInt(2);
    OperatorNode cloneDad = dad.cloneTree(), cloneMom = mom.cloneTree();
    OperatorNode dadStopPoint = pickStop(cloneDad), momStopPoint = pickStop(cloneMom);
    
    if (mutation > mutationRate) {
      if (decision == 0) {
        dadStopPoint.setLeft(momStopPoint);
      } else {
        dadStopPoint.setRight(momStopPoint);
      }
    } else {
      if (decision == 0) {
        dadStopPoint.setLeft(generateTree(solution, 1));
      } else {
        dadStopPoint.setRight(generateTree(solution, 1));
      }
    }
    
    return cloneDad;
  }
  
  public OperatorNode[] initiatePopulation(int size, int solution) {
    OperatorNode[] population = new Node[size];
    
    for (int i = 0; i < size; i++) {
      population[i] = generateTree(solution, 3);
    }
    
    return population;
  }
  
  public boolean evaluateFitness(OperatorNode[] population, int solution) {
    boolean sequenceFound = false;
    
    for (int i = 0; i < population.length; i++) {
      int result = population[i].operate();
      if (result == solution) {
        sequenceFound = true;
        System.out.println("hi");
      }
      population[i].setFitness(Math.abs(result - solution));
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
  
  public OperatorNode[] reproduction(OperatorNode[] fittest, int size, int solution) {
    OperatorNode[] breed = new OperatorNode[size];
    OperatorNode dad, mom;
    int filled = 0;
    
    if (fittest[0].getFitness() < fittest[1].getFitness()) {
      dad = fittest[0]; mom = fittest[1];
    } else {
      mom = fittest[0]; dad = fittest[1];
    }
    
    while (filled < size) {
      breed[filled++] = crossOver(dad, mom, 5, solution);
    }
    
    return breed;
  }
  
  private void geneticAlgorithm(int size, int solution, OperatorNode[] initialPopulation) {
    OperatorNode[] population;
    if (initialPopulation == null) population = initiatePopulation(size, solution);
    else population = initialPopulation;
    
    if (evaluateFitness(population, solution)) {
      return;
    } else {
      OperatorNode[] fittest = new OperatorNode[2];
      fittest[0] = tournamentSelection(population);
      fittest[1] = tournamentSelection(population);
      OperatorNode[] breed = reproduction(fittest, size, solution);
      ++this.totalGenerations;
      geneticAlgorithm(size, solution, breed);
    }
  }
  
  private int getNumberOfGenerations() {
    return this.totalGenerations;
  }  
  
  public static void main(String[] args) {
    NumberGuessing gp = new NumberGuessing();
    int example = 42;
    
    long startTime = System.currentTimeMillis();
    gp.geneticAlgorithm(5, example, null);
    long stopTime = System.currentTimeMillis();
    
    System.out.println("Elapsed time is: " + (stopTime - startTime));
    System.out.println("Number of generations was: " + gp.getNumberOfGenerations());
  }
}