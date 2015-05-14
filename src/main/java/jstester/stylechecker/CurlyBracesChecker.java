package jstester.stylechecker;

import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Statement;
import jstester.JsFileProperties;
import jstester.jsparser.JsParser;

class CurlyBracesChecker implements StyleRule {

  private static final JsParser PARSER = new JsParser();
  private final CurlyBracesErrorMessageComputer errorMessageComputer;

  CurlyBracesChecker() {
    errorMessageComputer = new CurlyBracesErrorMessageComputer();
  }

  @Override
  public String checkRule(JsFileProperties[] userCodes) {
    StringBuilder errors = new StringBuilder();
    for (JsFileProperties file : userCodes) {
      errors.append(getFileErrors(file));
    }
    return errors.toString();
  }

  private String getFileErrors(JsFileProperties file) {
    List<Statement> statements = PARSER.parse(file.toString());
    List<Integer> errorCharPositions = new ArrayList<Integer>();
    getErrorPositions(statements, errorCharPositions, file.toString());
    return errorMessageComputer.computeErrorMessage(file, errorCharPositions);
  }

  private void getErrorPositions(List<Statement> statements,
      List<Integer> positions, String content) {
    for (Statement statement : statements) {
      getErrorPositions(positions, content, statement);
    }
  }

  private void getErrorPositions(List<Integer> positions, String content,
      Statement statement) {
    if (statement instanceof IfNode) {
      IfNode ifNode = (IfNode) statement;
      ifStarCheck(positions, content, ifNode);
    } else if (statement instanceof LoopNode) {
      LoopNode loopNode = (LoopNode) statement;
      loopStartCheck(positions, content, loopNode);
    }
    getErrorPositions(PARSER.getStatements(statement), positions, content);
  }

  private void ifStarCheck(List<Integer> positions, String content,
      IfNode ifNode) {
    if (content.charAt(ifNode.getPass().getStart()) != '{') {
      positions.add(ifNode.getTest().getFinish());
    }
  }

  private void loopStartCheck(List<Integer> positions, String content,
      LoopNode loopNode) {
    if (content.charAt(loopNode.getBody().getStart()) != '{') {
      positions.add(loopNode.getTest().getFinish());
    }
  }
}
