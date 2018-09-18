/*
  Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
  This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl
*/

package org.jenkinsci.plugins.common.workflow;

import hudson.Extension;
import hudson.model.Job;
import jenkins.branch.MultiBranchProject;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;
import org.jenkinsci.plugins.workflow.flow.FlowDefinition;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import javax.annotation.Nonnull;
import java.util.Collection;

public class ScmBasedCommonWorkflowBranchProjectFactory extends AbstractCommonWorkflowBranchProjectFactory {
    private String scriptPath;
    private Boolean sandbox; 
    private SCM scm; 
    private Boolean lightweight;

    @DataBoundConstructor public ScmBasedCommonWorkflowBranchProjectFactory(){}

    @DataBoundSetter public void setScriptPath(String _scriptPath){ scriptPath = _scriptPath; }
    public String getScriptPath(){ return scriptPath; }

    @DataBoundSetter public void setSandbox(Boolean _sandbox) { sandbox = _sandbox; }
    public Boolean getSandbox(){ return sandbox; }

    @DataBoundSetter public void setLightweight(Boolean _lightweight) { lightweight = _lightweight; }
    public Boolean getLightweight(){ return lightweight; }

    @DataBoundSetter public void setScm(SCM _scm){ scm = _scm; }
    public SCM getScm(){ return scm; }

    @Override protected FlowDefinition createDefinition() {
        CommonWorkflowScmScript script = new CommonWorkflowScmScript();
        script.setScriptPath(scriptPath);
        script.setScm(scm);
        script.setLightweight(lightweight);
        script.setSandbox(sandbox);
        return script;
    }

    @Extension
    public static class DescriptorImpl extends AbstractWorkflowBranchProjectFactoryDescriptor {

        @Override
        public boolean isApplicable(Class<? extends MultiBranchProject> clazz) {
            return MultiBranchProject.class.isAssignableFrom(clazz);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "by common pipeline script from scm"; 
        }

        public Collection<? extends SCMDescriptor<?>> getApplicableDescriptors() {
            StaplerRequest req = Stapler.getCurrentRequest();
            Job<?,?> job = req != null ? req.findAncestorObject(Job.class) : null;
            return SCM._for(job);
        }

    }

}
