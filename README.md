# Common Workflow Plugin

## Purpose

This purpose of this Jenkins plugin is to make it possible to use a single entrypoint
script, stored and maintained using an SCM tool such as GitHub, for an entire
organization. This simplifies the governance of the organization's CI/CD pipelines
while improving scalability and maintainability.

<!---
## Dependencies
TODO: add dependency plugins
--->

## Building

### Prerequisites:
* Maven (`mvn`) is installed on your machine
* Git has been installed and configured on your machine

### From the Command Line:
```
git clone https://github.com/boozallen/common-workflow-plugin.git
cd common-workflow-plugin
mvn clean install -DskipTests
```

## Installing

Unlike other Jenkins plugins, this one needs to be installed manually. After
building the plugin (see above), you should see a number of binary files in
a folder called "target." Take the common-workflow.hpi file in that folder and
copy it into the `$JENKINS_HOME/plugins` directory on your Jenkins Master server,
then restart the server.

## Using the Plugin

### Prerequisites
* If your project source code is in a private repository, create a Jenkins credential  
  that can, at minimum, read from those repositories.
* If the repository containing the entrypoint script you wish to use is in a private  
  repository, you will need a Jenkins credential to read from that repository as well.

### Steps

You can configure an existing "GitHub Organization" job or create a new one. To create
a new job, click on either of these two links.
![image](https://user-images.githubusercontent.com/10341296/45721754-5e284580-bb77-11e8-9186-336339ae9b5d.png)

Enter an "item name" as prompted - this is typically the name of your GitHub
Organization - then select the "GitHub Organization" job and click OK.
![image](https://user-images.githubusercontent.com/10341296/45721915-0938ff00-bb78-11e8-8492-05253e72f807.png)

You will then be taken to a screen to configure your new job. Scroll down to Projects and,
if your source code is not in a public repository, select the appropriate credentials from
the drop down. Otherwise select "none." Then delete the default Project Recognizer.
![image](https://user-images.githubusercontent.com/10341296/45722017-7ba9df00-bb78-11e8-9069-c2a136fdfaba.png)

Click Add to add a new project recognizer and select "Common Pipeline Script From SCM"
![image](https://user-images.githubusercontent.com/10341296/45722072-bc095d00-bb78-11e8-9410-abf7a2152910.png)

You should see a section pop up like the one below. Do the following
1. Select the source code management tool you will be using (likely Git)
2. Enter the Repository URL in the corresponding field for the repository   
   containing the entrypoint script you plan to use.  
   You can git this by clicking the "clone" button on the repository's page and copying the https URL
3. If the repository containing your entrypoint script is private, select the appropriate  
   credentials from the dropdown in the Credentials field. Otherwise select "none."
4. Enter the path of your entrypoint script in the Script Path field. The path is relative to the root  
   of the repository
5. Optionally uncheck "Use Groovy Sandbox"
![image](https://user-images.githubusercontent.com/10341296/45722105-df340c80-bb78-11e8-9da8-37d74af1d387.png)

Once that's done, select Save and a Multibranch Pipeline job will be automatically set up for each
repository in the Github organization.
