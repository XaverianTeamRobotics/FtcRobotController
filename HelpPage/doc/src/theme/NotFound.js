import React from "react";
import Translate, { translate } from "@docusaurus/Translate";
import { PageMetadata } from "@docusaurus/theme-common";
import Layout from "@theme/Layout";

export default function NotFound() {
  return (
    <>
      <PageMetadata
        title={translate({
          id: "theme.NotFound.title",
          message: "Page Not Found",
        })}
      />
      <Layout>
        <main className="vp nf">
          <h1 className="nft">
            <Translate
              id="theme.NotFound.title"
              description="The title of the 404 page">
              404 Not Found
            </Translate>
          </h1>
          <p className="nfp">
            ¯\_(ツ)_/¯
          </p>
        </main>
      </Layout>
    </>
  );
}
