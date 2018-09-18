/*
  Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
  This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl
*/

package org.jenkinsci.plugins.common.workflow;

import hudson.Extension;
import hudson.model.*;
import org.jenkinsci.plugins.workflow.cps.CpsFlowExecution;
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition;
import org.jenkinsci.plugins.workflow.flow.FlowDefinition;
import org.jenkinsci.plugins.workflow.flow.FlowDefinitionDescriptor;
import org.jenkinsci.plugins.workflow.flow.FlowExecutionOwner;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import hudson.scm.SCM;
import java.lang.reflect.Field;
import java.util.List;

class CommonWorkflowScmScript extends FlowDefinition {

    private String scriptPath; 
    private Boolean sandbox; 
    private SCM scm;
    private boolean lightweight; 

    public CommonWorkflowScmScript(){}

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public void setSandbox(Boolean sandbox) {
        this.sandbox = sandbox;
    }

    public void setLightweight(Boolean lightweight) {
        this.lightweight = lightweight;
    }

    public void setScm(SCM scm){
        this.scm = scm; 
    }

    @Override
    public CpsFlowExecution create(FlowExecutionOwner handle, TaskListener listener, List<? extends Action> actions) throws Exception {
        
        CpsScmFlowDefinition flowDef = new CpsScmFlowDefinition(scm, scriptPath);  
        flowDef.setLightweight(lightweight);
        CpsFlowExecution flow = flowDef.create(handle, listener, actions);
        
        Field f = flow.getClass().getDeclaredField("sandbox");
        f.setAccessible(true);
        f.set(flow, sandbox); 

        return flow; 
    }

    @Extension
    public static class DescriptorImpl extends FlowDefinitionDescriptor {

        @Override
        public String getDisplayName() {
            return "Common Pipeline script from SCM";
        }

    }

    /**
     * Want to display this in the r/o configuration for a branch project, but not offer it on standalone jobs or in any other context.
     */
    @Extension
    public static class HideMeElsewhere extends DescriptorVisibilityFilter {

        @Override
        public boolean filter(Object context, Descriptor descriptor) {
            if (descriptor instanceof DescriptorImpl) {
                return context instanceof WorkflowJob && ((WorkflowJob) context).getParent() instanceof WorkflowMultiBranchProject;
            }
            return true;
        }

    }
}
