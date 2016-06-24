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

package org.pathirage.calcite.tutorial.planner.physical;

import org.apache.calcite.plan.*;

public enum  TConvention implements Convention {
  INSTANCE;

  @Override
  public Class getInterface() {
    return TRel.class;
  }

  @Override
  public String getName() {
    return "T_PHYSICAL";
  }

  @Override
  public RelTraitDef getTraitDef() {
    return ConventionTraitDef.INSTANCE;
  }

  @Override
  public boolean satisfies(RelTrait trait) {
    return this == trait;
  }

  @Override
  public void register(RelOptPlanner planner) {

  }

  @Override
  public String toString() {
    return getName();
  }
}
