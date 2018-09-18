/*
  Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
  This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl
*/

package org.jenkinsci.plugins.common.workflow;

import hudson.Extension;
import jenkins.branch.MultiBranchProjectFactory;
import jenkins.branch.MultiBranchProjectFactoryDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.jenkinsci.plugins.workflow.multibranch.AbstractWorkflowBranchProjectFactory;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;

public class ScriptBasedCommonWorkflowMultiBranchProjectFactory extends AbstractCommonWorkflowMultiBranchProjectFactory {
    private String script;
    private Boolean sandbox;

    @DataBoundConstructor public ScriptBasedCommonWorkflowMultiBranchProjectFactory() { }

    @DataBoundSetter
    public void setScript(String _script) { script = _script; }
    public String getScript() { return script; }

    @DataBoundSetter
    public void setSandbox(Boolean _sandbox) { sandbox = _sandbox; }
    public Boolean getSandbox() { return sandbox; }

    public Object readResolve() { return this; }

    private AbstractWorkflowBranchProjectFactory newProjectFactory() {
        ScriptBasedCommonWorkflowBranchProjectFactory workflowBranchProjectFactory = new ScriptBasedCommonWorkflowBranchProjectFactory();
        workflowBranchProjectFactory.setScript(script);
        workflowBranchProjectFactory.setSandbox(sandbox);
        return workflowBranchProjectFactory;
    }

    @Override
    protected void customize(WorkflowMultiBranchProject project){
        project.setProjectFactory(newProjectFactory());
    }

    @Extension public static class DescriptorImpl extends MultiBranchProjectFactoryDescriptor {

        /* 
            returning the factory will result in this option being
            selected by default in multibranch factory jobs (github org job)
        */
        @Override public MultiBranchProjectFactory newInstance() {
            return null; // new CommonWorkflowMultiBranchProjectFactory();
        }

        @Override public String getDisplayName() {
            return "Common Pipeline Script";
        }

    }

}