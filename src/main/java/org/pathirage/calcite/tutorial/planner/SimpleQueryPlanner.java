package org.pathirage.calcite.tutorial.planner;


import com.google.common.io.Resources;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.plan.*;
import org.apache.calcite.rel.RelCollationTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.*;
import org.pathirage.calcite.tutorial.planner.physical.TConvention;
import org.pathirage.calcite.tutorial.planner.physical.TRel;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleQueryPlanner {

  private static final int TREL_CONVERSION_RULES = 0;

  private final Planner planner;

  public SimpleQueryPlanner(SchemaPlus schema) {
    final List<RelTraitDef> traitDefs = new ArrayList<RelTraitDef>();

    traitDefs.add(ConventionTraitDef.INSTANCE);
    traitDefs.add(RelCollationTraitDef.INSTANCE);

    FrameworkConfig calciteFrameworkConfig = Frameworks.newConfigBuilder()
        .parserConfig(SqlParser.configBuilder()
            // Lexical configuration defines how identifiers are quoted, whether they are converted to upper or lower
            // case when they are read, and whether identifiers are matched case-sensitively.
            .setLex(Lex.MYSQL)
            .build())
        // Sets the schema to use by the planner
        .defaultSchema(schema)
        .traitDefs(traitDefs)
        // Context provides a way to store data within the planner session that can be accessed in planner rules.
        .context(Contexts.EMPTY_CONTEXT)
        // Rule sets to use in transformation phases. Each transformation phase can use a different set of rules.
        .ruleSets(ConverterRuleSets.getRuleSets())
        // Custom cost factory to use during optimization
        .costFactory(null)
        .typeSystem(RelDataTypeSystem.DEFAULT)
        .build();

    this.planner = Frameworks.getPlanner(calciteFrameworkConfig);
  }

  public RelNode getLogicalPlan(String query) throws ValidationException, RelConversionException {
    SqlNode sqlNode;

    try {
      sqlNode = planner.parse(query);
    } catch (SqlParseException e) {
      throw new RuntimeException("Query parsing error.", e);
    }

    SqlNode validatedSqlNode = planner.validate(sqlNode);

    return planner.rel(validatedSqlNode).project();
  }

  public RelNode getTRel(String query) throws RelConversionException, ValidationException {
    RelNode relNode = getLogicalPlan(query);
    RelTraitSet traitSet = relNode.getTraitSet();
    traitSet = traitSet.simplify();
    return planner.transform(TREL_CONVERSION_RULES, traitSet.plus(TConvention.INSTANCE), relNode);
  }

  public static void main(String[] args) throws Exception {
    // Simple connection implementation for loading schema from sales.json
    CalciteConnection connection = new SimpleCalciteConnection();
    String salesSchema = Resources.toString(SimpleQueryPlanner.class.getResource("/sales.json"), Charset.defaultCharset());
    // ModelHandler reads the sales schema and load the schema to connection's root schema and sets the default schema
    new ModelHandler(connection, "inline:" + salesSchema);

    // Create the query planner with sales schema. conneciton.getSchema returns default schema name specified in sales.json
    SimpleQueryPlanner queryPlanner = new SimpleQueryPlanner(connection.getRootSchema().getSubSchema(connection.getSchema()));
    RelNode plan = queryPlanner.getTRel("select product from orders");
    System.out.println(RelOptUtil.toString(plan));
    ((TRel)plan).doSomething(0);
  }
}
