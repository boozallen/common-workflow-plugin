/*
  Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
  This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl
*/

package org.jenkinsci.plugins.common.workflow;

import hudson.Extension;
import jenkins.branch.MultiBranchProject;
import org.jenkinsci.plugins.workflow.flow.FlowDefinition;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import javax.annotation.Nonnull;

public class ScriptBasedCommonWorkflowBranchProjectFactory extends AbstractCommonWorkflowBranchProjectFactory {
    private String script;
    private Boolean sandbox; 

    @DataBoundConstructor public ScriptBasedCommonWorkflowBranchProjectFactory() {}

    @DataBoundSetter 
    public void setScript(String _script){ script = _script; }
    public String getScript(){ return script; }

    @DataBoundSetter 
    public void setSandbox(Boolean _sandbox) { sandbox = _sandbox; }
    public Boolean getSandbox(){ return sandbox; }
    
    @Override protected FlowDefinition createDefinition() {
        return new CommonWorkflowScript(script, sandbox);
    }

    @Extension
    public static class DescriptorDefaultImpl extends AbstractWorkflowBranchProjectFactoryDescriptor {

        @Override
        public boolean isApplicable(Class<? extends MultiBranchProject> clazz) {
            return MultiBranchProject.class.isAssignableFrom(clazz);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "by common pipeline script"; 
        }

    }

}
