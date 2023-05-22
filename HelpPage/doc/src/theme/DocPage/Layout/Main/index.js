import React, { Fragment, useContext, useEffect, useState } from "react";
import clsx from "clsx";
import { useDocsSidebar } from "@docusaurus/theme-common/internal";
import styles from "./styles.module.css";
import Navbar from "@site/src/theme/Navbar";
import { FooterContext } from "@site/src/utils/FooterContext";
import Footer from "@theme/Footer";
import { NavbarHR } from "@site/src/components/NavbarHR";
import { DocumentHeightContext } from "@site/src/utils/DocumentHeightContext";

export default function DocPageLayoutMain({ hiddenSidebarContainer, children }) {

  let sidebar = useDocsSidebar();
  try {
    sidebar = useDocsSidebar();
  } catch(e) {
    console.error(e);
  }

  const [ noFooter ] = useContext(FooterContext);
  const [ height ] = useContext(DocumentHeightContext);
  const [ containerHeight, setContainerHeight ] = useState(window.innerHeight);


  const navbarHeight = () => {
    let h = 0;
    for(const elem of document.querySelectorAll("#nav-with-breadcrumbs")) {
      if(h < elem.clientHeight) {
        h = elem.clientHeight;
      }
    }
    return h;
  };

  //ugh, hate doing this (x3). but, again, i am left with no choice.
  const observer = new ResizeObserver((entries) => {
    window.requestAnimationFrame(() => {
      for(const entry of entries) {
        if(entry.target === null || entry.target === undefined)return;
        setContainerHeight(entry.target.clientHeight);
      }
    });
  });

  useEffect(() => {
    observer.observe(document.querySelector("div.doc"));
    return () => {
      observer.disconnect();
    };
  }, []);

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
          <div className={"doc-scroll thin-scrollbar"} style={{ height: `${ Math.max(containerHeight - 4 - Math.max(height, navbarHeight()), 0) }px` }}>
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
          <div className={"scrollbar-footer"}/>
        </div>
      </main>
    </Fragment>
  );
}
