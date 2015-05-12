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
    return computeErrorMsg(file, errorCharPositions);
  }

  private void getErrorPositions(List<Statement> statements,
      List<Integer> positions, String content) {
    for (Statement statement : statements) {
      if (statement instanceof IfNode) {
        IfNode ifNode = (IfNode) statement;
        if (content.charAt(ifNode.getPass().getStart()) != '{') {
          positions.add(ifNode.getTest().getFinish());
        }
      } else if (statement instanceof LoopNode) {
        LoopNode loopNode = (LoopNode) statement;
        if (content.charAt(loopNode.getBody().getStart()) != '{') {
          positions.add(loopNode.getTest().getFinish());
        }
      }
      getErrorPositions(PARSER.getStatements(statement), positions, content);
    }
  }

  private String computeErrorMsg(JsFileProperties file,
      List<Integer> errorCharPositions) {
    String[] content = file.toString().split("\n");
    StringBuilder messages = new StringBuilder();
    String name = file.getFileName();
    int cnt = 0;
    int currentIndex = 0;
    for (int i = 0; i < content.length; ++i) {
      if (currentIndex == errorCharPositions.size()) {
        break;
      }
      cnt += content[i].length() + 1;
      if (cnt >= errorCharPositions.get(currentIndex)) {
        messages
            .append("Missing curly brace (" + name + ":" + (i + 1) + ").\n");
        ++currentIndex;
      }
    }
    return messages.toString();
  }
}
