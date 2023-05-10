// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const date = () => {
  const year = new Date().getFullYear();
  if(year > 2023) {
    return `2023–${ year }`;
  }
  return `${ year }`;
};

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: "19460 | Documentation",
  favicon: "img/favicon.png",

  // Set the production url of your site here
  url: "https://robotics.xbhs.net",
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: "/docs",

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: "XaverianTeamRobotics", // Usually your GitHub org/user name.
  projectName: "FtcRobotController", // Usually your repo name.

  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",

  // Even if you don't use internalization, you can use this field to set useful
  // metadata like html lang. For example, if your site is Chinese, you may want
  // to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: "en",
    locales: [ "en" ],
  },

  presets: [
    [
      "classic",
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          routeBasePath: "/",
          sidebarPath: require.resolve("./sidebars.js"),
        },
        blog: false,
        pages: false,
        theme: {
          customCss: require.resolve("./src/css/custom.css"),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      navbar: {
        title: "19460 Hawks Robotics",
        logo: {
          alt: "Hawkbot",
          src: "img/favicon.png",
        },
        items: [
          {
            href: "../",
            label: "Home",
            position: "right",
          },
          {
            href: "https://github.com/XaverianTeamRobotics/",
            label: "GitHub",
            position: "right",
          },
        ]
      },
      colorMode: {
        defaultMode: "light",
        disableSwitch: true
      },
      prism: {
        theme: require("prism-react-renderer/themes/dracula"),
        darkTheme: require("prism-react-renderer/themes/dracula"),
      },
      footer: {
        style: "light",
        copyright: `Copyright © <wbr/>${ date() } <wbr/>Xaverian Brothers High School. <wbr/>All rights reserved.`,
      }
    }),
};

module.exports = config;
