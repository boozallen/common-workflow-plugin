/*
  Copyright © 2018 Booz Allen Hamilton. All Rights Reserved.
  This software package is licensed under the Booz Allen Public License. The license can be found in the License file or at http://boozallen.github.io/licenses/bapl
*/

package org.jenkinsci.plugins.common.workflow;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.TaskListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import jenkins.branch.MultiBranchProject;
import jenkins.branch.MultiBranchProjectFactory;
import jenkins.branch.OrganizationFolder;
import jenkins.model.TransientActionFactory;
import jenkins.scm.api.SCMSource;
import jenkins.scm.api.SCMSourceCriteria;
import org.jenkinsci.plugins.workflow.cps.Snippetizer;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;

public abstract class AbstractCommonWorkflowMultiBranchProjectFactory extends MultiBranchProjectFactory.BySCMSourceCriteria {

    @Override protected final WorkflowMultiBranchProject doCreateProject(ItemGroup<?> parent, String name, Map<String,Object> attributes){
        WorkflowMultiBranchProject project = new WorkflowMultiBranchProject(parent, name);
        customize(project);
        return project;
    }

    @Override public final void updateExistingProject(MultiBranchProject<?, ?> project, Map<String, Object> attributes, TaskListener listener) throws IOException, InterruptedException {
        if (project instanceof WorkflowMultiBranchProject) {
            customize((WorkflowMultiBranchProject) project);
        } // otherwise got recognized by something else before, oh well
    }

    protected void customize(WorkflowMultiBranchProject project){}

    @Override protected SCMSourceCriteria getSCMSourceCriteria(SCMSource source) {
        return new SCMSourceCriteria() {
            private static final long serialVersionUID = 1L;

			@Override
            public boolean isHead(Probe probe, TaskListener listener) throws IOException {
                return true;
            }
        };
    }

    @Extension public static class PerFolderAdder extends TransientActionFactory<OrganizationFolder> {

        @Override public Class<OrganizationFolder> type() {
            return OrganizationFolder.class;
        }

        @Override public Collection<? extends Action> createFor(OrganizationFolder target) {
            if (target.getProjectFactories().get(AbstractCommonWorkflowMultiBranchProjectFactory.class) != null && target.hasPermission(Item.EXTENDED_READ)) {
                return Collections.singleton(new Snippetizer.LocalAction());
            } else {
                return Collections.emptySet();
            }
        }

    }

}