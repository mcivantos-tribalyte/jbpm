/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.jbpm.kie.services.impl.query.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dashbuilder.dataset.filter.ColumnFilter;
import org.dashbuilder.dataset.filter.FilterFactory;
import org.jbpm.runtime.manager.impl.identity.UserDataServiceProvider;
import org.jbpm.services.api.query.QueryParamBuilder;
import org.kie.api.task.UserGroupCallback;

public class UserTaskPotOwnerQueryBuilder implements QueryParamBuilder<ColumnFilter>{

    private Map<String, Object> parameters;
    private boolean built = false;
    
    public UserTaskPotOwnerQueryBuilder(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public ColumnFilter build() {
     // return null if it was already invoked
        if (built) {
            return null;
        }
         
        String columnName = "potOwner";
        UserGroupCallback userGroupCallback = UserDataServiceProvider.getUserGroupCallback();
        List<String> potOwnerGoups = new ArrayList<String>();
        potOwnerGoups.add("HR");
        potOwnerGoups.add("Administrator");
        potOwnerGoups.add("kie-server");
        
        
      //userGroupCallback.getGroupsForUser((String)parameters.get("potOwner").toString())
        ColumnFilter filter = FilterFactory.AND(
                FilterFactory.in((String)parameters.get("potOwner").toString(),potOwnerGoups));
        filter.setColumnId(columnName);
        
        built = true;
        return filter;
    }    
}
