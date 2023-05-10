import React, { Fragment, useContext, useEffect, useState } from "react";
import clsx from "clsx";
import { useDocsSidebar } from "@docusaurus/theme-common/internal";
import styles from "./styles.module.css";
import Navbar from "@site/src/theme/Navbar";
import { FooterContext } from "@site/src/utils/FooterContext";
import Footer from "@theme/Footer";
import { NavbarBreadcrumbHeightContext } from "@site/src/utils/NavbarBreadcrumbHeightContext";
import { NavbarHR } from "@site/src/components/NavbarHR";
import { DocumentHeightContext } from "@site/src/utils/DocumentHeightContext";
export default function DocPageLayoutMain({ hiddenSidebarContainer, children }) {

  const sidebar = useDocsSidebar();

  const [ noFooter ] = useContext(FooterContext);
  const [ height ] = useContext(DocumentHeightContext);

  const navbarHeight = () => {
    let h = 0;
    for(const elem of document.querySelectorAll("#nav-with-breadcrumbs")) {
      if(h < elem.clientHeight) {
        h = elem.clientHeight;
      }
    }
    return h;
  };

  return (
    <Fragment>
      <NavbarHR/>
      <main
        className={clsx(
          styles.docMainContainer,
          (hiddenSidebarContainer || !sidebar) && styles.docMainContainerEnhanced,
        )}>
        <div className={"doc"}>
          <div className={"docnav"}>
            <Navbar/>
          </div>
          <div style={{ height: `${ Math.max((height - navbarHeight()), 0) }px` }}/>
          <div className={"doc-scroll"} style={{ height: `${ Math.max(window.innerHeight - Math.max(height, navbarHeight()), 0) }px` }}>
            <div
              className={`${ clsx(
                "container padding-top--md padding-bottom--lg",
                styles.docItemWrapper,
                hiddenSidebarContainer && styles.docItemWrapperEnhanced,
              ) } doc-lm doc-mt`}
            >
              {children}
            </div>
            {!noFooter && <Footer />}
          </div>
        </div>
      </main>
    </Fragment>
  );
}
