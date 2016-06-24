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

package org.pathirage.calcite.tutorial.planner.physical.rules;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.logical.LogicalProject;
import org.pathirage.calcite.tutorial.planner.physical.TConvention;
import org.pathirage.calcite.tutorial.planner.physical.TProjectRel;

public class TProjectRule extends ConverterRule {
  public static final TProjectRule INSTANCE = new TProjectRule();

  private TProjectRule() {
    super(LogicalProject.class, Convention.NONE, TConvention.INSTANCE, "TProjectRule");
  }

  @Override
  public RelNode convert(RelNode rel) {
    final Project project = (Project) rel;
    final RelNode input = project.getInput();

    return new TProjectRel(project.getCluster(),
        project.getTraitSet().replace(TConvention.INSTANCE),
        convert(input, input.getTraitSet().replace(TConvention.INSTANCE)), project.getProjects(), project.getRowType());
  }
}
