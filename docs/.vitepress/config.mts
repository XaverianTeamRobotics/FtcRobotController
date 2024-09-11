import { defineConfigWithTheme } from 'vitepress'
import type { ThemeConfig } from 'vitepress-carbon'
import baseConfig from 'vitepress-carbon/config'

// https://vitepress.dev/reference/site-config
export default defineConfigWithTheme<ThemeConfig>({
  extends: baseConfig,
  title: "Hawk Robotics Documentation",
  description: "Documentation for the Ftc Robot Controller",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' }
    ],

    sidebar: [
      { text: 'Welcome', link: '/'},
      { text: 'OpModes', link: '/opmodes'},
      { text: 'Hardware', link: '/hardware'},
    ],

    search: {
      provider: 'local',
    },

    socialLinks: [
      { icon: 'github', link: 'https://github.com/XaverianTeamRobotics/FtcRobotController' }
    ]
  },
  cleanUrls: true
})
