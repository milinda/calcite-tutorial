/**
 * Copyright 2016 Milinda Pathirage
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pathirage.calcite.tutorial.planner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.tools.RuleSet;
import org.pathirage.calcite.tutorial.planner.physical.rules.TProjectRule;
import org.pathirage.calcite.tutorial.planner.physical.rules.TTableScanRule;

import java.util.Iterator;

public class ConverterRuleSets {

  private static final ImmutableSet<RelOptRule> converterRules = ImmutableSet.<RelOptRule>builder().add(
      TProjectRule.INSTANCE,
      TTableScanRule.INSTANCE
  ).build();

  public static RuleSet[] getRuleSets() {
    return new RuleSet[]{new TRuleSet(converterRules)};
  }

  public static class TRuleSet implements RuleSet {
    final ImmutableSet<RelOptRule> rules;


    public TRuleSet(ImmutableSet<RelOptRule> rules) {
      this.rules = rules;
    }

    public TRuleSet(ImmutableList<RelOptRule> rules) {
      this.rules = ImmutableSet.<RelOptRule>builder().addAll(rules).build();
    }

    @Override
    public Iterator<RelOptRule> iterator() {
      return rules.iterator();
    }
  }
}
