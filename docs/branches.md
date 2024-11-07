---
title: Branches
slug: branches
---

# Branches

Branches are a way to work on different versions of the project at the same time. 
By default, every repository has a `master` branch. You can create new branches and switch between them.
Think of it like a tree, where all branches are connected to the `master` branch.

Because the `master` branch is the most important branch, it has rules that prevent
certain people from making changes to it. This is to prevent mistakes and to keep the `master` branch clean.
Now, in order to make changes to the `master` branch, you need to create a new branch and make changes there.
Once you are done, you can merge the changes back into the `master` branch through what is 
called a *pull request*.

## Creating a Branch

To create a new branch using Android Studio, follow these steps:
  * Click on the Git menu either in the bottom right corner or in the top bar
  * Select the branch you want to use as the base for your new branch
  * Select `New Branch from (<branch name>)` from the dropdown menu
  * The new branch will be created and you will be switched to it

## Switching Branches

To switch between branches using Android Studio, follow these steps:
  * Click on the Git menu either in the bottom right corner or in the top bar
  * Select the branch you want to switch to from the dropdown menu
  * Click the `Checkout` button

> [!WARN]
> When checking out a branch, make sure to commit or stash your changes first,
> otherwise you may lose some changes

## The Shelf

The shelf is a place where you can store changes that you are not ready to commit yet.
To use the shelf, follow these steps:
  * Click on the Commit menu in the left sidebar
  * Select changes you want to stash and click the Shelve icon

In the commit menu, there is another tab called *Shelf* that contains
shelved files. To unshelve something, right click the item and press
`Unshelve`.

---

# Pull Requests

Pull requests merge two branches together. They allow us to easily review changes on
a branch. To create a pull request, there is a button called `Pull Requests` with the
Github icon in the left sidebar. When you click that, there will be a list of all open pull
requests. To create one, click the plus button at the top. It will show you the commits that
will be included and give you a box to input a title and a description. At the top,
it will show you the destination branch on the left and the source branch on the right.
Once you are done, click the `Create Pull Request` button at the bottom. The pull request will be made
online and someone will have to review it before the changes are incorporated.