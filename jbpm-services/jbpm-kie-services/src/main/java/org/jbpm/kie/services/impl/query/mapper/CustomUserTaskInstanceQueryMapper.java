/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.kie.services.impl.query.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dashbuilder.dataset.DataSet;
import org.jbpm.services.api.model.CustomUserTaskInstanceDesc;
import org.jbpm.services.api.query.QueryResultMapper;

public class CustomUserTaskInstanceQueryMapper extends AbstractQueryMapper<CustomUserTaskInstanceDesc> implements QueryResultMapper<List<CustomUserTaskInstanceDesc>> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Dedicated for ServiceLoader to create instance, use <code>get()</code> method instead 
     */
    public CustomUserTaskInstanceQueryMapper() {
        super();
    }
    
    public static CustomUserTaskInstanceQueryMapper get() {
        return new CustomUserTaskInstanceQueryMapper();
    }
    
    @Override
    public List<CustomUserTaskInstanceDesc> map(Object result) {
        if (result instanceof DataSet) {
            DataSet dataSetResult = (DataSet) result;
            List<CustomUserTaskInstanceDesc> mappedResult = new ArrayList<CustomUserTaskInstanceDesc>();
            
                                                                
            if (dataSetResult != null) {
                Map<Long, CustomUserTaskInstanceDesc> tmp = new HashMap<Long, CustomUserTaskInstanceDesc>();
                
                for (int i = 0; i < dataSetResult.getRowCount(); i++) {
                    Long taskId = getColumnLongValue(dataSetResult, COLUMN_TASKID, i);
                    CustomUserTaskInstanceDesc ut = tmp.get(taskId);
                    if (ut == null) {

                        ut = buildInstance(dataSetResult, i);
                        mappedResult.add(ut);    
                        
                        tmp.put(taskId, ut);
                    }else if(getColumnStringValue(dataSetResult, COLUMN_POTOWNER, i) != null){
                        ut.addPotOwner(getColumnStringValue(dataSetResult, COLUMN_POTOWNER, i));
                    }  
                                    
                }
            }
            
            return mappedResult;
        }
        
        throw new IllegalArgumentException("Unsupported result for mapping " + result);
    }

    @Override
    public String getName() {
        return "CustomUserTasks";
    }

    @Override
    public Class<?> getType() {
        return CustomUserTaskInstanceDesc.class;
    }

    @Override
    public QueryResultMapper<List<CustomUserTaskInstanceDesc>> forColumnMapping(Map<String, String> columnMapping) {
        return new CustomUserTaskInstanceQueryMapper();
    }

    @Override
    protected CustomUserTaskInstanceDesc buildInstance(DataSet dataSetResult, int index) {
        
        CustomUserTaskInstanceDesc customUserTask = new org.jbpm.kie.services.impl.model.CustomUserTaskInstanceDesc(
              getColumnStringValue(dataSetResult, COLUMN_ACTUALOWNER, index),//actualOwner,
              getColumnStringValue(dataSetResult, COLUMN_CREATEDBY, index),//createdBy,
              getColumnDateValue(dataSetResult, COLUMN_CREATEDON, index),//createdOn,
              getColumnDateValue(dataSetResult, COLUMN_EXPIRATIONTIME, index),//expirationtime,
              getColumnLongValue(dataSetResult, COLUMN_TASKID, index),//taskId,
              getColumnStringValue(dataSetResult, COLUMN_NAME, index),//name,
              getColumnIntValue(dataSetResult, COLUMN_PRIORITY, index),//priority,
              getColumnLongValue(dataSetResult, COLUMN_TASK_PROCESSINSTANCEID, index),//processInstanceId,
              getColumnStringValue(dataSetResult, COLUMN_TASK_PROCESSID, index),//processId,
              getColumnStringValue(dataSetResult, COLUMN_TASK_STATUS, index),//status,
              getColumnStringValue(dataSetResult, COLUMN_POTOWNER, index),//potowner,
              getColumnStringValue(dataSetResult, COLUMN_CORRELATIONKEY, index),//correlationkey,
              getColumnStringValue(dataSetResult, COLUMN_TASK_TYPE, index),//tasktype,
              getColumnDateValue(dataSetResult, COLUMN_LASTMODIFICATION_DATE, index)//lastmodificationdate,
              );
        return customUserTask;
    }

}
