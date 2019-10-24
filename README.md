# Palladio-Bench-Oomph
This repository contains the template and the workflow to generate the Setup definition to be used by Eclipse Installer.

## Setup Palladio Development Environment
The Eclipse Installer provides assistance with setting up an Eclipse-based development environment. The automated setup includes initial source code handling (git clone) as well as resolving of additional dependencies. The IDE instances which are configured through the Eclipse Installer share a common bundle and feature pool (by default <UserFolder>/.p2) to reduce duplication of binary artifacts.
  
In order to set-up your Palladio IDE using Eclipse Installer you need to excercise the following steps
1. Download and install Eclipse Installer on your local machine (https://www.eclipse.org/downloads/packages/installer)
2. Provide the Eclipse Installer with a reference to the Palladio Product and Project Catalogs. Either add the following snipped to the end of your eclipse-inst.ini file (same folder as the Eclipse Installer executable) or provide them as additional arguments when starting eclipse-inst.exe.
```
-Doomph.redirection.palladio_products=index:/redirectable.products.setup->https://updatesite.palladio-simulator.com/palladio-bench-oomph/nightly/plain/setups/redirectable.products.setup
-Doomph.redirection.palladio_projects=index:/redirectable.projects.setup->https://updatesite.palladio-simulator.com/palladio-bench-oomph/nightly/plain/setups/redirectable.projects.setup
```
3. Start the Installer and activate the PalladioSimulator.org Product Catalog (Click on the three lines in the upper right corner -> Product Catalogs -> Ensure PalladioSimulator.org is ticked)
4. If you would like to clone Palladio Projects as part of the setup you need to switch to advanced mode
5. You can now install either _Palladio Bench_, _Eclipse Modeling Tools_ or any of your preferred Eclipse flavours.
6. (Only in advanced mode) After selecting the product you can select which source code projects should be cloned. You need to ensure that PalladioSimulator.org is again activated (Folder icon in the upper right corner). Simply tick the projects which you would like to have imported into your Workspace. Dependencies of the projects are resolved automatically.
7. (Only in advanced mode) On the next page you are asked to provide folders and paths for 
  * the new eclipse executable (basically the eclipse folder, but without any plugins or features)
  * the workspace location
  * the folder and a naming convention where git repositories are cloned to
  * for each project the Github clone url
8. Once you finished the installation dialog the new Eclipse instance is provisioned and started by default.
9. Upon the first start the IDE begins cloning the git repositories (if they are not already present), imports the projects into the workspace and creates a Working Set per Palladio project. It also tries to resolve any missing dependencies via the update sites provided in the respective projects' target platforms.

## Add a project to your existing workspace
If your project has been set-up using these instructions you can simply add new Palladio Projects to your workspace using the _Import Projects_ wizard. Simply click File->Import->Oomph/Projects into Workspace and select the desired project from the PalladioSimulator.org catalog.

If the IDE was not set up using the Eclipse Installer make sure to install the Oomph extension (installed by default on the standard IDE flavors of Eclipse 2019-06) and add the parameters above to the eclipse.ini file of your IDE. Thereafter, you can simply use the import wizard to add projects to your workspace.
