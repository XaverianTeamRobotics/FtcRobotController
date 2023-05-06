# Website

This website is built using [Docusaurus 2](https://docusaurus.io/), a modern static website generator.

### Installation

```
$ npm i
```

### Local Development

```
$ npm run start
```

### Build

```
$ npm run build
```

### Deployment

Using SSH:

```
$ USE_SSH=true npm run deploy
```

Not using SSH:

```
$ GIT_USER=<Your GitHub username> npm run deploy
```

### Notes

* When we update a major version (defined by SemVer *\<major>*.*\<minor>*.*\<patch>* spec), we might break things in `/src/theme/`. To fix any broken components, reswizzle them and port the code we changed over to the new component. Keep that in mind.