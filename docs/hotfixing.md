# Hotfix Guide
> Updated: 08-19-2022

Here you will find a general guide on how to apply a hotfix to different deployed environments.

## Creating a Hotfix Branch

### Finding the version number

To make the process more concise, identify what environment you want to apply the hotfix to. To find what version of the Portal is deployed in the identified environment

Go to the [morsco-reece/environments repository](https://github.com/morsco-reece/environments/). Each environment has a directory in the `morsco-reece/environments` repository. Reference the chart below to identify the appropriate directory to enter. 

| Environment             | Directory             | 
| ----------------------- | --------------------- | 
|  development            | n/a - (auto deployed) | 
|  testing                | `ecomm-test`          | 
|  uat                    | `ecomm-uat`           | 
|  production             | n/a - (auto deployed) | 
|  sandbox / prod replica | `ecomm-production`    |

If the environment is "auto deployed", skip the steps below.

### Creating the branch

Each directory contains a `Chart.yaml` file that contains the version numbers for each deployed application in the directory's associated environment. Once in the file, follow the steps below:

1. Find the dependency named `max-portal` and note it's version number. This is the deployed version.
2. Check the `Portal` repository for a release branch. Release branches follow the naming pattern of `release-v[version number w/o patch number]`. For example, "0.123.4" would be `release-v0.123`. 
   - If the branch does not exist, find the [tagged release](https://github.com/morsco-reece/portal/releases) with the matching version number and follow these steps:
       1. Checkout the tagged release ex: `git checkout v0.123.0`.
       2. Open the CircleCI dashboard for [Portal](https://app.circleci.com/pipelines/github/morsco-reece/portal).
       3. Create a branch based on the checkout out tag and name it `release-v[version number w/o patch number]`.
       4. Push the branch and cancel any workflows kicked off by creating the release branch.
3. Create a new branch off of the release branch and begin making your code changes!

## Deploying a Hotfix by Environment

Guides organized by environment.

### Development

Any PR merged into the `develop` branch will be automatically deployed to the `dev` environment. No further action is required.

### Test

1. Create a PR to merge your working branch into the release branch. Ex: `max-123-my-hotfix-change` -> `release-v0.123`
2. Once merged, a new tagged release will be created with an incremented patch number. For example if you are merging into `release-v0.123` and the current version number is `0.123.4`, the new tagged release would be named `0.123.5`.
3. After the release is created, clone the `morsco-reece/environments` repository and create a branch off of `master`. 
4. In `ecomm-test/Chart.yaml` change the version number of the `max-portal` dependency to the version number of the new release with it's new patch number.
5. Create a PR for your branch into `master`.
6. Once the PR is merged, the deployment process will be kicked off.

### UAT

1. ***Repeat steps 1 - 3 from [Deploying a Hotfix by Environment - Test](#test).***
2. In `ecomm-uat/Chart.yaml` change the version number of the `max-portal` dependency to the version number of the new release with it's new patch number.
4. ***Repeat steps 5 & 6 from [Deploying a Hotfix by Environment - Test](#test)***

### Production

Any PR merged into the `master` branch will be automatically deployed to the `production` environment. No further action is required.

### Sandbox

1. ***Repeat steps 1 - 3 from [Deploying a Hotfix by Environment - Test](#test).***
4. In `ecomm-uat/Chart.yaml` change the version number of the `max-portal` dependency to the version number of the new release with it's new patch number.
3. ***Repeat steps 5 & 6 from [Deploying a Hotfix by Environment - Test](#test)***

## Merging Down a Hotfix

Once the hotfix is deployed to the targeted environment, the changes should be propagated to other environments. 

The process is similar to processes described in [Deploying a Hotfix by Environment](#deploying-a-hotfix-by-environment) with some alterations. The general process is as follows. 

1. Follow the process outlined in [creating a hotfix branch](#creating-a-hotfix-branch) to make a branch based on the targeted environment's release branch.
2. Find the commit hash of the hotfix that was deployed to the original target environment and copy it. Ideally, use the squash merge's commit hash.
3. Using `git cherry-pick`, pull in the commit identified in the previous step and push the branch. 
4. Create a PR from your branch into the lower environment's release branch. Once merged, the build process will be kicked off. 
5. Then follow the steps in [Deploying a Hotfix by Environment](#deploying-a-hotfix-by-environment) for setting the release versions in the `morsco-reece/environments` repository.
6. Repeat this process for any other environments necessary.