import React, { useContext, useEffect, useState } from "react";
import clsx from "clsx";
import { HtmlClassNameProvider, PageMetadata, ThemeClassNames, } from "@docusaurus/theme-common";
import Layout from "@theme/Layout";
import MDXContent from "@theme/MDXContent";
import TOC from "@theme/TOC";
import styles from "./styles.module.css";
import Footer from "@site/src/theme/Footer";
import { FooterContext } from "@site/src/utils/FooterContext";

export default function MDXPage(props) {
  const { content: MDXPageContent } = props;
  const {
    metadata: { title, description, frontMatter },
  } = MDXPageContent;
  const { wrapperClassName, hide_table_of_contents: hideTableOfContents } =
    frontMatter;
  const [ height, setHeight ] = useState(60);
  useEffect(() => {
    const e = document.getElementById("nav-with-breadcrumbs");
    if(e !== null && e !== undefined) {
      setHeight(e.clientHeight);
    }
  }, );
  const [ noFooter ] = useContext(FooterContext);
  return (
    <HtmlClassNameProvider
      className={clsx(
        wrapperClassName ?? ThemeClassNames.wrapper.mdxPages,
        ThemeClassNames.page.mdxPage,
      )}>
      <PageMetadata title={title} description={description} />
      <Layout nav >
        <main className="container--page">
          <div style={{ height: `${ height }px` }}/>
          <div className={clsx("row", styles.mdxPageWrapper)}>
            <div className={clsx("col npx", !hideTableOfContents && "col--12")}>
              <article>
                <MDXContent>
                  <MDXPageContent />
                </MDXContent>
              </article>
            </div>
            {!hideTableOfContents && MDXPageContent.toc.length > 0 && (
              <div className="col col--2">
                <TOC
                  toc={MDXPageContent.toc}
                  minHeadingLevel={frontMatter.toc_min_heading_level}
                  maxHeadingLevel={frontMatter.toc_max_heading_level}
                />
              </div>
            )}
          </div>
          {!noFooter && <span className={"pgf"}><Footer /></span>}
        </main>
      </Layout>
    </HtmlClassNameProvider>
  );
}
