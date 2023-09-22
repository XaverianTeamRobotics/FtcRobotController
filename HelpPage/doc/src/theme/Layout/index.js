import React, { useState } from "react";
import clsx from "clsx";
import ErrorBoundary from "@docusaurus/ErrorBoundary";
import { PageMetadata, SkipToContentFallbackId, ThemeClassNames, } from "@docusaurus/theme-common";
import { useKeyboardNavigation } from "@docusaurus/theme-common/internal";
import SkipToContent from "@theme/SkipToContent";
import AnnouncementBar from "@theme/AnnouncementBar";
import Navbar from "@theme/Navbar";
import LayoutProvider from "@theme/Layout/Provider";
import ErrorPageContent from "@theme/ErrorPageContent";
import styles from "./styles.module.css";
import { FooterContext } from "@site/src/utils/FooterContext";

export default function Layout(props) {
  const {
    children,
    noFooter,
    wrapperClassName,
    // Not really layout-related, but kept for convenience/retro-compatibility
    title,
    description,
  } = props;
  useKeyboardNavigation();
  return (
    <LayoutProvider>
      <PageMetadata title={title} description={description} />

      <SkipToContent />

      <AnnouncementBar />

      <div className={"h-0"}>
        <Navbar disabled screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now={props.nav}/>
      </div>

      <FooterContext.Provider value={useState(noFooter ?? false)}>
        <div
          id={SkipToContentFallbackId}
          className={`${ clsx(
            ThemeClassNames.wrapper.main,
            styles.mainWrapper,
            wrapperClassName,
          ) } ${ props.nav ? "thin-scrollbar mdxpgc" : "" }`}>
          <ErrorBoundary fallback={(params) => <ErrorPageContent {...params} />}>
            {children}
          </ErrorBoundary>
        </div>
      </FooterContext.Provider>
    </LayoutProvider>
  );
}
