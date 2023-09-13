import React, { Fragment, useContext } from "react";
import { ErrorCauseBoundary, useThemeConfig } from "@docusaurus/theme-common";
import { splitNavbarItems, useNavbarMobileSidebar, } from "@docusaurus/theme-common/internal";
import NavbarItem from "@theme/NavbarItem";
import NavbarColorModeToggle from "@theme/Navbar/ColorModeToggle";
import SearchBar from "@theme/SearchBar";
import NavbarMobileSidebarToggle from "@theme/Navbar/MobileSidebar/Toggle";
import NavbarSearch from "@theme/Navbar/Search";
import styles from "./styles.module.css";
import NavbarLogo from "@site/src/theme/Navbar/Logo";
import DocBreadcrumbsWrapper from "@site/src/theme/DocBreadcrumbs";
import { NavReenabler } from "@site/src/utils/NavReenabler";

function useNavbarItems() {
  return useThemeConfig().navbar.items;
}
function NavbarItems({ items }) {
  return (
    <>
      {items.map((item, i) => (
        <ErrorCauseBoundary
          key={i}
          onError={(error) =>
            new Error(
              `A theme navbar item failed to render.
Please double-check the following navbar item (themeConfig.navbar.items) of your Docusaurus config:
${ JSON.stringify(item, null, 2) }`,
              { cause: error },
            )
          }>
          <NavbarItem {...item} />
        </ErrorCauseBoundary>
      ))}
    </>
  );
}
function NavbarContentLayout({ left, right }) {
  return (
    <div className="navbar__inner">
      <div id="doc-breadcrumbs" className="navbar__items nav-left">{left}</div>
      <div className="navbar__items navbar__items--right h-nav">{right}</div>
    </div>
  );
}
export default function NavbarContent({ disabled }) {
  const mobileSidebar = useNavbarMobileSidebar();
  const items = useNavbarItems();
  const [ leftItems, rightItems ] = splitNavbarItems(items);
  const searchBarItem = items.find((item) => item.type === "search");
  const [ screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now ] = useContext(NavReenabler);
  return (
    <Fragment>
      <NavbarContentLayout
        left={
          <>
            {!mobileSidebar.disabled && <Fragment>
              <NavbarMobileSidebarToggle />
            </Fragment>
            }
            <div className={"w-full sm:flex"}>
              <div className={`${ !screw_it_i_dont_care_if_its_disabled_i_want_to_enable_it_now ? "d-n" : "mdln" }`}>
                <NavbarLogo />
              </div>
            </div>
            <div className={"md-disp flex flex-col place-center"}>
              { !disabled &&
                <DocBreadcrumbsWrapper navbarinsert className={"flex m-0"}/>
              }
            </div>
            <div className={"nav-left"}>
              <NavbarItems items={leftItems} />
            </div>
            {!mobileSidebar.disabled && <Fragment>
              <NavbarColorModeToggle className="sm-disp margin-left--md" />
            </Fragment>}
          </>
        }
        right={
          // Ask the user to add the respective navbar items => more flexible

          // hi!! im the user. that is precisely why i am swizzling this. cant put the things where i want them :(

          <>
            <NavbarItems items={rightItems} />
            <NavbarColorModeToggle className={styles.colorModeToggle} />
            {!searchBarItem && (
              <NavbarSearch>
                <SearchBar />
              </NavbarSearch>
            )}
          </>
        }
      />
    </Fragment>
  );
}
