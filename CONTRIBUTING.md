# Contributing to Reedelk

Welcome to the Reedelk developers community! 
If you are looking for help, thinking about getting involved
in the project or trying to report a bug, you are in the right place!

## Table of Contents

- [Where to seek for help?](#where-to-seek-for-help)
  - [Community Edition](#community-edition)
- [Where to report bugs?](#where-to-report-bugs)
- [Where to submit feature requests?](#where-to-submit-feature-requests)
- [Contributing](#contributing)
  - [Proposing new components](#proposing-new-components)
  - [Submitting a patch](#submitting-a-patch)
    - [Git branches](#git-branches)
    - [Commit message format](#commit-message-format)


## Where to seek for help?

### Community Edition

You can get help from our [Slack Developers Community](https://join.slack.com/t/reedelk/shared_invite/zt-fz3wx56f-XDylXpqXERooKeOtrhdZug). 
It is a great place for asking questions, giving advice and staying up-to-date with the
latest announcements. The Slack Developers Community is frequented by
other Reedelk Developers and by Reedelk maintainers.

**Please avoid opening GitHub issues for general questions or help**, as those
should be reserved for actual bug reports. The Reedelk community is welcoming and
more than willing to assist you on Slack channels!

[Back to TOC](#table-of-contents)

## Where to report bugs?

Feel free to [submit an issue](https://github.com/reedelk/reedelk-runtime/issues/new) on
the GitHub repository, we would be grateful to hear about it! Please make sure
to respect the GitHub issue template, and include:

1. A summary of the issue
2. A list of steps to reproduce the issue
3. The version of Reedelk you encountered the issue with
4. The Reedelk configuration, or the parts that are relevant to your issue
5. A screenshot of the integration flow you are having the issue with (or its JSON definition)

If you wish, you are more than welcome to propose a patch to fix the issue!
See the [Submit a patch](#submitting-a-patch) section for more information
on how to best do so.

[Back to TOC](#table-of-contents)

## Where to submit feature requests?

You can [submit an issue](https://github.com/reedelk/reedelk-runtime/issues/new) for feature
requests. Please add as much detail as you can.

You are also welcome to propose patches adding new features. See the section
on [Submitting a patch](#submitting-a-patch) for details.

[Back to TOC](#table-of-contents)

## Contributing

We welcome contributions of all kinds, you do not need to code to be helpful!
All of the following tasks are noble and worthy contributions that you can
make without coding:

- Reporting a bug (see the [report bugs](#where-to-report-bugs) section)
- Helping other members of the community on the support channels
- Fixing a typo in the code
- Proposing new integration components [proposing-new-components](#proposing-new-components)
- Providing your feedback on the proposed features and designs
- Reviewing Pull Requests

If you wish to contribute code (features or bug fixes), see the [Submitting a
patch](#submitting-a-patch) section.

[Back to TOC](#table-of-contents)

### Proposing New Components

If you have a new integration component to suggest being part of the Reedelk components library
you can [submit an issue](https://github.com/reedelk/reedelk-runtime/issues/new) for feature
requests. Please add as much detail as you can.

### Submitting a patch

Feel free to contribute fixes or minor features, we love to receive Pull
Requests! If you are planning to develop a larger feature, come talk to us
first!

When contributing, please follow the guidelines provided in this document. They
will cover topics such as the different Git branches we use or the commit message
format to use.

Once you have read them, and you are ready to submit your Pull Request, be sure
to verify a few things:

- Your work was based on the appropriate branch (`master` vs. `development`), and you
  are opening your Pull Request against the appropriate one
- Your commit history is clean: changes are atomic and the git message format
  was respected
- Rebase your work on top of the base branch (seek help online on how to use
  `git rebase`; this is important to ensure your commit history is clean and
   linear)
- The tests are passing: run `mvn test`
- Do not update `releases.json` yourself. Your change will be included there in
  due time if it is accepted.

If the above guidelines are respected, your Pull Request has all its chances
to be considered and will be reviewed by a maintainer.

If your Pull Request was accepted and fixes a bug, adds functionality, or
makes it significantly easier to use or understand Reedelk, congratulations!
You are now an official contributor to Reedelk.

Your change will be included in the subsequent release, and we will
not forget to include your name if you are an external contributor. :wink:

[Back to TOC](#table-of-contents)

#### Git branches

We work on two branches: `master`, where non-breaking changes land, and `development`,
where important features or breaking changes land in-between major releases.

When contributing to Reedelk, this distinction is important. Please ensure that
you are basing your work on top of the appropriate branch, it might save you
some time down the road!

If you have write access to the GitHub repository, please follow the following
naming scheme when pushing your branch(es):

- `feature/foo-bar` for new features
- `bugfix/foo-bar` for bug fixes
- `tests/foo-bar` when the change concerns only the test suite
- `refactor/foo-bar` when refactoring code without any behavior change

[Back to TOC](#table-of-contents)

#### Commit message format

To maintain a healthy Git history, we ask of you that you write your commit
messages as follows:

- The tense of your message must be **present**
- The message must be prefixed by a **type** and a **scope**

The format for a commit message is as follows:

```[type][scope][message]```

Where type is one of:
- `feature`: code belonging to a new feature
- `bugfix`: code belonging to a bugfix
- `tests`: code belonging to tests
- `refactor`: code belonging to a refactoring or code improvements
- `misc`: changes to assets, strings, pom.xml and configuration files and so on.

Scope is one of:
- `module-descriptor`: if the change refers to the **module-descriptor** project
- `module-maven-plugin`: if the change refers to the **module-maven-plugin** project
- `module-parent`: if the change refers to the **module-parent** project
- `runtime-admin-console`: if the change refers to the **runtime-admin-console** project
- `runtime-api`: if the change refers to the **runtime-api** project
- `runtime-commons`: if the change refers to the **runtime-commons** project
- `runtime-launcher`: if the change refers to the **runtime-launcher** project
- `runtime-platform`: if the change refers to the **runtime-platform** project

Examples: 
```
bugfix: runtime-platform: fixed serialization of JSON primitives.
```
```
feature: module-descriptor: added @Mandatory annotation to mark not empty properties.
```
