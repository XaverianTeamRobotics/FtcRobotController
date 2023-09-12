import React, { useState } from "react";
import clsx from "clsx";
import { useThemeConfig } from "@docusaurus/theme-common";
import { useHideableNavbar, useNavbarMobileSidebar, } from "@docusaurus/theme-common/internal";
import { translate } from "@docusaurus/Translate";
import NavbarMobileSidebar from "@theme/Navbar/MobileSidebar";
import styles from "./styles.module.css";
import { NavReenabler } from "@site/src/utils/NavReenabler";

function NavbarBackdrop(props) {
  return (
    <div
      role="presentation"
      {...props}
      className={clsx("navbar-sidebar__backdrop", props.className)}
    />
  );
}
export default function NavbarLayout({ children, disabled, screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now }) {
  const {
    navbar: { hideOnScroll, style },
  } = useThemeConfig();
  const mobileSidebar = useNavbarMobileSidebar();
  const { navbarRef, isNavbarVisible } = useHideableNavbar(hideOnScroll);
  return (
    <NavReenabler.Provider value={useState(screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now)}>
      <div className={`${ disabled ? "h-0" : "" }${ screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now ? "-ignore nb bgp" : "" }`}>
        <nav
          id={"nav-with-breadcrumbs"}
          ref={navbarRef}
          aria-label={translate({
            id: "theme.NavBar.navAriaLabel",
            message: "Main",
            description: "The ARIA label for the main navigation",
          })}
          className={`${ clsx(
            "navbar",
            "navbar--fixed-top",
            hideOnScroll && [
              styles.navbarHideable,
              !isNavbarVisible && styles.navbarHidden,
            ],
            {
              "navbar--dark": style === "dark",
              "navbar--primary": style === "primary",
              "navbar-sidebar--show": mobileSidebar.shown,
            },
          ) } ${ disabled ? "h-0" : "" }${ screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now ? "-ignore" : "" } ${ disabled ? "d-n" : "" }${ screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now ? "-ignore nb-ignore" : "" }`}>
          {children}
          <NavbarBackdrop onClick={mobileSidebar.toggle} />
          <NavbarMobileSidebar />
          <div className={"nbb"}/>
        </nav>
      </div>
    </NavReenabler.Provider>
  );
}
