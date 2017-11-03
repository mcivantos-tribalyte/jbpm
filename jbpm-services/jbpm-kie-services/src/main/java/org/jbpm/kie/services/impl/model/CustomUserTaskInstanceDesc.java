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

package org.jbpm.kie.services.impl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CustomUserTaskInstanceDesc implements org.jbpm.services.api.model.CustomUserTaskInstanceDesc, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String actualOwner;
    private String createdBy;
    private Date createdOn;
    private Date expirationTime;
    private Long taskId;
    private String name;
    private Integer priority;
    private Long processInstanceId;
    private String processId;
    private String status;
    private List<String> potOwner = new ArrayList<String>();
    private String taskType;
    private String correlationKey;
    private Date lastModificationDate;

    
    public CustomUserTaskInstanceDesc(String actualOwner, String createdBy,
                                      Date createdOn,Date expirationTime,
                                      Long taskId, String name, 
                                      Integer priority, Long processInstanceId,
                                      String processId, String status,
                                      String potOwner, String correlationKey, 
                                      String taskType,Date lastModificationDate) {
        //super();
        this.taskId = taskId;
        this.status = status;
        this.actualOwner = actualOwner;
        this.name = name;
        this.expirationTime = expirationTime;
        this.priority = priority;
        this.actualOwner = actualOwner;
        this.createdBy = createdBy;
        this.potOwner.add(potOwner);
        this.processId = processId;
        this.processInstanceId = processInstanceId;
        this.createdOn = createdOn;
        this.correlationKey = correlationKey;
        this.taskType = taskType;
        this.lastModificationDate = lastModificationDate;
    }
    
    @Override
    public String getActualOwner() {
        
        return this.actualOwner;
    }

    @Override
    public String getCreatedBy() {
        
        return this.createdBy;
    }

    @Override
    public Date getCreatedOn() {
        
        return this.createdOn;
    }

    @Override
    public Date getExpirationTime() {
        
        return this.expirationTime;
    }

    @Override
    public Long getTaskId() {
        
        return this.taskId;
    }

    @Override
    public String getName() {
        
        return this.name;
    }

    @Override
    public Integer getPriority() {
        
        return this.priority;
    }

    
    @Override
    public Long getProcessInstanceId() {
        
        return this.processInstanceId;
    }

    @Override
    public String getProcessId() {
        
        return this.processId;
    }

    @Override
    public String getStatus() {
        
        return this.status;
    }

    @Override
    public List<String> getPotOwner() {
        
        return this.potOwner;
    }
    
    @Override
    public String getCorrelationKey() {
        
        return this.correlationKey;
    }

    @Override
    public Date getLastModificationDate() {
        
        return this.lastModificationDate;
    }
    
    @Override
    public String getTaskType() {
        
        return this.taskType;
    }    

    public void setActualOwner(String actualOwner) {
        this.actualOwner = actualOwner;
    }

    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    
    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public void setPotOwner(List<String> potOwner) {
        this.potOwner = potOwner;
    }
    
    public void addPotOwner(String potOwner) {
        this.potOwner.add(potOwner);
    }
    
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    
    public void setCorrelationKey(String correlationKey) {
        this.correlationKey = correlationKey;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
    
    @Override
    public String toString() {
        return "CustomUserTaskInstance [actualOwner=" + actualOwner + ", createdBy=" + createdBy
                + ", createdOn=" + createdOn + ", expirationTime=" + expirationTime 
                + ", taskId=" + taskId + ", name=" + name
                + ", priority=" + priority + ", processInstanceId=" + processInstanceId
                + ", processId=" + processId + ", status=" + status
                + ", potOwner=" + potOwner + ", taskType=" + taskType
                + ", correlationKey=" + correlationKey + ", lastModificationDate=" + lastModificationDate+ "]";
    }

}
