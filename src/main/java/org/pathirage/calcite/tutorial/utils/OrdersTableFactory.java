package org.pathirage.calcite.tutorial.utils;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.*;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.Map;

public class OrdersTableFactory implements TableFactory<Table> {
  @Override
  public Table create(SchemaPlus schema, String name, Map<String, Object> operand, RelDataType rowType) {
    final Object[][] rows = {
        {1, "paint", 10},
        {2, "paper", 5},
        {3, "brush", 12},
        {4, "paint", 3},
        {5, "paint", 3}
    };
    return new OrdersTable(ImmutableList.copyOf(rows));
  }

  public static class OrdersTable implements ScannableTable {
    protected final RelProtoDataType protoRowType = new RelProtoDataType() {
      public RelDataType apply(RelDataTypeFactory a0) {
        return a0.builder()
            .add("id", SqlTypeName.INTEGER)
            .add("product", SqlTypeName.VARCHAR, 10)
            .add("units", SqlTypeName.INTEGER)
            .build();
      }
    };

    private final ImmutableList<Object[]> rows;

    public OrdersTable(ImmutableList<Object[]> rows) {
      this.rows = rows;
    }

    public Enumerable<Object[]> scan(DataContext root) {
      return Linq4j.asEnumerable(rows);
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
      return protoRowType.apply(typeFactory);
    }

    @Override
    public Statistic getStatistic() {
      return Statistics.UNKNOWN;
    }

    @Override
    public Schema.TableType getJdbcTableType() {
      return Schema.TableType.TABLE;
    }
  }
}
