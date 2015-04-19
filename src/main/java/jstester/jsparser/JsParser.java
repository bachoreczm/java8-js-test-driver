package jstester.jsparser;

import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

public class JsParser {

  /**
   * @param jsCode
   *          javascript source
   * @return {@link List} of {@link Statement}s.
   */
  public List<Statement> parse(String jsCode) {
    Options options = new Options("nashorn");
    options.set("anon.functions", true);
    options.set("parse.only", true);
    options.set("scripting", true);

    ErrorManager errors = new ErrorManager();
    Thread currentThread = Thread.currentThread();
    ClassLoader contextClassLoader = currentThread.getContextClassLoader();
    Context context = new Context(options, errors, contextClassLoader);
    Source source = Source.sourceFor("test", jsCode);
    Parser parser = new Parser(context.getEnv(), source, errors);
    FunctionNode functionNode = parser.parse();
    Block block = functionNode.getBody();
    return block.getStatements();
  }

  /**
   * @param statement
   *          a javascript code statement
   * @return inner statements
   */
  public List<Statement> getStatements(Statement statement) {
    if (statement instanceof LoopNode) {
      return ((LoopNode) statement).getBody().getStatements();
    }
    if (statement instanceof VarNode) {
      return getStatements(((VarNode) statement).getAssignmentSource());
    }
    if (statement instanceof IfNode) {
      IfNode ifNode = (IfNode) statement;
      List<Statement> ifStatements = new ArrayList<>();
      ifStatements.addAll(ifNode.getPass().getStatements());
      ifStatements.addAll(ifNode.getFail().getStatements());
      return ifStatements;
    }
    if (statement instanceof ExpressionStatement) {
      ExpressionStatement expression = (ExpressionStatement) statement;
      return getStatements(expression.getExpression());
    }
    return new ArrayList<>();
  }

  private List<Statement> getStatements(Expression expression) {
    if (expression instanceof BinaryNode) {
      return getStatements(((BinaryNode) expression).getAssignmentSource());
    }
    if (expression instanceof FunctionNode) {
      return ((FunctionNode) expression).getBody().getStatements();
    }
    return new ArrayList<>();
  }
}
