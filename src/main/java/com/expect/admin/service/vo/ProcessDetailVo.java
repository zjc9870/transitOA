package com.expect.admin.service.vo;

import java.util.List;

/**
 * Created by qifeng on 17/12/8.
 */
public class ProcessDetailVo {
    String nodeId;
    String nodeName;
    List<String> roleId;
    List<String> roleName;
    String processId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<String> getRoleName() {
        return roleName;
    }

    public List<String> getRoleId() {
        return roleId;
    }

    public void setRoleId(List<String> roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(List<String> roleName) {
        this.roleName = roleName;

    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
