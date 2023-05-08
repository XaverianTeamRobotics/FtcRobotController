import React, { useContext } from "react";
import clsx from "clsx";
import { useDocsSidebar } from "@docusaurus/theme-common/internal";
import styles from "./styles.module.css";
import Navbar from "@site/src/theme/Navbar";
import { FooterContext } from "@site/src/utils/FooterContext";
import Footer from "@theme/Footer";
import { DocumentHeightContext } from "@site/src/utils/DocumentHeightContext";
export default function DocPageLayoutMain({ hiddenSidebarContainer, children }) {
  const sidebar = useDocsSidebar();
  const [ noFooter ] = useContext(FooterContext);
  const [ height ] = useContext(DocumentHeightContext);
  const navbarHeight = () => {
    let h = 0;
    for(const elem of document.querySelectorAll("#nav-with-breadcrumbs")) {
      console.log(elem);
      if(h < elem.clientHeight) {
        h = elem.clientHeight;
      }
    }
    return h;
  };
  return (
    <main
      className={clsx(
        styles.docMainContainer,
        (hiddenSidebarContainer || !sidebar) && styles.docMainContainerEnhanced,
      )}>
      <div className={"doc"}>
        <div className={"docnav"}>
          <Navbar/>
        </div>
        <div
          className={`${ clsx(
            "container padding-top--md padding-bottom--lg",
            styles.docItemWrapper,
            hiddenSidebarContainer && styles.docItemWrapperEnhanced,
          ) } doc-lm doc-mt`}
          style={{
            marginTop: `${ height - navbarHeight() }px`
          }}
        >
          {children}
        </div>
        {!noFooter && <Footer />}
      </div>
    </main>
  );
}
