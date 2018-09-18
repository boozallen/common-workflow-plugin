/*
  Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
  This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl
*/

package org.jenkinsci.plugins.common.workflow;

import hudson.Extension;
import hudson.model.Job;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;
import jenkins.branch.MultiBranchProjectFactory;
import jenkins.branch.MultiBranchProjectFactoryDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import java.util.Collection;
import org.jenkinsci.plugins.workflow.multibranch.AbstractWorkflowBranchProjectFactory;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;

public class ScmBasedCommonWorkflowMultiBranchProjectFactory extends AbstractCommonWorkflowMultiBranchProjectFactory {
    private String scriptPath;
    private Boolean sandbox; 
    private SCM scm; 
    private Boolean lightweight;

    @DataBoundSetter public void setScriptPath(String _scriptPath){ scriptPath = _scriptPath; }
    public String getScriptPath(){ return scriptPath; }

    @DataBoundSetter public void setSandbox(Boolean _sandbox) { sandbox = _sandbox; }
    public Boolean getSandbox(){ return sandbox; }

    @DataBoundSetter public void setLightweight(Boolean _lightweight) { lightweight = _lightweight; }
    public Boolean getLightweight(){ return lightweight; }

    @DataBoundSetter public void setScm(SCM _scm){ scm = _scm; }
    public SCM getScm(){ return scm; }

    @DataBoundConstructor public ScmBasedCommonWorkflowMultiBranchProjectFactory(){}

    public Object readResolve() { return this; }

    private AbstractWorkflowBranchProjectFactory newProjectFactory() {
        ScmBasedCommonWorkflowBranchProjectFactory workflowBranchProjectFactory = new ScmBasedCommonWorkflowBranchProjectFactory();
        workflowBranchProjectFactory.setScriptPath(scriptPath);
        workflowBranchProjectFactory.setScm(scm);
        workflowBranchProjectFactory.setLightweight(lightweight);
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
            return null; // new ScmBasedCommonWorkflowMultiBranchProjectFactory();
        }

        @Override public String getDisplayName() {
            return "Common Pipeline Script From SCM";
        }

        public Collection<? extends SCMDescriptor<?>> getApplicableDescriptors() {
            StaplerRequest req = Stapler.getCurrentRequest();
            Job<?,?> job = req != null ? req.findAncestorObject(Job.class) : null;
            return SCM._for(job);
        }

    }

}