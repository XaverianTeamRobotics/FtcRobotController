import React from "react";
import clsx from "clsx";
import { translate } from "@docusaurus/Translate";
import { ThemeClassNames } from "@docusaurus/theme-common";
import { useBackToTopButton } from "@docusaurus/theme-common/internal";
import styles from "./styles.module.css";
export default function BackToTopButton() {
  const { shown, scrollToTop } = useBackToTopButton({ threshold: 300 });
  // manually disable this button, because I don't want to make it work with this mess
  const enable = false;
  return (
    <button
      aria-label={translate({
        id: "theme.BackToTopButton.buttonAriaLabel",
        message: "Scroll back to top",
        description: "The ARIA label for the back to top button",
      })}
      className={clsx(
        "clean-btn",
        ThemeClassNames.common.backToTopButton,
        styles.backToTopButton,
        shown && enable && styles.backToTopButtonShow,
      )}
      type="button"
      onClick={scrollToTop}
    />
  );
}
