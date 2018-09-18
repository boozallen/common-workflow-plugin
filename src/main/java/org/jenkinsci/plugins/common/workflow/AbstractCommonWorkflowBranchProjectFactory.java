/*
  Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
  This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl
*/

package org.jenkinsci.plugins.common.workflow;

import hudson.model.TaskListener;
import jenkins.scm.api.SCMSource;
import jenkins.scm.api.SCMSourceCriteria;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory;
import java.io.IOException;

public class AbstractCommonWorkflowBranchProjectFactory extends WorkflowBranchProjectFactory {
    private String script;
    private Boolean sandbox; 

    public Object  readResolve() { return this; }
    public Boolean getSandbox()  { return sandbox; }
    public String  getScript()   { return script; }

    @Override
    protected SCMSourceCriteria getSCMSourceCriteria(SCMSource source) {
        return new SCMSourceCriteria() {
            @Override
            public boolean isHead(Probe probe, TaskListener listener) throws IOException {
                return true;
            }
        };
    }

}
